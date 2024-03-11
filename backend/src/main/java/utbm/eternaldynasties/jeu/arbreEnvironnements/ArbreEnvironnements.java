package utbm.eternaldynasties.jeu.arbreEnvironnements;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.utils.Json;

import java.util.*;
import java.util.stream.Collectors;

public class ArbreEnvironnements {
    private Map<String, Environnement> listeEnvironnements = new HashMap<>();

    public ArbreEnvironnements(JSONObject jsonObject) {
        ArrayList<JSONObject> liste = (ArrayList<JSONObject>)jsonObject.get("Environnements");
        for (Map objet : liste) {
            this.listeEnvironnements.put((String)objet.get("nom"), new Environnement((String) objet.get("nom"),objet));
        }
    }
    public String toString() {
        Map<String, Object> liste = new HashMap<>();
        this.listeEnvironnements.values().forEach(r -> liste.put(r.getNom(), r.getJsonObjet()));
        return Json.jsonToString(liste);
    }

    public Environnement get(String nom){
        return listeEnvironnements.get(nom);
    }
}
