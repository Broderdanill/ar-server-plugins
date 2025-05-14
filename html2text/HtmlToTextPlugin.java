package com.example;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.ArrayList;

public class HtmlToTextPlugin extends ARFilterAPIPlugin {
    
    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        if (args == null || args.size() < 1) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10010, "HTML content argument missing."));
            throw new ARException(statusList);
        }

        String html = (String) args.get(0).getValue();
        String cleanText;

        try {
            cleanText = convertHtmlToText(html);
        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            statusList.add(new StatusInfo(3, 10011, "Failed to convert HTML to text: " + e.getMessage()));
            throw new ARException(statusList);
        }

        List<Value> results = new ArrayList<>();
        results.add(new Value(cleanText));
        return results;
    }

    private String convertHtmlToText(String html) {
        Document doc = Jsoup.parse(html);
        doc.select("script, style").remove();  // Ta bort irrelevanta delar
        String text = doc.text();

        // Ta bort överflödiga radbrytningar/whitespace
        text = text.replaceAll("[ \\t\\x0B\\f]+", " ");        // Multipla blanksteg → en
        text = text.replaceAll("(\\r?\\n){2,}", "\n");         // Fler än en radbrytning → en
        text = text.trim();

        return text;
    }
}
