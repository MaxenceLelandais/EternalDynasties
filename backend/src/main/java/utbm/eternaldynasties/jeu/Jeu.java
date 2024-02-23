package utbm.eternaldynasties.jeu;

import utbm.eternaldynasties.services.FichierJeuService;
import utbm.eternaldynasties.utils.Json;
import utbm.eternaldynasties.utils.Log;

public class Jeu {

    private final FichierJeuService fichierJeuService;
    public Jeu(FichierJeuService fichierJeuService){
        this.fichierJeuService = fichierJeuService;

        Log.info("JeuService","Jeu en cours de démarrage ...");
        System.out.println(Json.jsonObjectToString(fichierJeuService.arbreDeRecherches));
    }
}
