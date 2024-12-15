package com.canwia.BankExchange.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;



@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "canwia",
                        email = "contact@canwia.com",
                        url = "https://canwia.com"
                ),
                description = "OpenApi documentation for Social App",
                title = "Social App - Canwia",
                version = "1.0",
                license = @License(
                        name = "License Name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of Service"
        ),
        servers =
        @Server(
                description = "Local ENV",
                url="http://localhost:8080"
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearer Auth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {


    /*
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API title").version("API version"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
    */

}
