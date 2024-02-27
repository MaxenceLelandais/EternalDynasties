package utbm.eternaldynasties.jeu;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreRecherches.ArbreDeRecherches;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.services.FichierJeuService;
import utbm.eternaldynasties.utils.Json;
import utbm.eternaldynasties.utils.Log;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jeu {

    private final FichierJeuService fichierJeuService;
    private ArbreDeRecherches arbreDeRecherches;
    private Map<String, Joueur> listeJoueur = new HashMap<>();

    public Jeu(FichierJeuService fichierJeuService) {
        this.fichierJeuService = fichierJeuService;

        Log.info("JeuService", "Jeu en cours de démarrage ...");
        this.arbreDeRecherches = new ArbreDeRecherches(fichierJeuService.arbreDeRecherches);
        for(String nomJoueur : getSauvegardes().split("\n")){
            this.listeJoueur.put(nomJoueur,chargerPartie(nomJoueur));
        }
    }

    public ArbreDeRecherches getArbreDeRecherches() {
        return arbreDeRecherches;
    }

    public Joueur startPartie(String nomPartie) {
        String listeSauvegardes = getSauvegardes();
        if (listeSauvegardes.contains(nomPartie)) {
            return chargerPartie(nomPartie);
        } else {
            return nouvellePartie(nomPartie);
        }
    }

    public Joueur chargerPartie(String nomPartie) {
        Joueur joueur;
        if (this.listeJoueur.containsKey(nomPartie)) {
            joueur = this.listeJoueur.get(nomPartie);
        } else {
            joueur = new Joueur(Json.read("src/main/resources/sauvegardes/" + nomPartie + ".save"), this.arbreDeRecherches.clone());
            joueur.save();
            this.listeJoueur.put(nomPartie, joueur);
        }
        return joueur;
    }

    public Joueur nouvellePartie(String nomPartie) {
        Joueur joueur;
        if (this.listeJoueur.containsKey(nomPartie)) {
            joueur = this.listeJoueur.get(nomPartie);
        } else {
            joueur = new Joueur(Json.read("src/main/resources/donnees/joueur.json"), this.arbreDeRecherches.clone());
            joueur.init(nomPartie);
            joueur.save();
            this.listeJoueur.put(nomPartie, joueur);
        }
        return joueur;
    }

    public Joueur getJoueur(String nom){
        return this.listeJoueur.get(nom);
    }

    public String getSauvegardes() {
        File[] files = (new File("src/main/resources/sauvegardes")).listFiles();
        StringBuilder data = new StringBuilder();
        if (files != null) {
            for (File file : files) {
                data.append(file.getName().contains(".save") ? file.getName().replace(".save", "\n") : "");
            }
        }
        return data.toString();
    }
}
