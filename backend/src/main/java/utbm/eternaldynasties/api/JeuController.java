package utbm.eternaldynasties.api;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utbm.eternaldynasties.services.JeuService;
import utbm.eternaldynasties.utils.Json;

@RestController
@RequestMapping(value = "*", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class JeuController {

    private JeuService jeuService;
    @Autowired
    public JeuController(JeuService jeuService) {
        this.jeuService = jeuService;
    }

    @GetMapping(value = "recherches")
    public JSONObject getRecherche() {
        return Json.stringToJsonObject(jeuService.getJeu().getArbreDeRecherches().toString());
    }

    @GetMapping(value = "")
    public String autre() {
        return "Il n'y a rien ici.";
    }
}