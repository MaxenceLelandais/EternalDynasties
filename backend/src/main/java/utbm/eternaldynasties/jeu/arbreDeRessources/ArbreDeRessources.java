package utbm.eternaldynasties.jeu.arbreDeRessources;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.utils.Json;

import java.util.HashMap;
import java.util.Map;

public class ArbreDeRessources {
    private Map<String, Ressource> listeRessources = new HashMap<>();

    public ArbreDeRessources(JSONObject jsonObject) {
        for (Object key : jsonObject.keySet()) {
            String keyString = key.toString();
            this.listeRessources.put(keyString, new Ressource(keyString, (Map<String, Object>) jsonObject.get(key)));
        }
    }

    public Map<String, Ressource> getListeRessources() {
        return listeRessources;
    }

    public Ressource getRessource(String nomRessource){
        return this.listeRessources.get(nomRessource);
    }

    public String toString() {
        Map<String, Object> liste = new HashMap<>();
        this.listeRessources.values().forEach(r -> liste.put(r.getNom(), r.getJsonObjet()));
        return Json.jsonToString(liste);
    }
}