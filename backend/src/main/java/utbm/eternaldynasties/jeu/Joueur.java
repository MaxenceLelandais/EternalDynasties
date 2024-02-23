package utbm.eternaldynasties.jeu;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utbm.eternaldynasties.utils.Json.convertJsonObjectToMap;

public class Joueur {
    private String nom;
    private String civilisation;
    private long tempsDeJeu;
    private long debutDeLaPartie;
    private HashMap<String, Long> ressources = new HashMap<>();
    private HashMap<String, String> caracteristiquesCollectes = new HashMap<>();
    private ArrayList<String> recherches = new ArrayList<>();

    public Joueur(JSONObject jsonObject){
        Map data = convertJsonObjectToMap(jsonObject);
        if(data.containsKey("Nom")){
            this.nom = data.get("Nom").toString();
            this.civilisation = data.get("Civilisation").toString();
            this.tempsDeJeu = (long) data.get("Temps de jeu");
            this.debutDeLaPartie = (long) data.get("Début de la partie");
            this.ressources = (HashMap<String, Long>) data.get("Ressources");
            this.caracteristiquesCollectes = (HashMap<String, String>) data.get("Caracteristiques collectes");
            this.recherches = (ArrayList<String>) data.get("Recherches");
        }
    }

    public Map<Object, Object> toMap(){
        Map<Object, Object> data = new HashMap<>();
        data.put("Nom", this.nom);
        data.put("Civilisation", this.civilisation);
        data.put("Temps de jeu", this.tempsDeJeu);
        data.put("Début de la partie", this.debutDeLaPartie);
        data.put("Ressources", this.ressources);
        data.put("Caracteristiques collectes", this.caracteristiquesCollectes);
        data.put("Recherches", this.recherches);
        return data;
    }
}
