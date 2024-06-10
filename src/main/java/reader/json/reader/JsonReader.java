package main.java.reader.json.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.naming.NameNotFoundException;

import main.java.reader.file.FileReader;
import main.java.reader.json.models.JsonObject;

public class JsonReader {
    private File file;
    private String prefix;
    private Pattern regex_pattern;
    
    private final String prefix_regex_pattern;
    private final String key_regex_pattern;
    private final FileReader file_reader;

    public JsonReader(String prefix, File file) {
        file_reader = new FileReader();
        this.file = file;
        this.prefix = prefix;
        prefix_regex_pattern = "(\\\"" + this.prefix + "\\\"\s:\s\\{.*\\})|"
            + "(\\\"" + this.prefix + "\\\"\s:\s\\\"[a-zA-Z0-9]\\\")|"
            + "(\\\"" + this.prefix + "\\\"\s:\s\\[.*\\])";

        key_regex_pattern = "(\\\""+ "%s" +"\\\"\\s+:\\s+\\\"[\\w\\s]+\\\")|"
            + "(\\\"" + "%s" + "\\\"\\s:\\s+\\[.*\\])|"
            + "(\\\"" + "%s" + "\\\"\\s+:\\s+\\d.\\d+)|"
            + "(\\\"" + "%s" + "\\\"\\s:\\strue)|"
            + "(\\\"" + "%s" + "\\\"\\s:\\sfalse)";
    }

    public String readJsonAsString() throws FileNotFoundException, IOException {
        return file_reader.readFileAsString(file);
    }

    public<T> JsonObject<T> readJsonByKey(String key, Class<T> value_class) throws NameNotFoundException, FileNotFoundException, IOException {
        regex_pattern = Pattern.compile(prefix_regex_pattern, Pattern.MULTILINE);

        var json_file_string = readJsonAsString();
        json_file_string = json_file_string.substring(1, json_file_string.length() - 1);
        var regex_matcher = regex_pattern.matcher(json_file_string);

        while (regex_matcher.find()) {
            var prefix_data = clearData(prefix, regex_matcher.group());
            prefix_data = prefix_data.substring(1, prefix_data.length() - 1);

            var key_regex = Pattern.compile(String.format(key_regex_pattern, key, key, key, key, key));
            var key_matcher = key_regex.matcher(prefix_data);

            while (key_matcher.find()) {
                var json_data = clearData(key, key_matcher.group()).replaceAll("\"", "");
                var json_object = new JsonObject<T>(key, json_data.indexOf(91) != -1);
                json_object.setValue(json_data, value_class);

                return json_object;
            }

            throw new NameNotFoundException("Not Found : " + key);
        }

        throw new NameNotFoundException("Not Found : " + prefix);
    }

    private String clearData(String prefix, String regex_group) {
        return regex_group.replace("\"" + prefix + "\"\s:\s", "");
    }
}
