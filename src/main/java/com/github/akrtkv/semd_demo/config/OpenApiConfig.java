package com.github.akrtkv.semd_demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openApi() {
        var contact = new Contact();
        return new OpenAPI()
                .info(
                        new Info().title("semd-demo")
                                .description("Формирование СЭМД xml.")
                                .contact(contact)
                                .version("0.0.1")
                );
    }
}
