package utbm.eternaldynasties.api;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import utbm.eternaldynasties.services.FichierJeuService;
import utbm.eternaldynasties.utils.Json;
import utbm.eternaldynasties.utils.Log;

import java.io.File;
import java.util.Arrays;

@RestController
@RequestMapping(value = "serveur/*", method = RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
public class ServeurController {

    private final FichierJeuService fichierJeuService;

    @Autowired
    public ServeurController(FichierJeuService fichierJeuService) {
        this.fichierJeuService = fichierJeuService;
    }

    @GetMapping(value = "etat")
    public String sendEtat() {
        Log.info("ServeurServices", "Démarrage des services OK.");
        return "Réponse du serveur: OK";
    }

    @GetMapping(value = "arbreDeRecherche")
    public String sendArbreDeRecherches() {
        JSONObject json = fichierJeuService.arbreDeRecherches;
        Log.info("ServeurServices", "Récupération du fichier type arbre de recherches.");
        return Json.jsonToString(json);
    }

    @GetMapping(value = "joueur")
    public String sendJoueur() {
        JSONObject json = fichierJeuService.joueur;
        Log.info("ServeurServices", "Récupération du fichier type joueur.");
        return Json.jsonToString(json);
    }

    @GetMapping(value = "ressources")
    public String sendRessource() {
        JSONObject json = fichierJeuService.ressources;
        Log.info("ServeurServices", "Récupération du fichier type ressources.");
        return Json.jsonToString(json);
    }

    @GetMapping(value = "")
    public String autre() {
        return "Il n'y a rien ici.";
    }
}