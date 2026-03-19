package com.ing.mortgage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mortgageOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mortgage API")
                        .description("API for mortgage feasibility checks and interest rates")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ING")
                                .email("support@ing.com")));
    }
}
