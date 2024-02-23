package utbm.eternaldynasties.services;

import jakarta.annotation.PostConstruct;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import utbm.eternaldynasties.utils.Json;

@Service
public class FichierJeuService {

    private final String fichierArbreDeRecherches = "src/main/resources/donnees/arbre_de_recherches.json";
    private final String fichierJoueur = "src/main/resources/donnees/joueur.json";
    private final String fichierRessources = "src/main/resources/donnees/ressources.json";

    public JSONObject arbreDeRecherches;
    public JSONObject joueur;
    public JSONObject ressources;
    @PostConstruct
    public void init(){
        this.arbreDeRecherches  = Json.read(this.fichierArbreDeRecherches);
        this.joueur  = Json.read(this.fichierJoueur);
        this.ressources  = Json.read(this.fichierRessources);
    }
}
