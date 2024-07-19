package com.example.crud.config;

import com.example.crud.Jwt.JwtAuthenticationFilter;
import com.example.crud.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/products/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/products/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/products/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/products/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers("/brands/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/brands/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/brands/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/brands/**").hasAuthority(Role.ADMIN.name())
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
