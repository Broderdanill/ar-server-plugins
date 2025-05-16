package com.example;

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
        List<Value> results = new ArrayList<>();

        try {
            if (args == null || args.size() < 2) {
                results.add(new Value(0));
                results.add(new Value("Missing arguments"));
                results.add(new Value("At least HTTP method and URL are required."));
                return results;
            }

            String method = ((String) args.get(0).getValue()).toUpperCase();
            String urlStr = (String) args.get(1).getValue();
            String headersJson = args.size() > 2 ? (String) args.get(2).getValue() : null;
            String data = args.size() > 3 ? (String) args.get(3).getValue() : null;
            String authType = args.size() > 4 ? (String) args.get(4).getValue() : "none";
            String authValue1 = args.size() > 5 ? (String) args.get(5).getValue() : null;
            String authValue2 = args.size() > 6 ? (String) args.get(6).getValue() : null;
            String trustPath = args.size() > 7 ? (String) args.get(7).getValue() : null;
            String trustPass = args.size() > 8 ? (String) args.get(8).getValue() : null;

            urlStr = encodeUrlQuery(urlStr);

            HttpResult httpResult = doHttpCall(method, urlStr, headersJson, data, authType,
                                               authValue1, authValue2, trustPath, trustPass);

            results.add(new Value(httpResult.status));
            results.add(new Value(httpResult.statusText));
            results.add(new Value(httpResult.body));

        } catch (Throwable t) {
            results.add(new Value(0));
            results.add(new Value(t.getClass().getSimpleName()));
            results.add(new Value("Exception: " + t.getMessage()));
        }

        return results;
    }

    private String encodeUrlQuery(String originalUrl) {
        try {
            URL url = new URL(originalUrl);
            String base = url.getProtocol() + "://" + url.getHost() +
                    (url.getPort() != -1 ? ":" + url.getPort() : "") + url.getPath();
            String query = url.getQuery();

            if (query == null) return originalUrl;

            StringBuilder encoded = new StringBuilder();
            String[] parts = query.split("&");
            for (int i = 0; i < parts.length; i++) {
                String[] keyVal = parts[i].split("=", 2);
                String key = URLEncoder.encode(keyVal[0], "UTF-8");
                String val = keyVal.length > 1 ? URLEncoder.encode(keyVal[1], "UTF-8") : "";
                encoded.append(key).append("=").append(val);
                if (i < parts.length - 1) encoded.append("&");
            }

            return base + "?" + encoded.toString();
        } catch (Exception e) {
            return originalUrl;
        }
    }

    private static class HttpResult {
        int status;
        String statusText;
        String body;
    }

    private HttpResult doHttpCall(String method, String urlStr, String headersJson, String data,
                                  String authType, String authValue1, String authValue2,
                                  String trustStore, String trustPassword) throws IOException {

        if ("cert".equalsIgnoreCase(authType) && authValue1 != null && authValue2 != null) {
            System.setProperty("javax.net.ssl.keyStore", authValue1);
            System.setProperty("javax.net.ssl.keyStorePassword", authValue2);
            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

            if (trustStore != null && !trustStore.isEmpty()) {
                System.setProperty("javax.net.ssl.trustStore", trustStore);
            }

            if (trustPassword != null && !trustPassword.isEmpty()) {
                System.setProperty("javax.net.ssl.trustStorePassword", trustPassword);
            }
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
                    conn.setRequestProperty("Authorization", authValue1);
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
        String statusText = conn.getResponseMessage();
        InputStream is = (status >= 200 && status < 400) ? conn.getInputStream() : conn.getErrorStream();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        }

        HttpResult result = new HttpResult();
        result.status = status;
        result.statusText = statusText;
        result.body = sb.toString().trim();
        return result;
    }
}