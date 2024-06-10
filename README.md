# Reader

### Example Usage

```java
public abstract class Options {
    private JsonReader json_reader;

    private final File appsettings_file;
    private final Logger logger;

    public Options(String prefix) {
        var file_reader = new FileReader();
        appsettings_file = file_reader.getFileByName("appsettings.json");

        json_reader = new JsonReader(prefix, appsettings_file);
        logger = new Logger(this.getClass().getSimpleName());
    }

    public<T> JsonObject<T> getOptionData(String option_key, Class<T> object) {
        try {
            return json_reader.readJsonByKey(option_key, object);
        }
        catch (Exception e) {
            logger.setException(e);
            logger.write();
        }

        return new JsonObject<T>(option_key, false);
    }

    public<T> JsonObject<T> getOptionDataList(String option_key, Class<T> object_class) {
        try {
            return json_reader.readJsonByKey(option_key, object_class);
        }
        catch (Exception e) {
            logger.setException(e);
            logger.write();
        }

        return new JsonObject<T>(option_key, true);
    }
}
```
