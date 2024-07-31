package com.tutorial.bankingservice.security;

import com.tutorial.bankingservice.filter.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public SecurityConfig(UserDetailsService userDetailsService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsService = userDetailsService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> {
                    try {
                        csrf.disable()
                                .authorizeHttpRequests(authorize -> authorize
                                        .anyRequest().authenticated()
                                )
                                .addFilterBefore(jwtFilter(http), BasicAuthenticationFilter.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return http.build();
    }

    @Bean
    public JwtAuthFilter jwtFilter(HttpSecurity http) throws Exception {
        return new JwtAuthFilter(authenticationManager(http), userDetailsServiceImpl);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        return authenticationManagerBuilder.build();
    }

}