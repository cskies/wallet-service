package com.recargapay.wallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI walletServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wallet Service API")
                        .description("API for managing digital wallets and transactions")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RecargaPay")
                                .url("https://recargapay.com.br")
                                .email("api@recargapay.com.br")));
    }
}