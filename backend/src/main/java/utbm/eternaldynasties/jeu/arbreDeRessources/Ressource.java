package utbm.eternaldynasties.jeu.arbreDeRessources;


import java.util.HashMap;
import java.util.Map;

public class Ressource {

    private String nom;
    private String description;
    private Map<String, Long> listeCout = new HashMap<>();
    private Map<String, Bonus> listeBonus = new HashMap<>();
    private final Map<String, Object> jsonObjet;


    public Ressource(String nom, Map<String, Object> jsonObjet) {

        this.jsonObjet = jsonObjet;
        this.nom = nom;
        this.update();
    }

    public Ressource update() {

        this.description = (String) this.jsonObjet.get("Description");
        Map<String, String> map = this.jsonObjet.containsKey("Coût") ? (Map<String, String>) this.jsonObjet.get("Coût") : new HashMap<String, String>();
        if (map != null) {
            for (String key : map.keySet()) {
                this.listeCout.put(key, Long.parseLong(map.get(key)));
            }
        }
        map = this.jsonObjet.containsKey("Bonus") ? (Map<String, String>) this.jsonObjet.get("Bonus") : new HashMap<String, String>();
        if (map != null) {
            for (String key : map.keySet()) {
                this.listeBonus.put(key, new Bonus(key, map.get(key)));
            }
        }
        return this;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Long> getListeCout() {
        return listeCout;
    }

    public Map<String, Bonus> getListeBonus() {
        return listeBonus;
    }

    public Map<String, Object> getJsonObjet() {
        return jsonObjet;
    }
}