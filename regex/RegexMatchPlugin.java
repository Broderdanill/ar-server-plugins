package regex;

import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.api.StatusInfo;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

public class RegexMatchPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> args) throws ARException {
        if (args == null || args.size() < 2) {
            List<StatusInfo> statusList = new ArrayList<>();
            StatusInfo info = new StatusInfo(9970, 0L, "FATAL");
            info.setMessageText("Expected 2 arguments: input string and regex pattern");
            statusList.add(info);
            throw new ARException(statusList);
        }

        String input = (String) args.get(0).getValue();
        String patternStr = (String) args.get(1).getValue();

        try {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(input);

            List<Value> results = new ArrayList<>();
            if (matcher.find()) {
                results.add(new Value(matcher.group())); // full match
            } else {
                results.add(new Value("")); // or null if preferred
            }
            return results;
        } catch (Exception e) {
            List<StatusInfo> statusList = new ArrayList<>();
            StatusInfo info = new StatusInfo(9971, 0L, "FATAL");
            info.setMessageText("Regex error: " + e.getMessage());
            statusList.add(info);
            throw new ARException(statusList);
        }
    }
}