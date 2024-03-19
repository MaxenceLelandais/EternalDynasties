/**
 * Classe Jeu : cette classe rassemble tous les fichiers json du jeu, la gestion des parties et des sauvegardes.
 */


package utbm.eternaldynasties.jeu;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreDeRessources.ArbreDeRessources;
import utbm.eternaldynasties.jeu.arbreEnvironnements.ArbreEnvironnements;
import utbm.eternaldynasties.jeu.arbreRecherches.ArbreDeRecherches;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.services.FichierJeuService;
import utbm.eternaldynasties.utils.Json;
import utbm.eternaldynasties.utils.Log;

import java.io.File;
import java.util.*;

/**
 * Cette classe rassemble les fonctions de bases du jeu.
 */
public class Jeu {

    private final FichierJeuService fichierJeuService;
    private final ArbreDeRecherches arbreDeRecherches;
    private final ArbreDeRessources arbreDeRessources;
    private final ArbreEnvironnements arbreEnvironnements;
    private final Map<String, Joueur> listeJoueur = new HashMap<>();

    public JSONObject eres;

    /**
     * Récupère les instances de chaques fichiers du jeu (fichiers json).
     * Il initialise les arbres et les sauvegardes.
     * @param fichierJeuService service où on récupère les instances des fichiers json.
     */
    public Jeu(FichierJeuService fichierJeuService) {
        this.fichierJeuService = fichierJeuService;

        Log.info("JeuService", "Jeu en cours de démarrage ...");
        this.arbreDeRecherches = new ArbreDeRecherches(fichierJeuService.arbreDeRecherches);
        this.arbreDeRecherches.setEresObj(this.fichierJeuService.eres);
        this.arbreDeRessources = new ArbreDeRessources(fichierJeuService.ressources);
        this.arbreEnvironnements = new ArbreEnvironnements(fichierJeuService.environnements);
        this.eres = fichierJeuService.eres;

        for (String nomJoueur : getSauvegardes().values()) {
            if (!nomJoueur.isEmpty()) {
                String civilisation = nomJoueur.split("-")[0];
                String environnement = nomJoueur.split("-")[1];

                this.listeJoueur.put(nomJoueur, chargerPartie(civilisation,environnement));
            }
        }
    }


    public FichierJeuService getFichierJeuService() {
        return fichierJeuService;
    }

    public ArbreDeRecherches getArbreDeRecherches() {
        return arbreDeRecherches;
    }

    /**
     * S'occupe de démarrer une partie.
     * Il charge une partie si elle existe.
     * Sinon, elle en créait une.
     * @param civilisation Nom de la partie
     * @param environnement Nom de l'environnement choisi pour la partie
     * @return la partie nouvellement chargée.
     */
    public Joueur startPartie(String civilisation, String environnement) {
        if (getSauvegardes().containsValue(civilisation)) {
            return chargerPartie(civilisation,environnement);
        } else {
            return nouvellePartie(civilisation,environnement);
        }
    }

    /**
     * Charge une partie existante.
     * @param civilisation Nom de la partie
     * @param environnement Nom de l'environnement choisi pour la partie
     * @return la partie nouvellement chargée.
     */
    public Joueur chargerPartie(String civilisation, String environnement) {
        Joueur joueur = null;
        if (this.listeJoueur.containsKey(civilisation+"-"+environnement)) {
            joueur = this.listeJoueur.get(civilisation+"-"+environnement);
        } else {
            JSONObject jsonObject = Json.read("src/main/resources/sauvegardes/" + civilisation+"-"+environnement + ".save");
            if (jsonObject != null) {
                joueur = new Joueur(jsonObject, new ArbreDeRecherches(fichierJeuService.arbreDeRecherches), new ArbreDeRessources(fichierJeuService.ressources), this.arbreEnvironnements.get(environnement));
                joueur.save();
                this.listeJoueur.put(civilisation+"-"+environnement, joueur);
            }
        }
        return joueur;
    }

    /**
     * Créait une nouvelle partie.
     * @param civilisation Nom de la partie
     * @param environnement Nom de l'environnement choisi pour la partie
     * @return la partie nouvellement chargée.
     */
    public Joueur nouvellePartie(String civilisation, String environnement) {
        Joueur joueur;
        if (this.listeJoueur.containsKey(civilisation+"-"+environnement)) {
            joueur = this.listeJoueur.get(civilisation+"-"+environnement);
        } else {
            joueur = new Joueur(Json.read("src/main/resources/donnees/joueur.json"), new ArbreDeRecherches(fichierJeuService.arbreDeRecherches), new ArbreDeRessources(fichierJeuService.ressources), this.arbreEnvironnements.get(environnement));
            joueur.init(civilisation, this.arbreEnvironnements.get(environnement));
            joueur.save();
            this.listeJoueur.put(civilisation+"-"+environnement, joueur);
        }
        return joueur;
    }

    public ArbreDeRessources getArbreDeRessources() {
        return arbreDeRessources;
    }

    public ArbreEnvironnements getArbreEnvironnements() {
        return arbreEnvironnements;
    }

    public ArbreDeRecherches getArbreDeRecherches() {
        return arbreDeRecherches;
    }

    public Joueur getJoueur(String nom) {
        return this.listeJoueur.get(nom);
    }

    public Map<String, String> getSauvegardes() {
        File[] files = (new File("src/main/resources/sauvegardes")).listFiles();
        Map<String, String> data = new HashMap<>();
        if (files != null) {
            for (File file : files) {
                data.put("Partie",file.getName().contains(".save") ? file.getName().replace(".save", "") : "");
            }
        }
        return data;
    }

    public String getListeEnvironnements() {
        return arbreEnvironnements.toString();
    }
}
