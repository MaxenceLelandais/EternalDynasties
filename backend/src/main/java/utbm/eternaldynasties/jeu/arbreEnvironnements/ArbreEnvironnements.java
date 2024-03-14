/**
 * Classe ArbreEnvironnements : Elle regroupe tous les environnements du jeu et leurs gestions.
 */

package utbm.eternaldynasties.jeu.arbreEnvironnements;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe récupère, traites et conserve les données des environnements du jeu vidéo.
 */
public class ArbreEnvironnements {
    private Map<String, Environnement> listeEnvironnements = new HashMap<>();

    /**
     * Récupère le fichier json en format objet et le traite pour extraire les données 'environnements'.
     */
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
