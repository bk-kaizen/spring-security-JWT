package com.bk.jwtsecurity.auth.service.api;

import java.io.IOException;

import com.bk.jwtsecurity.auth.dto.AuthenticationRequest;
import com.bk.jwtsecurity.auth.dto.AuthenticationResponse;
import com.bk.jwtsecurity.auth.dto.RegisterRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Service interface for authentication-related operations.
 */
public interface AuthenticationService {

    /**
     * Registers a new user.
     * @param  request the registration request containing user details
     * @return         the authentication response
     */
    AuthenticationResponse register(RegisterRequest request) throws Exception;

    /**
     * Authenticates a user.
     * @param  request the authentication request containing user credentials
     * @return         the authentication response
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Refreshes the authentication token.
     * @param  request     the HTTP servlet request
     * @param  response    the HTTP servlet response
     * @throws IOException if an I/O error occurs while handling the request
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
