package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.api.AttachmentValue;
import com.bmc.arsys.api.StatusInfo;

import java.util.Base64;
import java.util.List;
import java.util.ArrayList;

public class Base64ToAttachmentPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        if (args == null || args.size() < 2) {
            List<StatusInfo> statusList = new ArrayList<>();
            StatusInfo info = new StatusInfo(9956, 0L, "FATAL");
            info.setMessageText("Missing parameters. Expected: base64 string, filename");
            statusList.add(info);
            throw new ARException(statusList);
        }

        String base64Str = (String) args.get(0).getValue();
        String filename = (String) args.get(1).getValue();

        byte[] fileContent;
        try {
            fileContent = Base64.getDecoder().decode(base64Str);
        } catch (IllegalArgumentException e) {
            List<StatusInfo> statusList = new ArrayList<>();
            StatusInfo info = new StatusInfo(9957, 0L, "FATAL");
            info.setMessageText("Invalid Base64 input: " + e.getMessage());
            statusList.add(info);
            throw new ARException(statusList);
        }

        AttachmentValue attachment = new AttachmentValue();
        attachment.setName(filename);
        attachment.setValue(fileContent);

        List<Value> results = new ArrayList<>();
        results.add(new Value(attachment));
        return results;
    }
}
