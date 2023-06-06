package com.bk.jwtsecurity.auth.service.api;

import com.bk.jwtsecurity.auth.dto.AuthenticationRequest;
import com.bk.jwtsecurity.auth.dto.AuthenticationResponse;
import com.bk.jwtsecurity.auth.dto.RegisterRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
