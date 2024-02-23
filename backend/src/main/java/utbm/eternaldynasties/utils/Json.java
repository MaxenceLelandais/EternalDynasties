package utbm.eternaldynasties.utils;

import com.google.gson.GsonBuilder;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileReader;
import java.util.Map;

public class Json {

    public static JSONObject read(String src) {

        try (FileReader fichier = new FileReader(src)) {
            JSONParser parser = new JSONParser(fichier);
            Object obj = parser.parse();
            return new JSONObject((Map) obj);
        } catch (Exception e) {
            Log.error("Json", "Problème de lecture du fichier : " + src);
            e.printStackTrace();
        }
        return null;
    }

    public static Map convertJsonObjectToMap(JSONObject jsonObject) {
        return (Map) jsonObject;
    }

    public static String jsonObjectToString(JSONObject jsonObject) {

        // Affichage du contenu du JsonObject avec indentation et saut de lignes
        return (new GsonBuilder().setPrettyPrinting().create()).toJson(jsonObject);
    }
}
