/**
 * Classe FichierJeuService : Singleton du projet initialisé et géré par Spring Boot.
 */


package utbm.eternaldynasties.services;

import jakarta.annotation.PostConstruct;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import utbm.eternaldynasties.utils.Json;

import java.io.InputStream;

@Service
public class FichierJeuService {

//    private final String fichierArbreDeRecherches = "src/main/resources/donnees/arbre_de_recherches.json";
//    private final String fichierJoueur = "src/main/resources/donnees/joueur.json";
//    private final String fichierRessources = "src/main/resources/donnees/ressources.json";
//    private final String fichierEnvironnements = "src/main/resources/donnees/environnements.json";
//    private final String fichierEre = "src/main/resources/donnees/ere.json";

    private final InputStream fichierArbreDeRecherches = getClass().getResourceAsStream("/donnees/arbre_de_recherches.json");
    private final InputStream fichierJoueur = getClass().getResourceAsStream("/donnees/joueur.json");
    private final InputStream fichierRessources = getClass().getResourceAsStream("/donnees/ressources.json");
    private final InputStream fichierEnvironnements = getClass().getResourceAsStream("/donnees/environnements.json");
    private final InputStream fichierEre = getClass().getResourceAsStream("/donnees/ere.json");

    public JSONObject arbreDeRecherches;
    public JSONObject joueur;
    public JSONObject ressources;
    public JSONObject environnements;
    public JSONObject eres;

    /**
     * Fonction d'appelée lors de l'instanciation.
     */
    @PostConstruct
    public void init(){
        this.arbreDeRecherches  = Json.read(this.fichierArbreDeRecherches);
        this.joueur  = Json.read(this.fichierJoueur);
        this.ressources  = Json.read(this.fichierRessources);
        this.environnements  = Json.read(this.fichierEnvironnements);
        this.eres = Json.read(this.fichierEre);
    }
}
