package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogToFilePlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        List<Value> result = new ArrayList<>();

        if (args == null || args.size() < 4) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(2, 10010, "Missing arguments. Expected: log_file, message, log_level, application."));
            throw new ARException(statusList);
        }

        String logFilePath = (String) args.get(0).getValue();
        String message = (String) args.get(1).getValue();
        String logLevel = (String) args.get(2).getValue();
        String application = (String) args.get(3).getValue();

        try {
            File logFile = new File(logFilePath);

            // Skapa mappstruktur om den inte finns
            File parentDir = logFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
                }
            }

            // Skapa filen om den inte existerar
            if (!logFile.exists()) {
                if (!logFile.createNewFile()) {
                    throw new IOException("Failed to create log file: " + logFilePath);
                }
            }

            // Skapa loggrad
            String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String logLine = String.format("%s [%s] [%s] %s%n", timestamp, application, logLevel.toUpperCase(), message);

            // Skriv till fil
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(logLine);
            }

            // Returnera status tillbaka till AR
            result.add(new Value("Log entry written to " + logFilePath));
            return result;

        } catch (IOException e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(2, 10011, "I/O Error writing to file: " + e.getMessage()));
            throw new ARException(statusList);
        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(2, 10012, "Unexpected error: " + e.getMessage()));
            throw new ARException(statusList);
        }
    }
}
