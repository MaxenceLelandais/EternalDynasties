package utbm.eternaldynasties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public abstract class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    protected abstract SpringApplicationBuilder configure(SpringApplicationBuilder application);
}
