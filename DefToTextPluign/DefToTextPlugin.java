package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class DefAttachmentToTextPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        List<Value> results = new ArrayList<>();

        if (args == null || args.size() < 1 || !(args.get(0).getValue() instanceof AttachmentValue)) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(StatusInfo.SEVERITY_ERROR, 10010, "Attachment input is required"));
            throw new ARException(statusList);
        }

        AttachmentValue attachment = (AttachmentValue) args.get(0).getValue();

        try (InputStream inputStream = context.getEntryBlobInputStream(attachment);
             InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
             BufferedReader br = new BufferedReader(reader)) {

            StringBuilder text = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append("\n");
            }

            results.add(new Value(text.toString().trim()));

        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(StatusInfo.SEVERITY_ERROR, 10011, "Error reading attachment: " + e.getMessage()));
            throw new ARException(statusList);
        }

        return results;
    }
}
