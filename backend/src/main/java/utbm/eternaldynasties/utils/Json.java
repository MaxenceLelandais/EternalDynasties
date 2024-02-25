package utbm.eternaldynasties.utils;

import com.google.gson.GsonBuilder;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileReader;
import java.util.Map;

public class Json {

    public static JSONObject read(String src) {

        try (FileReader fichier = new FileReader(src)) {
            return new JSONObject((Map) new JSONParser(fichier).parse());
        } catch (Exception e) {
            Log.error("Json", "Problème de lecture du fichier : " + src);
            e.printStackTrace();
        }
        return null;
    }

    public static Map convertJsonObjectToMap(JSONObject jsonObject) {
        return (Map) jsonObject;
    }

    public static String jsonMapToString(Map<String, Object> jsonObject) {
        return (new GsonBuilder().setPrettyPrinting().create()).toJson(jsonObject);
    }
    public static String jsonObjectToString(JSONObject jsonObject) {
        return (new GsonBuilder().setPrettyPrinting().create()).toJson(jsonObject);
    }

    public static JSONObject stringToJsonObject(String text){
        try {
            return new JSONObject((Map) new JSONParser(text).parse());
        } catch (ParseException e) {
            Log.error("Json", "Problème stringToJsonObject : " + text);
            return null;
        }
    }
}
