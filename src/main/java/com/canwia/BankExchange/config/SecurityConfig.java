package com.canwia.BankExchange.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        HttpSecurity httpSecurity = http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configure(http))
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("http://localhost:3000"));
                        config.setAllowedMethods(List.of("*"));
                        return config;
                    });
                })
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(x -> x.requestMatchers(
                        "/api/v1/auth/**",
                        "/api/v1/home",
                        "/api/v1/account/**",
                        "/api/v1/user/**",
                        "/api/v1/currency/**",
                        "/api/v1/exchange/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
                ).permitAll())
                .authorizeHttpRequests(y -> y.anyRequest().authenticated())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
