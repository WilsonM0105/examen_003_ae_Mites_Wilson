package com.pucetec.inventory_config_service.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    // 1) Público
                    .requestMatchers("/public/**").permitAll()

                    // 2) Autenticado (solo GET)
                    .requestMatchers(HttpMethod.GET, "/api/rules/**").authenticated()

                    // 3) Solo ADMIN (crear/editar/toggle/borrar)
                    .requestMatchers(HttpMethod.POST, "/api/rules/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/rules/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/rules/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/rules/**").hasRole("ADMIN")

                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { rs ->
                rs.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }

        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val groups = jwt.getClaimAsStringList(JwtClaims.GROUPS) ?: emptyList()
            // Si en Cognito tu grupo es "admin", esto crea ROLE_ADMIN
            groups.map { SimpleGrantedAuthority("ROLE_${it.uppercase()}") }
        }
        return converter
    }
}
