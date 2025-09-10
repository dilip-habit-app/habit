package com.habittracker.habit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Habit monitor API")
                        .version("1.0")
                        .description("This is API documentation for Habit monitor Spring Boot application")
                        .contact(new Contact().name("Dilip Kumar Singh").email("career.dilip.kumar@gmail.com")));
    }
}
