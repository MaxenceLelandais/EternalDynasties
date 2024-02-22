package utbm.eternaldynasties;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class ServletInitializer extends Application {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}