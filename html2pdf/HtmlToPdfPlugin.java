package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.api.AttachmentValue;
import com.bmc.arsys.api.StatusInfo;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;

public class HtmlToPdfPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        if (args == null || args.size() < 2) {
            List<StatusInfo> statusList = new ArrayList<>();
            StatusInfo info = new StatusInfo(9995, 0L, "FATAL");
            info.setMessageText("Expected 2 parameters: HTML string and filename.");
            statusList.add(info);
            throw new ARException(statusList);
        }

        String html = (String) args.get(0).getValue();
        String filename = (String) args.get(1).getValue();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            AttachmentValue attachment = new AttachmentValue();
            attachment.setName(filename);
            attachment.setValue(outputStream.toByteArray());

            List<Value> results = new ArrayList<>();
            results.add(new Value(attachment));
            return results;
        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            StatusInfo info = new StatusInfo(9996, 0L, "FATAL");
            info.setMessageText("PDF generation error: " + e.getMessage());
            statusList.add(info);
            throw new ARException(statusList);
        }
    }
}