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
    private Map<ArrayList<Recherche>, ArrayList<Recherche>> conditions = new HashMap<>();
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

        this.description = (String)this.jsonObjet.get("Description");
        this.cout = this.jsonObjet.containsKey("Coût") ? (Map<String, Long>) this.jsonObjet.get("Coût") : null;
        this.bonus = this.jsonObjet.containsKey("Bonus") ? (Map<String, String>) this.jsonObjet.get("Bonus") : null;

        ajoutVal("Dépendance", this.dependances, listeRecherches);
        ajoutVal("Débloque", this.debloque, listeRecherches);
        ajoutVal("Inhibe", this.inhibe, listeRecherches);
        ajoutValIf("If", listeRecherches);

        this.jsonObjet.put("Etat", this.etat);
        this.jsonObjet.put("Recherche possible", this.recherchePossible);

        return this;
    }

    private void ajoutVal(String key, Map<String, Recherche> liste, Map<String, Recherche> listeRecherches) {
        if (this.jsonObjet.containsKey(key) && this.jsonObjet.get(key) != null) {
            for (String valeur : (ArrayList<String>) this.jsonObjet.get(key)) {
                liste.put(valeur, ajoutKeyValListeRecherches(listeRecherches,valeur));
            }
        }
    }


    private void ajoutValIf(String key, Map<String, Recherche> listeRecherches) {
        if (this.jsonObjet.containsKey(key) && this.jsonObjet.get(key) != null) {
            Map<String, String> map = (Map<String, String>) this.jsonObjet.get(key);
            for (String recherchesDependantes : map.keySet()) {
                ArrayList<Recherche> listeDependance = new ArrayList<>();
                ArrayList<Recherche> listeDebloque = new ArrayList<>();

                for (String rechercheDependante : recherchesDependantes.split("\\+")) {
                    listeDependance.add(ajoutKeyValListeRecherches(listeRecherches, rechercheDependante));
                }
                for (String rechercheDebloque : map.get(recherchesDependantes).split("\\+")) {
                    listeDebloque.add(ajoutKeyValListeRecherches(listeRecherches, rechercheDebloque));
                }
                this.conditions.put(listeDependance, listeDebloque);
            }
        }
    }

    private Recherche ajoutKeyValListeRecherches(Map<String, Recherche> listeRecherches, String valeur) {
        Recherche recherche = null;
        if (!valeur.isEmpty()) {
            if (listeRecherches.containsKey(valeur)) {
                recherche = listeRecherches.get(valeur);
            } else {
                recherche = new Recherche(valeur);
                listeRecherches.put(valeur, recherche);
            }
        }
        return recherche;
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

    public void activer() {
        if (actualiseEtat() && checkConditions()) {
            this.etat = true;
            this.inhibe.values().forEach(Recherche::disableRecherche);
        }
    }

    private boolean checkConditions() {
        if (this.conditions.isEmpty()) {
            return true;
        }
        for (ArrayList<Recherche> dependances : this.conditions.keySet()) {
            if (dependances.stream().allMatch(Recherche::getEtat)) {
                return true;
            }
        }
        return false;
    }

    public boolean actualiseEtat() {
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

    public String toString() {
        return Json.jsonToString(jsonObjet);
    }

    void forceActive(){
        this.etat = true;
    }

}