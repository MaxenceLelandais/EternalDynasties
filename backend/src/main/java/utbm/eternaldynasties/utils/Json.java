package utbm.eternaldynasties.utils;

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
}
