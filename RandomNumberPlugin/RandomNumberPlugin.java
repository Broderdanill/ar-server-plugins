package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class RandomNumberPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        int min = 1;
        int max = 100;

        if (args != null && args.size() >= 2) {
            min = (Integer) args.get(0).getValue();
            max = (Integer) args.get(1).getValue();
        }

        int result = new Random().nextInt((max - min) + 1) + min;
        List<Value> results = new ArrayList<>();
        results.add(new Value(result));
        return results;
    }
}
