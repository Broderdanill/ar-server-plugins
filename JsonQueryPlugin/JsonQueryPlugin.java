package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import com.jayway.jsonpath.JsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        try {
            Object result = JsonPath.read(json, "$." + path);

            ObjectMapper mapper = new ObjectMapper();
            String jsonResult = mapper.writeValueAsString(result);  // konvertera till korrekt JSON-sträng

            List<Value> results = new ArrayList<>();
            results.add(new Value(jsonResult));
            return results;

        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10021, "JSON parsing/query error: " + e.getMessage()));
            throw new ARException(statusList);
        }
    }
}
