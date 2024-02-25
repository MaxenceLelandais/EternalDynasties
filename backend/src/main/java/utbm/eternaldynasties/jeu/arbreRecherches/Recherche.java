package utbm.eternaldynasties.jeu.arbreRecherches;

import utbm.eternaldynasties.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recherche {
    private String nom;
    private String description;
    private Map<String, Long> cout = new HashMap<>();
    private Map<String, Recherche> debloque = new HashMap<>();
    private Map<String, String> bonus = new HashMap<>();
    private Map<String, Recherche> dependances = new HashMap<>();
    private Map<String, Recherche> inhibe = new HashMap<>();
    private Boolean etat = false;
    private Boolean recherchePossible = true;
    private Map<String, Object> jsonObjet = new HashMap<>();

    public Recherche(String nom) {
        this.nom = nom;
    }

    public Recherche(String nom, Map<String, Object> jsonObjet, Map<String, Recherche> listeRecherches) {

        this.jsonObjet = jsonObjet;
        this.nom = nom;
        this.update(listeRecherches);
    }

    public Recherche update(Map<String, Object> jsonObjet, Map<String, Recherche> listeRecherches) {
        this.jsonObjet = jsonObjet;
        return update(listeRecherches);
    }
    public Recherche update(Map<String, Recherche> listeRecherches) {
        this.description = this.jsonObjet.get("Description").toString();
        this.cout = (Map<String, Long>) this.jsonObjet.get("Coût");
        this.bonus = (Map<String, String>) this.jsonObjet.get("Bonus");

        ajoutVal("Dépendance",this.dependances, listeRecherches);
        ajoutVal("Débloque",this.debloque, listeRecherches);
        ajoutVal("Inhibe",this.inhibe, listeRecherches);

        this.jsonObjet.put("Etat", this.etat);
        this.jsonObjet.put("Recherche possible", this.recherchePossible);

        return this;
    }

    private void ajoutVal(String key, Map<String, Recherche> liste, Map<String, Recherche> listeRecherches){
        if (this.jsonObjet.get(key) != null) {
            for (String valeur : (ArrayList<String>) this.jsonObjet.get(key)) {
                Recherche recherche;
                if (listeRecherches.containsKey(valeur)) {
                    recherche = listeRecherches.get(valeur);
                } else {
                    recherche = new Recherche(valeur);
                    listeRecherches.put(valeur, recherche);
                }
                liste.put(valeur, recherche);
            }
        }
    }


    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Long> getCout() {
        return cout;
    }

    public Map<String, Recherche> getDebloque() {
        return debloque;
    }

    public Map<String, String> getBonus() {
        return bonus;
    }

    public Map<String, Recherche> getDependances() {
        return dependances;
    }

    public boolean getEtat() {
        return etat;
    }

    public void activer(){
        if(actualiseEtat()){
            this.etat = true;
            this.inhibe.values().forEach(Recherche::disableRecherche);
        }
    }

    public boolean actualiseEtat(){
        return dependances.values().stream().allMatch(Recherche::getEtat);
    }

    public boolean getRecherchePossible() {
        return recherchePossible;
    }

    public Map<String, Recherche> getInhibe() {
        return inhibe;
    }

    public Map<String, Object> getJsonObjet() {
        return jsonObjet;
    }

    public void disableRecherche() {
        this.recherchePossible = false;
    }

    public String toString(){
        return Json.jsonMapToString(jsonObjet);
    }


}