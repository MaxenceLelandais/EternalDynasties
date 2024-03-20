/**
 * Classe Recherche : Elle regroupe toutes les informations d'une Recherche.
 */

package utbm.eternaldynasties.jeu.arbreRecherches;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreDeRessources.Bonus;
import utbm.eternaldynasties.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recherche {

    private int id = -1;
    private final String nom;
    private String description;
    private final Map<String, Double> listeCout = new HashMap<>();
    private final Map<String, Recherche> debloque = new HashMap<>();
    private final Map<String, Bonus> listeBonus = new HashMap<>();
    private final Map<String, Recherche> dependances = new HashMap<>();
    private final Map<ArrayList<Recherche>, ArrayList<Recherche>> conditions = new HashMap<>();
    private final Map<String, Recherche> inhibe = new HashMap<>();

    // Etat de la recherche.
    private Boolean etat = false;

    // Une recherche peut être disponible dans la partie si elle n'est pas désactivé par une recherche.
    private Boolean recherchePossible = true;
    private Map<String, Object> jsonObjet = new HashMap<>();
    private ArrayList<String> recherchesEre = new ArrayList<>();

    public Recherche(String nom) {
        this.nom = nom;
    }

    public Recherche(String nom, Map<String, Object> jsonObjet, Map<String, Recherche> listeRecherches) {

        this.jsonObjet = jsonObjet;
        this.nom = nom;
        this.update(listeRecherches);
    }

    /**
     * Actualise les données provenant du json recherche.
     */
    public Recherche update(Map<String, Object> jsonObjet, Map<String, Recherche> listeRecherches) {
        this.jsonObjet = jsonObjet;
        return update(listeRecherches);
    }

    /**
     * Actualise les données provenant du json recherche.
     */
    public Recherche update(Map<String, Recherche> listeRecherches) {

        this.description = (String) this.jsonObjet.get("Description");
        String valId = (String) this.jsonObjet.get("Id");
        if (valId != null) {
            this.id = Integer.parseInt(valId);
        } else {
            this.recherchesEre = (ArrayList<String>) this.jsonObjet.get("Recherches");
        }

        Map<String, String> map = this.jsonObjet.containsKey("Coût") ? (Map<String, String>) this.jsonObjet.get("Coût") : new HashMap<>();
        if (map != null) {
            for (String key : map.keySet()) {
                this.listeCout.put(key, Double.parseDouble(map.get(key)));
            }
        }
        map = this.jsonObjet.containsKey("Bonus") ? (Map<String, String>) this.jsonObjet.get("Bonus") : new HashMap<>();
        if (map != null) {
            for (String key : map.keySet()) {
                this.listeBonus.put(key, new Bonus(key, map.get(key)));
            }
        }

        ajoutVal("Dépendance", this.dependances, listeRecherches);
        ajoutVal("Débloque", this.debloque, listeRecherches);
        ajoutVal("Inhibe", this.inhibe, listeRecherches);
        ajoutValIf(listeRecherches);

        this.jsonObjet.put("Etat", this.etat);
        this.jsonObjet.put("Recherche possible", this.recherchePossible);

        return this;
    }

    private void ajoutVal(String key, Map<String, Recherche> liste, Map<String, Recherche> listeRecherches) {
        if (this.jsonObjet.containsKey(key) && this.jsonObjet.get(key) != null) {
            for (String valeur : (ArrayList<String>) this.jsonObjet.get(key)) {
                liste.put(valeur, ajoutKeyValListeRecherches(listeRecherches, valeur));
            }
        }
    }

    private void ajoutValIf(Map<String, Recherche> listeRecherches) {
        if (this.jsonObjet.containsKey("If") && this.jsonObjet.get("If") != null) {
            Map<String, String> map = (Map<String, String>) this.jsonObjet.get("If");
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

    public void activer() {

        if (actualiseEtat() && checkConditions()) {
            this.etat = true;
            this.inhibe.values().forEach(Recherche::disableRecherche);
        }
    }

    boolean checkConditions() {
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

    void forceActive() {
        this.etat = true;
        this.inhibe.values().forEach(Recherche::disableRecherche);
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Recherche> getDebloque() {
        return debloque;
    }

    public Map<String, Double> getListeCout() {
        return listeCout;
    }

    public Map<String, Bonus> getListeBonus() {
        return listeBonus;
    }

    public Map<String, Recherche> getDependances() {
        return dependances;
    }

    public boolean getEtat() {
        return etat;
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

        this.jsonObjet.put("nom",this.nom);
        return jsonObjet;
    }

    public void disableRecherche() {
        this.recherchePossible = false;
    }

    public JSONObject convertJSONObject() {
        return Json.objectToJsonObject(jsonObjet);
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getRecherchesEre() {
        return recherchesEre;
    }
}