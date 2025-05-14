package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class HtmlToPdfPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        if (args == null || args.size() < 1) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10000, "HTML content argument saknas."));
            throw new ARException(statusList);
        }

        String html = (String) args.get(0).getValue();
        byte[] pdfBytes;

        try {
            pdfBytes = generatePdfFromHtml(html);
        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10001, "Fel vid PDF-generering: " + e.getMessage()));
            throw new ARException(statusList);
        }

        List<Value> results = new ArrayList<>();
        AttachmentValue attachment = new AttachmentValue("generated.pdf", pdfBytes);
        results.add(new Value(attachment));
        return results;
    }

    private byte[] generatePdfFromHtml(String html) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(os);
        builder.run();
        return os.toByteArray();
    }
}
