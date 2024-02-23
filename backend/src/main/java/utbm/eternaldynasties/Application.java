package utbm.eternaldynasties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import utbm.eternaldynasties.api.ServeurServices;
import utbm.eternaldynasties.utils.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
