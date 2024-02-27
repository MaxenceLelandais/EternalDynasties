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
@RequestMapping(value = "*", method = {RequestMethod.GET, RequestMethod.POST},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class JeuController {

    private JeuService jeuService;

    @Autowired
    public JeuController(JeuService jeuService) {
        this.jeuService = jeuService;
    }

    @PostMapping(value = "listeRecherches")
    public JSONObject sendRecherche() {
        return Json.stringToJsonObject(jeuService.getJeu().getArbreDeRecherches().toString());
    }

    @PostMapping(value = "parties")
    public String sendSauvegardes() {
        return this.jeuService.getJeu().getSauvegardes();
    }

    @GetMapping(value="joueur")
    public String getPartie(@RequestParam(value = "nomJoueur") String nomJoueur) {
        return Json.jsonToString(this.jeuService.getJeu().startPartie(nomJoueur).toMap());
    }
    @GetMapping(value="recherchesDisponibles")
    public String getRecherchesDisponibles(@RequestParam(value = "nomJoueur") String nomJoueur) {
        Map<String, String> map = new HashMap<>();
        this.jeuService.getJeu().getJoueur(nomJoueur).getArbreDeRecherche().recherchesPossibles().forEach(r->map.put(r.getNom(),r.toString()));
        return Json.jsonToString(map);
    }

    @GetMapping(value="activerRecherche")
    public void getActiverRecherche(@RequestParam(value = "nomJoueur") String nomJoueur,@RequestParam(value = "recherche") String nomRecherche) {
        this.jeuService.getJeu().getJoueur(nomJoueur).getArbreDeRecherche().activerRecherche(nomRecherche);
    }

    @RequestMapping(value = "")
    public String autre() {
        return "Il n'y a rien ici.";
    }
}

