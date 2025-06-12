package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogToFilePlugin extends ARFilterAPIPlugin {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final int MAX_BACKUP_FILES = 5;

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        List<Value> result = new ArrayList<>();

        if (args == null || args.size() < 4) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(2, 10010, "Missing arguments. Expected: log_file, message, log_level, application."));
            throw new ARException(statusList);
        }

        try {
            String logFilePath = (String) args.get(0).getValue();

            // Re-encode all string inputs to ensure UTF-8 correctness
            String rawMessage = (String) args.get(1).getValue();
            String message = new String(rawMessage.getBytes("ISO-8859-1"), StandardCharsets.UTF_8);

            String rawLogLevel = (String) args.get(2).getValue();
            String logLevel = new String(rawLogLevel.getBytes("ISO-8859-1"), StandardCharsets.UTF_8);

            String rawApplication = (String) args.get(3).getValue();
            String application = new String(rawApplication.getBytes("ISO-8859-1"), StandardCharsets.UTF_8);

            File logFile = new File(logFilePath);
            File parentDir = logFile.getParentFile();

            // Skapa mappstruktur om den inte finns
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

            // Kontrollera om rotering behövs
            if (logFile.length() > MAX_FILE_SIZE) {
                rotateLogs(logFile);
            }

            // Skapa loggrad
            String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String logLine = String.format("%s [%s] [%s] %s%n", timestamp, application, logLevel.toUpperCase(), message);

            // Skriv till loggfilen med UTF-8
            try (BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8))) {
                writer.write(logLine);
            }

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

    private void rotateLogs(File logFile) {
        String baseName = logFile.getAbsolutePath();

        // Ta bort äldsta om den finns
        File oldest = new File(baseName + "." + MAX_BACKUP_FILES);
        if (oldest.exists()) {
            oldest.delete();
        }

        // Flytta övriga uppåt
        for (int i = MAX_BACKUP_FILES - 1; i >= 1; i--) {
            File src = new File(baseName + "." + i);
            File dst = new File(baseName + "." + (i + 1));
            if (src.exists()) {
                src.renameTo(dst);
            }
        }

        // Flytta huvudfilen till .1
        File firstBackup = new File(baseName + ".1");
        logFile.renameTo(firstBackup);
    }
}