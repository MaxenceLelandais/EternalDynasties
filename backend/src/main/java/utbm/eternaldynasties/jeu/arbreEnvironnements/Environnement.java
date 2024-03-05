package utbm.eternaldynasties.jeu.arbreEnvironnements;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreDeRessources.Bonus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Environnement {

    private Map<String, Object> jsonObjet;
    private String nom;
    private String logo;
    private String banniere;
    private ArrayList<String> images = new ArrayList<>();
    private String merveille;

    public Environnement(String nom, Map<String, Object> jsonObjet) {

        this.jsonObjet = jsonObjet;
        this.nom = nom;
        this.update();
    }

    public Environnement update(Map<String, Object> jsonObjet) {
        this.jsonObjet = jsonObjet;
        return update();
    }

    public Environnement update() {

        this.nom = (String) this.jsonObjet.get("nom");
        this.logo = (String) this.jsonObjet.get("logo");
        this.banniere = (String) this.jsonObjet.get("banniere");
        this.merveille = (String) this.jsonObjet.get("merveille");
        this.images = (ArrayList<String>) this.jsonObjet.get("images");

        return this;
    }

    public Map<String, Object> getJsonObjet() {
        return jsonObjet;
    }

    public String getNom() {
        return nom;
    }

    public String getLogo() {
        return logo;
    }

    public String getBanniere() {
        return banniere;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public String getMerveille() {
        return merveille;
    }
}