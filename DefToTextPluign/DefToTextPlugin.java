package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;

public class DefToTextPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        List<Value> results = new ArrayList<>();

        if (args == null || args.size() < 1 || !(args.get(0).getValue() instanceof AttachmentValue)) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10010, "Input must be a file attachment (.def)"));
            throw new ARException(statusList);
        }

        AttachmentValue attachment = (AttachmentValue) args.get(0).getValue();
        byte[] data = attachment.getValue();

        if (data == null || data.length == 0) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10011, "Attachment has no data. It must be provided during the current transaction."));
            throw new ARException(statusList);
        }

        try (
            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
            InputStreamReader reader = new InputStreamReader(byteStream, "UTF-8");
            BufferedReader br = new BufferedReader(reader)
        ) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append("\n");
            }

            results.add(new Value(text.toString().trim()));
        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10012, "Failed to read attachment content: " + e.getMessage()));
            throw new ARException(statusList);
        }

        return results;
    }
}
