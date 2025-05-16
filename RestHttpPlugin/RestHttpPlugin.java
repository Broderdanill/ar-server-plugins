package RestHttpPlugin;


import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;


import java.io.*;
import java.net.*;
import java.util.*;


import org.json.JSONObject;


public class RestHttpPlugin extends ARFilterAPIPlugin {


    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) {
        JSONObject result = new JSONObject();


        try {
            if (args == null || args.size() < 2) {
                result.put("success", false);
                result.put("error", "Minst HTTP-metod och URL krävs.");
                return wrapResult(result.toString());
            }


            String method = ((String) args.get(0).getValue()).toUpperCase();
            String urlStr = (String) args.get(1).getValue();
            String headersJson = args.size() > 2 ? (String) args.get(2).getValue() : null;
            String data = args.size() > 3 ? (String) args.get(3).getValue() : null;
            String authType = args.size() > 4 ? (String) args.get(4).getValue() : "none";
            String authValue1 = args.size() > 5 ? (String) args.get(5).getValue() : null;
            String authValue2 = args.size() > 6 ? (String) args.get(6).getValue() : null;


            HttpResult httpResult = doHttpCall(method, urlStr, headersJson, data, authType, authValue1, authValue2);


            result.put("success", true);
            result.put("status", httpResult.status);
            result.put("body", httpResult.body);


        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "HTTP-anrop misslyckades: " + e.getMessage());
        }


        return wrapResult(result.toString());
    }


    private List<Value> wrapResult(String json) {
        List<Value> results = new ArrayList<>();
        results.add(new Value(json));
        return results;
    }


    private static class HttpResult {
        int status;
        String body;
    }


    private HttpResult doHttpCall(String method, String urlStr, String headersJson, String data,
                                  String authType, String authValue1, String authValue2) throws IOException {
        if ("cert".equalsIgnoreCase(authType) && authValue1 != null && authValue2 != null) {
            System.setProperty("javax.net.ssl.keyStore", authValue1);
            System.setProperty("javax.net.ssl.keyStorePassword", authValue2);
            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        }


        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);


        switch (authType.toLowerCase()) {
            case "basic":
                if (authValue1 != null && authValue2 != null) {
                    String encoded = Base64.getEncoder().encodeToString((authValue1 + ":" + authValue2).getBytes());
                    conn.setRequestProperty("Authorization", "Basic " + encoded);
                }
                break;
            case "bearer":
                if (authValue1 != null) {
                    conn.setRequestProperty("Authorization", "Bearer " + authValue1);
                }
                break;
            case "header":
                if (authValue1 != null) {
                    String[] parts = authValue1.split(":", 2);
                    if (parts.length == 2) {
                        conn.setRequestProperty(parts[0].trim(), parts[1].trim());
                    }
                }
                break;
        }


        if (headersJson != null && !headersJson.isEmpty()) {
            JSONObject headers = new JSONObject(headersJson);
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.getString(key));
            }
        }


        if (data != null && !data.isEmpty() && Arrays.asList("POST", "PUT", "PATCH").contains(method)) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes("UTF-8"));
            }
        }


        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 400) ? conn.getInputStream() : conn.getErrorStream();


        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        }


        HttpResult result = new HttpResult();
        result.status = status;
        result.body = sb.toString().trim();
        return result;
    }
}
