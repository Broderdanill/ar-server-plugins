package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogToFilePlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        if (args == null || args.size() < 4) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(2, 10010, "Missing arguments. Expected: log_file, message, log_level, application."));
            throw new ARException(statusList);
        }

        String logFile = (String) args.get(0).getValue();
        String message = (String) args.get(1).getValue();
        String logLevel = (String) args.get(2).getValue();
        String application = (String) args.get(3).getValue();

        try {
            String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String logLine = String.format("%s [%s] [%s] %s%n", timestamp, application, logLevel.toUpperCase(), message);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(logLine);
            }
        } catch (IOException e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(2, 10011, "I/O Error writing to file: " + e.getMessage()));
            throw new ARException(statusList);
        }

        return new ArrayList<>(); // No return values
    }
}
