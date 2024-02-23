package utbm.eternaldynasties.api;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utbm.eternaldynasties.services.FichierJeuService;
import utbm.eternaldynasties.utils.Log;

@RestController
@RequestMapping(value = "api/*", method = RequestMethod.GET,
        produces = MediaType.TEXT_PLAIN_VALUE)
public class ServeurController {

    private final FichierJeuService fichierJeuService;

    @Autowired
    public ServeurController(FichierJeuService fichierJeuService) {
        this.fichierJeuService = fichierJeuService;
    }

    @GetMapping(value = "etat")
    public String getEtat() {
        Log.info("ServeurServices", "Démarrage des services OK.");
        return "Réponse du serveur: OK";
    }

    @GetMapping(value = "arbreDeRecherche")
    public String getArbreDeRecherches() {
        JSONObject json = fichierJeuService.arbreDeRecherches;
        Log.info("ServeurServices", "Récupération du fichier d'arbre de recherches.");
        return json.toJSONString();
    }

    @GetMapping(value = "joueur")
    public String getJoueur() {
        JSONObject json = fichierJeuService.joueur;
        Log.info("ServeurServices", "Récupération du fichier joueur.");
        return json.toJSONString();
    }

    @GetMapping(value = "ressources")
    public String getRessource() {
        JSONObject json = fichierJeuService.ressources;
        Log.info("ServeurServices", "Récupération du fichier ressources.");
        return json.toJSONString();
    }

    @GetMapping(value = "")
    public String autre() {
        return "Il n'y a rien ici.";
    }
}