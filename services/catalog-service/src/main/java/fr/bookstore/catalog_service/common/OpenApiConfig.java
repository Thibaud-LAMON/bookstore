package fr.bookstore.catalog_service.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI catalogOpenApi(){
        return new OpenAPI().info(new Info()
                .title("Librairie - Service Catalogue")
                .description("Service responsable de la gestion du catalogue.")
                .version("v1"));
    }
}
