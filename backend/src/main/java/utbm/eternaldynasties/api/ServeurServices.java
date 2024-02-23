package utbm.eternaldynasties.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/*", method = RequestMethod.GET,
        produces = MediaType.TEXT_PLAIN_VALUE)
public class ServeurServices {

    private static final Logger logger = LoggerFactory.getLogger(ServeurServices.class);

    @GetMapping(value = "etat")
    public String pong() {
        logger.info("Démarrage des services OK .....");
        return "Réponse du serveur: OK";
    }

}