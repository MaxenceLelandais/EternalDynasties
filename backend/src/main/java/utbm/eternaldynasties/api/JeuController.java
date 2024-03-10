package utbm.eternaldynasties.api;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.services.JeuService;
import utbm.eternaldynasties.utils.Json;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "jeu/*", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class JeuController {

    private JeuService jeuService;

    //////////////////    Jeu      //////////////////

    @Autowired
    public JeuController(JeuService jeuService) {
        this.jeuService = jeuService;
    }

    @GetMapping(value = "listeRecherches")
    public JSONObject sendRecherche() {
        return Json.stringToJsonObject(jeuService.getJeu().getArbreDeRecherches().toString());
    }

    @GetMapping(value = "parties")
    public JSONObject sendSauvegardes() {
        return Json.objectToJsonObject(this.jeuService.getJeu().getSauvegardes());
    }

    @GetMapping(value = "arbreRecherches")
    public JSONObject sendArbreRecherches() {
        return Json.objectToJsonObject(this.jeuService.getJeu().getArbreDeRecherches().getArbrePourAffichage());
    }

    @GetMapping(value = "listeEres")
    public JSONObject sendListeEres() {
        return Json.objectToJsonObject(this.jeuService.getJeu().getArbreDeRecherches().getEres());
    }

    @GetMapping(value = "listeRessourcesJeu")
    public JSONObject sendRessources() {
        return Json.stringToJsonObject(jeuService.getJeu().getArbreDeRessources().toString());
    }

    @GetMapping(value = "listeEnvironnements")
    public JSONObject sendEnvironnements() {
        return jeuService.getJeu().getListeEnvironnements();
    }


    //////////////////    PARTIES      //////////////////


    @GetMapping(value="joueur")
    public JSONObject getPartie(@RequestParam(value = "nomJoueur") String nomJoueur) {
        return Json.objectToJsonObject(this.jeuService.getJeu().startPartie(nomJoueur).toMap());
    }
    @GetMapping(value="recherchesDisponibles")
    public JSONObject getRecherchesDisponibles(@RequestParam(value = "nomJoueur") String nomJoueur) {
        Map<String, JSONObject> map = new HashMap<>();
        this.jeuService.getJeu().getJoueur(nomJoueur).recherchesPossibles().forEach(r->map.put(r.getNom(),r.convertJSONObject()));
        return Json.objectToJsonObject(map);
    }

    @GetMapping(value="activerRecherche")
    public void getActiverRecherche(@RequestParam(value = "nomJoueur") String nomJoueur,@RequestParam(value = "recherche") String nomRecherche) {
        this.jeuService.getJeu().getJoueur(nomJoueur).activerRecherche(nomRecherche);
        this.jeuService.getJeu().getJoueur(nomJoueur).save();
    }

    @GetMapping(value="listeRessources")
    public JSONObject getListeRessources(@RequestParam(value = "nomJoueur") String nomJoueur) {
        return Json.objectToJsonObject(this.jeuService.getJeu().getJoueur(nomJoueur).getRessources());
    }

    @GetMapping(value="addRessource")
    public JSONObject getAddRessource(@RequestParam(value = "nomJoueur") String nomJoueur,@RequestParam(value = "ressource") String nomRessource) {
        return Json.objectToJsonObject(this.jeuService.getJeu().getJoueur(nomJoueur).clickAchat(nomRessource));
    }

    @GetMapping(value="tick")
    public JSONObject getAddRessource(@RequestParam(value = "nomJoueur") String nomJoueur) {
        return Json.objectToJsonObject(this.jeuService.getJeu().getJoueur(nomJoueur).tickBonus());
    }

    @GetMapping(value = "")
    public String autre() {
        return "Il n'y a rien ici.";
    }
}

