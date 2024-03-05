package utbm.eternaldynasties.jeu;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreDeRessources.ArbreDeRessources;
import utbm.eternaldynasties.jeu.arbreEnvironnements.ArbreEnvironnements;
import utbm.eternaldynasties.jeu.arbreRecherches.ArbreDeRecherches;
import utbm.eternaldynasties.services.FichierJeuService;
import utbm.eternaldynasties.utils.Json;
import utbm.eternaldynasties.utils.Log;

import java.io.File;
import java.util.*;

public class Jeu {

    private final FichierJeuService fichierJeuService;
    private ArbreDeRecherches arbreDeRecherches;
    private ArbreDeRessources arbreDeRessources;
    private ArbreEnvironnements arbreEnvironnements;
    private Map<String, Joueur> listeJoueur = new HashMap<>();

    public Jeu(FichierJeuService fichierJeuService) {
        this.fichierJeuService = fichierJeuService;

        Log.info("JeuService", "Jeu en cours de démarrage ...");
        this.arbreDeRecherches = new ArbreDeRecherches(fichierJeuService.arbreDeRecherches);
        this.arbreDeRessources = new ArbreDeRessources(fichierJeuService.ressources);
        this.arbreEnvironnements = new ArbreEnvironnements(fichierJeuService.environnements);

        for (String nomJoueur : getSauvegardes().values()) {
            if (!nomJoueur.isEmpty()) {
                this.listeJoueur.put(nomJoueur, chargerPartie(nomJoueur));
            }
        }
    }

    public ArbreDeRecherches getArbreDeRecherches() {
        return arbreDeRecherches;
    }

    public Joueur startPartie(String nomPartie) {
        if (getSauvegardes().values().contains(nomPartie)) {
            return chargerPartie(nomPartie);
        } else {
            return nouvellePartie(nomPartie);
        }
    }

    public Joueur chargerPartie(String nomPartie) {
        Joueur joueur = null;
        if (this.listeJoueur.containsKey(nomPartie)) {
            joueur = this.listeJoueur.get(nomPartie);
        } else {
            JSONObject jsonObject = Json.read("src/main/resources/sauvegardes/" + nomPartie + ".save");
            if (jsonObject != null) {
                joueur = new Joueur(jsonObject, new ArbreDeRecherches(fichierJeuService.arbreDeRecherches), new ArbreDeRessources(fichierJeuService.ressources));
                joueur.save();
                this.listeJoueur.put(nomPartie, joueur);
            }
        }
        return joueur;
    }

    public Joueur nouvellePartie(String nomPartie) {
        Joueur joueur;
        if (this.listeJoueur.containsKey(nomPartie)) {
            joueur = this.listeJoueur.get(nomPartie);
        } else {
            joueur = new Joueur(Json.read("src/main/resources/donnees/joueur.json"), new ArbreDeRecherches(fichierJeuService.arbreDeRecherches), new ArbreDeRessources(fichierJeuService.ressources));
            joueur.init(nomPartie);
            joueur.save();
            this.listeJoueur.put(nomPartie, joueur);
        }
        return joueur;
    }

    public ArbreDeRessources getArbreDeRessources() {
        return arbreDeRessources;
    }

    public ArbreEnvironnements getArbreEnvironnements() {
        return arbreEnvironnements;
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
}
