package com.tutorial.bankingservice.filter;

import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class ClientCredentialsAuthenticationFilter extends BasicAuthenticationFilter{
    public ClientCredentialsAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    }
