package utbm.eternaldynasties.services;

import jakarta.annotation.PostConstruct;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utbm.eternaldynasties.jeu.Jeu;
import utbm.eternaldynasties.utils.Json;
import utbm.eternaldynasties.utils.Log;

@Service
public class JeuService {

    @Autowired
    private FichierJeuService fichierJeuService;
    private Jeu jeu;

    @PostConstruct
    public void init() {
        Log.info("JeuService", "Démarrage du jeu...");
        this.jeu = new Jeu(fichierJeuService);
    }

    public Jeu getJeu() {
        return jeu;
    }
}
