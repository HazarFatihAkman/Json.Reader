# Reader

> Example Usage

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

> Example appsettings

```json
{
    "Test" : {
        "Local" : "Hazar Fatih Akman",
        "MyArray" : [
            1,
            12,
            314
        ],
        "MyDecimal" : 3.03,
        "MyBoolean" : true
    }
}
```
> Example Output


![Screenshot 2024-06-10 at 5 29 07 PM](https://github.com/HazarFatihAkman/Reader/assets/74676200/0428df38-845d-47ee-9135-90b2b81f13c2)
