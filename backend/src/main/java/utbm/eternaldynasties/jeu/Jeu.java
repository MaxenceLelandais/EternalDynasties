package utbm.eternaldynasties.jeu;

import utbm.eternaldynasties.jeu.arbreRecherches.ArbreDeRecherches;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.services.FichierJeuService;
import utbm.eternaldynasties.utils.Json;
import utbm.eternaldynasties.utils.Log;

import java.util.List;

public class Jeu {

    private final FichierJeuService fichierJeuService;
    private ArbreDeRecherches arbreDeRecherches;
    public Jeu(FichierJeuService fichierJeuService){
        this.fichierJeuService = fichierJeuService;

        Log.info("JeuService","Jeu en cours de démarrage ...");
        this.arbreDeRecherches = new ArbreDeRecherches(fichierJeuService.arbreDeRecherches);
    }

    public ArbreDeRecherches getArbreDeRecherches() {
        return arbreDeRecherches;
    }
}
