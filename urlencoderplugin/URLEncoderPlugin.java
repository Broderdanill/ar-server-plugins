package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.api.StatusInfo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

public class URLEncoderPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        if (args == null || args.size() < 1) {
            List<StatusInfo> statusList = new ArrayList<>();
            StatusInfo info = new StatusInfo(9980, 0L, "FATAL");
            info.setMessageText("Missing input string to encode.");
            statusList.add(info);
            throw new ARException(statusList);
        }

        String input = (String) args.get(0).getValue();

        try {
            String encoded = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
            List<Value> results = new ArrayList<>();
            results.add(new Value(encoded));
            return results;
        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            StatusInfo info = new StatusInfo(9981, 0L, "FATAL");
            info.setMessageText("Encoding error: " + e.getMessage());
            statusList.add(info);
            throw new ARException(statusList);
        }
    }
}