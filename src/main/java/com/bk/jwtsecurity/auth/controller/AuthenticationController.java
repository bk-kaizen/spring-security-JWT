package com.bk.jwtsecurity.auth.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bk.jwtsecurity.auth.dto.AuthenticationRequest;
import com.bk.jwtsecurity.auth.dto.AuthenticationResponse;
import com.bk.jwtsecurity.auth.dto.RegisterRequest;
import com.bk.jwtsecurity.auth.service.api.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Endpoint for user registration.
     * @param  request the registration request containing user details
     * @return         the authentication response
     */
    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody RegisterRequest request) throws Exception {
        return authenticationService.register(request);
    }

    /**
     * Endpoint for user authentication.
     * @param  request the authentication request containing user credentials
     * @return         the authentication response
     */
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Endpoint for refreshing the authentication token.
     * @param  request     the HTTP servlet request
     * @param  response    the HTTP servlet response
     * @throws IOException if an I/O error occurs while handling the request
     */
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
