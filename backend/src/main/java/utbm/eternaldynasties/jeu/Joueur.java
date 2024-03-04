package utbm.eternaldynasties.jeu;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreDeRessources.ArbreDeRessources;
import utbm.eternaldynasties.jeu.arbreDeRessources.Bonus;
import utbm.eternaldynasties.jeu.arbreRecherches.ArbreDeRecherches;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Joueur {

    private String nom;
    private String civilisation;
    private long tempsDeJeu;
    private long tempsDemarrage;
    private long debutDeLaPartie;
    private HashMap<String, Long> ressources = new HashMap<>();
    private HashMap<String, String> caracteristiquesCollectes = new HashMap<>();
    private ArrayList<String> recherches = new ArrayList<>();
    private ArbreDeRecherches arbreDeRecherche;
    private ArbreDeRessources arbreDeRessources;

    public Joueur(JSONObject jsonObject, ArbreDeRecherches arbreDeRecherche, ArbreDeRessources arbreDeRessources) {
        this.arbreDeRecherche = arbreDeRecherche;
        this.arbreDeRessources = arbreDeRessources;
        Map data = jsonObject;
        if (data.containsKey("Nom")) {
            this.nom = data.get("Nom").toString();
            this.civilisation = data.get("Civilisation").toString();
            this.tempsDeJeu = Long.parseLong((String)data.get("Temps de jeu"));
            this.debutDeLaPartie = Long.parseLong((String)data.get("Début de la partie"));
            this.ressources = (HashMap<String, Long>) data.get("Ressources");
            this.caracteristiquesCollectes = (HashMap<String, String>) data.get("Caracteristiques collectes");
            this.recherches = (ArrayList<String>) data.get("Recherches");
            this.arbreDeRecherche.init(this.recherches);
        }
        this.tempsDemarrage = System.currentTimeMillis();
    }

    public void init(String informations) {
        if (informations.contains(":")) {
            this.nom = informations.split(":")[0];
            this.civilisation = informations.split(":")[1];
        }else{
            this.nom = informations;
        }
        this.tempsDeJeu = 0L;
        this.debutDeLaPartie = System.currentTimeMillis();
    }

    public Map<Object, Object> toMap() {
        Map<Object, Object> data = new HashMap<>();
        data.put("Nom", this.nom);
        data.put("Civilisation", this.civilisation);
        data.put("Temps de jeu", this.tempsDeJeu+"");
        data.put("Début de la partie", this.debutDeLaPartie+"");
        data.put("Ressources", this.ressources);
        data.put("Caracteristiques collectes", this.caracteristiquesCollectes);
        data.put("Recherches", this.recherches);
        return data;
    }

    public void save(){
        this.tempsDeJeu += System.currentTimeMillis()-this.tempsDemarrage;
        Json.save("src/main/resources/sauvegardes/"+this.nom+".save", toMap());
    }

    public List<Recherche> recherchesPossibles(){
        return this.arbreDeRecherche.recherchesPossibles();
    }
    public boolean activerRecherche(String nomRecherche){
        if(this.arbreDeRecherche.activerRecherche(nomRecherche)){
            Map<String, Bonus> map = this.arbreDeRecherche.getRecherche(nomRecherche).getListeBonus();
            for(String key : map.keySet()){
                Bonus bonusRessource = this.arbreDeRessources.getRessource(key).getListeBonus().get(key);
                Bonus bonusRecherche = map.get(key);
            }
            return true;
        }
        return false;
    }
}