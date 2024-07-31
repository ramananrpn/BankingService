package com.tutorial.bankingservice.filter.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.tutorial.bankingservice.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtAuthFilter extends BasicAuthenticationFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String BASIC_PREFIX = "Basic ";

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.security.client.id}")
    private String clientId;

    @Value("${spring.security.client.secret}")
    private String clientSecret;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(AuthenticationManager authenticationManager,
                         UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION);

        if ("/register/users".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            handleClientCredentialsAuthentication(header, response);
        } else {
            handleJwtAuthentication(header, response);
        }

        chain.doFilter(request, response);
    }

    private void handleClientCredentialsAuthentication(String header, HttpServletResponse response) throws IOException {
        if (header != null && header.startsWith(BASIC_PREFIX)) {
            String[] credentials = extractCredentials(header);
            if (validateCredentials(credentials)) {
                Authentication authentication = getClientAuthentication();
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid client credentials");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
        }
    }

    private void handleJwtAuthentication(String header, HttpServletResponse response) throws IOException {
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.substring(TOKEN_PREFIX.length());
            try {
                Optional<String> username = validateToken(token);

                if (username.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username.get());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException e) {
                log.error("Error verifying token :: ", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
        }
    }

    private Optional<String> validateToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String username = String.valueOf(jwt.getClaim("email"));
        Date expiresAt = jwt.getExpiresAt();
        if (expiresAt.before(new Date())) {
            throw new JWTVerificationException("Expired token");
        }
        return Optional.ofNullable(username);
    }

    private String[] extractCredentials(String header) {
        String base64Credentials = header.substring(BASIC_PREFIX.length()).trim();
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decoded, StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }

    private boolean validateCredentials(String[] credentials) {
        return clientId.equals(credentials[0]) && clientSecret.equals(credentials[1]);
    }

    private Authentication getClientAuthentication() {
        return new UsernamePasswordAuthenticationToken(clientId, null, Collections.emptyList());
    }
}