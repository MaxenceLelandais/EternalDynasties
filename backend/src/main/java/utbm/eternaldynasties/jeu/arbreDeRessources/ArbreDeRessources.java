package utbm.eternaldynasties.jeu.arbreDeRessources;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ArbreDeRessources implements Cloneable {
    private Map<String, Ressource> listeRessources = new HashMap<>();

    public ArbreDeRessources(JSONObject jsonObject) {
        for (Object key : jsonObject.keySet()) {
            String keyString = key.toString();
            this.listeRessources.put(keyString, new Ressource(keyString, (Map<String, Object>) jsonObject.get(key)));
        }
    }

    public Ressource getRessource(String nomRessource){
        return this.listeRessources.get(nomRessource);
    }

    @Override
    public ArbreDeRessources clone() {
        try {
            return (ArbreDeRessources) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}