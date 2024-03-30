/**
 * Classe Ressource : Rassemble les informations des ressources.
 */

package utbm.eternaldynasties.jeu.arbreDeRessources;


import java.util.HashMap;
import java.util.Map;

/**
 * Elle est utilisée dans le jeu pour conserver les données des ressources du jeu.
 */
public class Ressource {

    private String type = "";
    private int id = -1;
    private final String nom;
    private String description;
    private final Map<String, Double> listeCout = new HashMap<>();
    private final Map<String, Bonus> listeBonus = new HashMap<>();
    private final Map<String, Double> listeBonusEstime= new HashMap<>();


    // Une ressource peut être disponible dans la partie si elle a été activé dans une recherche.
    private boolean active = false;


    public Ressource(String nom, Map<String, Object> jsonObjet) {
        this.nom = nom;
        this.update(jsonObjet);
    }

    /**
     * Actualise les données provenant du json ressource.
     */
    public Ressource update(Map<String, Object> jsonObjet) {

        if (jsonObjet.get("Type") != null) {
            this.type = jsonObjet.containsKey("Type") ? (String) jsonObjet.get("Type") : "";
        }
        if (jsonObjet.get("Id") != null) {
            this.id = jsonObjet.containsKey("Id") ? Integer.parseInt(jsonObjet.get("Id").toString()) : -1;
        }

        this.description = (String) jsonObjet.get("Description");
        Map<String, String> map = jsonObjet.containsKey("Coût") ? (Map<String, String>) jsonObjet.get("Coût") : new HashMap<String, String>();
        if (map != null) {
            for (String key : map.keySet()) {
                this.listeCout.put(key, Double.parseDouble(map.get(key)));
            }
        }
        map = jsonObjet.containsKey("Bonus") ? (Map<String, String>) jsonObjet.get("Bonus") : new HashMap<String, String>();
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

    public Map<String, Double> getListeCout() {
        return listeCout;
    }

    public Map<String, Bonus> getListeBonus() {
        return listeBonus;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, Double> getListeBonusEstime() {
        return listeBonusEstime;
    }

    public void getListeBonusEnString(HashMap<String, Double> ressourcesActuelles) {
        listeBonus.forEach((key,val)->{
            listeBonusEstime.put(key,val.estimationValeur(ressourcesActuelles));
        });
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}