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
    private final String fichierEnvironnements = "src/main/resources/donnees/environnements.json";
    private final String fichierEres = "src/main/resources/donnees/ere.json";

    public JSONObject arbreDeRecherches;
    public JSONObject joueur;
    public JSONObject ressources;
    public JSONObject environnements;
    public JSONObject eres;
    @PostConstruct
    public void init(){
        this.arbreDeRecherches  = Json.read(this.fichierArbreDeRecherches);
        this.joueur  = Json.read(this.fichierJoueur);
        this.ressources  = Json.read(this.fichierRessources);
        this.environnements  = Json.read(this.fichierEnvironnements);
        this.eres  = Json.read(this.fichierEres);
    }
}
