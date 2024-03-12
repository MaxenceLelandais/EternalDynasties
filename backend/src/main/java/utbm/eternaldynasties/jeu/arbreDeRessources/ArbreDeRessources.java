package utbm.eternaldynasties.jeu.arbreDeRessources;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.utils.Json;

import java.util.HashMap;
import java.util.Map;

public class ArbreDeRessources {
    private Map<String, Ressource> listeRessources = new HashMap<>();
    private Map<String, Map<Double, Ressource>> listeUpMax = new HashMap<>();

    public ArbreDeRessources(JSONObject jsonObject) {
        for (Object key : jsonObject.keySet()) {
            String keyString = key.toString();
            this.listeRessources.put(keyString, new Ressource(keyString, (Map<String, Object>) jsonObject.get(key)));
        }
        this.listeRessources.values().forEach(r -> {
            if (!r.getNom().contains("Max-")) {
                for (String nomBonus : r.getListeBonus().keySet()) {
                    if (nomBonus.contains("Max-") && !nomBonus.replace("Max-","").equals(r.getNom())) {
                        double quantite = r.getListeBonus().get(nomBonus).getQuantite();
                        if (this.listeUpMax.containsKey(nomBonus)) {
                            this.listeUpMax.get(nomBonus).put(quantite, r);
                        } else {
                            Map<Double, Ressource> map = new HashMap<>();
                            map.put(quantite, r);
                            this.listeUpMax.put(nomBonus, map);
                        }
                    }
                }
            }
        });
    }

    public Map<String, Ressource> getListeRessources() {
        return listeRessources;
    }

    public Ressource getRessource(String nomRessource) {
        return this.listeRessources.getOrDefault(nomRessource, null);
    }

    public Map<String, Map<Double, Ressource>> getListeUpMax() {
        return listeUpMax;
    }

    public String toString() {
        Map<String, Object> liste = new HashMap<>();
        this.listeRessources.values().forEach(r -> liste.put(r.getNom(), r.getJsonObjet()));
        return Json.jsonToString(liste);
    }
}