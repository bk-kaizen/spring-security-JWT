package com.bk.jwtsecurity.auth.service.impl;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bk.jwtsecurity.auth.dto.AuthenticationRequest;
import com.bk.jwtsecurity.auth.dto.AuthenticationResponse;
import com.bk.jwtsecurity.auth.dto.RegisterRequest;
import com.bk.jwtsecurity.auth.service.api.AuthenticationService;
import com.bk.jwtsecurity.config.JwtService;
import com.bk.jwtsecurity.user.model.User;
import com.bk.jwtsecurity.user.service.api.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder().firstName(request.getFirstname()).lastName(request.getLastname())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole()).build();
        var savedUser = userService.saveUser(user);
        var jwtToken = jwtService.generateToken(savedUser.getEmail());
        var refreshToken = jwtService.generateRefreshToken(savedUser.getEmail());
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (authenticate.isAuthenticated()) {
            var jwtToken = jwtService.generateToken(request.getEmail());
            var refreshToken = jwtService.generateRefreshToken(request.getEmail());
            return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
        } else {
            throw new UsernameNotFoundException("user not found!");
        }

    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        jwtService.refreshToken(request, response);
    }
}
