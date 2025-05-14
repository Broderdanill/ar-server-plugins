package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.ArrayList;

public class JsonQueryPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        if (args == null || args.size() < 2) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10020, "Arguments required: json, jsonPath"));
            throw new ARException(statusList);
        }

        String json = (String) args.get(0).getValue();
        String path = (String) args.get(1).getValue();

        Object result;
        try {
            result = JsonPath.read(json, "$." + path); // adds root $
        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10021, "JSON parsing/query error: " + e.getMessage()));
            throw new ARException(statusList);
        }

        List<Value> results = new ArrayList<>();
        results.add(new Value(result.toString()));
        return results;
    }
}
