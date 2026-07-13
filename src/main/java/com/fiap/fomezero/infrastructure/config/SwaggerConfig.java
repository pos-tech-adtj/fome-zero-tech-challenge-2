package com.fiap.fomezero.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fome Zero API - Sistema de Gestão de Restaurantes")
                        .description("API de gerenciamento de usuários do sistema de restaurantes." +
                                "Permite criar, atualizar, consultar e remover usuários, além de realizar autenticação de dois tipos de usuário: Dono de Restaurante e Cliente.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("FIAP Tech Challenge")
                                .email("artthurmaximo@uol.com.br")));
    }
}
