package me.waterbroodje.waterlinker.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtil {
    public static String getString(String json, String key) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.get(key).getAsString();
    }
}
