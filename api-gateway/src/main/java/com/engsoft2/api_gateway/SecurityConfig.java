package com.engsoft2.api_gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            // Desabilita o CSRF (Cross-Site Request Forgery), comum em APIs
            .csrf(csrf -> csrf.disable())
            
            // Define que o gateway é um Servidor de Recursos OAuth2 (Resource Server)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(withDefaults()) // Configura para validar tokens JWT
            )
            
            // Define as regras de autorização
            .authorizeExchange(exchanges -> exchanges
                // Qualquer requisição (exchange) deve estar autenticada
                .anyExchange().authenticated() 
            );

        return http.build();
    }
}