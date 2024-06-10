package main.java.reader.json.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JsonObject<T extends Object> {
    private String key = "";
    private boolean is_list = false;
    private T value = null;
    private List<T> values = null;

    public JsonObject(String key, boolean is_list) {
        this.key = key;
        this.is_list = is_list;
    }

    public void setValue(String value, Class<T> value_class) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }

        if (is_list) {
            this.values = castList(value, value_class);
        }
        else {
            this.value = cast(value, value_class);
        }
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    public List<T> getValues() {
        return Collections.unmodifiableList(values);
    }

    public boolean isList() {
        return is_list;
    }

    private T cast(String value, Class<T> object_class) {
        var class_name = object_class.getTypeName();

        if (String.class.getTypeName().equals(class_name)) {
            return object_class.cast(value);
        }
        else if (Double.class.getTypeName().equals(class_name)) {
            return object_class.cast(Double.parseDouble(value));
        }
        else if (Integer.class.getTypeName().equals(class_name)) {
            return object_class.cast(Integer.parseInt(value));
        }
        else if (Boolean.class.getTypeName().equals(class_name)) {
            return object_class.cast(Boolean.parseBoolean(value));
        }

        return null;
    }

    private List<T> castList(String value, Class<T> object_class) {
        value = value.substring(1, value.length() - 1);
        value = value.replaceAll("\\s", "");

        return Arrays
            .asList(value.split(","))
            .stream()
            .map(x -> cast(x, object_class))
            .collect(Collectors.toList());
    }
}
