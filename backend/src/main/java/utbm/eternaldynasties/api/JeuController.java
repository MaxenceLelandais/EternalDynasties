/**
 * Classe JeuController : Elle fait partie de l'api. Elle permet de réceptionner les requêtes, de les traîter et de renvoyer des informations.
 * Elle traîte toutes les requêtes liées au jeu.
 */

package utbm.eternaldynasties.api;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import utbm.eternaldynasties.services.JeuService;
import utbm.eternaldynasties.utils.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * C'est un contrôlleur Rest.
 * Cet observeur réceptionne les requêtes GET, avec comme entête de requête "jeu/*".
 * Il renvoie les données sous forme de JSON.
 */
@RestController
@RequestMapping(value = "jeu/*", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class JeuController {

    private final JeuService jeuService;

    /**
     * Spring Boot s'occupe de lier les instances pour qu'elles soient utilisables n'importe où.
     * Dans notre cas, le service gérant le jeu est récupéré pour être utilisé dans les traitements des requêtes.
     */
    @Autowired
    public JeuController(JeuService jeuService) {
        this.jeuService = jeuService;
    }

    //////////////////    Jeu : regroupe les requêtes pour obtenir les informations du jeu lui même.      //////////////////

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

    @GetMapping(value = "listeRessourcesJeu")
    public JSONObject sendRessources() {
        return Json.stringToJsonObject(jeuService.getJeu().getArbreDeRessources().toString());
    }

    @GetMapping(value = "listeEnvironnements")
    public JSONObject sendEnvironnements() {
        return Json.stringToJsonObject(jeuService.getJeu().getListeEnvironnements());
    }

    @GetMapping(value = "environnement")
    public JSONObject sendEnvironnementsByName(@RequestParam(value = "nom") String environnement) {
        return Json.objectToJsonObject(jeuService.getJeu().getArbreEnvironnements().get(environnement).getJsonObjet());
    }

    @GetMapping(value = "listeEres")
    public JSONObject sendEresByName() {
        return jeuService.getJeu().eres;
    }

    @GetMapping(value = "recupererPartie")
    public JSONObject getPartie(@RequestParam(value = "nomPartie") String nomPartie) {
        return Json.read("src/main/resources/sauvegardes/" + nomPartie + ".save");
    }

    @GetMapping(value = "chargerPartie")
    public JSONObject loadPartie(@RequestParam(value = "nom") String nom, @RequestBody String partie) {
        Json.save("src/main/resources/sauvegardes/" + nom + ".save", Json.stringToJsonObject(partie));
        return Json.objectToJsonObject(this.jeuService.getJeu().startPartie(nom.split("-")[0], nom.split("-")[1]).toMap());
    }


    //////////////////    PARTIES : regroupe les requêtes pour obtenir les informations d'une partie     //////////////////


    @GetMapping(value="joueur")
    public JSONObject getPartie(@RequestParam(value = "civilisation") String civilisation, @RequestParam(value = "environnement") String environnement) {
        return Json.objectToJsonObject(this.jeuService.getJeu().startPartie(civilisation, environnement).toMap());
    }

    @GetMapping(value = "listeRecherchesJoueur")
    public JSONObject sendRecherchesJoueur(@RequestParam(value = "nomJoueur") String nomJoueur) {
        return Json.stringToJsonObject(jeuService.getJeu().getJoueur(nomJoueur).getArbreDeRecherches().toString());
    }
    @GetMapping(value="recherchesDisponibles")
    public JSONObject getRecherchesDisponibles(@RequestParam(value = "nomJoueur") String nomJoueur) {
        Map<String, JSONObject> map = new HashMap<>();
        this.jeuService.getJeu().getJoueur(nomJoueur).recherchesPossibles().forEach(r->map.put(r.getNom(),r.convertJSONObject()));
        return Json.objectToJsonObject(map);
    }

    @GetMapping(value="activerRecherche")
    public String getActiverRecherche(@RequestParam(value = "nomJoueur") String nomJoueur,@RequestParam(value = "recherche") String nomRecherche) {
        return this.jeuService.getJeu().getJoueur(nomJoueur).activerRecherche(nomRecherche);
    }

    @GetMapping(value="listeRessources")
    public JSONObject getListeRessources(@RequestParam(value = "nomJoueur") String nomJoueur) {
        return Json.objectToJsonObject(this.jeuService.getJeu().getJoueur(nomJoueur).getRessourcesSimplifie());
    }

    @GetMapping(value = "ereActuelle")
    public JSONObject sendActualEre(@RequestParam(value = "nomJoueur") String nomJoueur) {
        Map<String,String> map = new HashMap<>();
        map.put("nom", this.jeuService.getJeu().getJoueur(nomJoueur).getEreActuelle());
        return Json.objectToJsonObject(map);
    }

    @GetMapping(value="arbreRessources")
    public JSONObject getArbreRessources(@RequestParam(value = "nomJoueur") String nomJoueur) {
        return Json.objectToJsonObject(this.jeuService.getJeu().getJoueur(nomJoueur).getArbreRessources());
    }

    @GetMapping(value="addRessource")
    public JSONObject getAddRessource(@RequestParam(value = "nomJoueur") String nomJoueur,@RequestParam(value = "ressource") String nomRessource,@RequestParam(value = "nombre") int nombre) {
        return Json.objectToJsonObject(this.jeuService.getJeu().getJoueur(nomJoueur).clickAchat(nomRessource, nombre));
    }

    @GetMapping(value="achatPourcentageMerveille")
    public JSONObject getAddRessource(@RequestParam(value = "nomJoueur") String nomJoueur,@RequestParam(value = "nombre") int nombre) {
        return Json.objectToJsonObject(this.jeuService.getJeu().getJoueur(nomJoueur).clickAchatMerveille(nombre));
    }

    @GetMapping(value="checkRecherche")
    public boolean getSiRechercheRecherchable(@RequestParam(value = "nomJoueur") String nomJoueur,@RequestParam(value = "recherche") String nomRecherche) {
        return this.jeuService.getJeu().getJoueur(nomJoueur).checkRecherche(nomRecherche);
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

