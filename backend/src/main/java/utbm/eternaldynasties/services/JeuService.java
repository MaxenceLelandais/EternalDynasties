/**
 * Classe JeuService : Singleton du projet initialisé et géré par Spring Boot.
 */


package utbm.eternaldynasties.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utbm.eternaldynasties.jeu.Jeu;
import utbm.eternaldynasties.utils.Log;

@Service
public class JeuService {

    /**
     * Liaison automatique avec un autre service par Spring Boot.
     */
    @Autowired
    private FichierJeuService fichierJeuService;
    private Jeu jeu;

    /**
     * Fonction d'appelée lors de l'instanciation.
     */
    @PostConstruct
    public void init() {
        Log.info("JeuService", "Démarrage du jeu...");
        this.jeu = new Jeu(fichierJeuService);
    }

    public Jeu getJeu() {
        return jeu;
    }
}
