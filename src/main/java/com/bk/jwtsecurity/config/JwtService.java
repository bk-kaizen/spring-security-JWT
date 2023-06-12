package com.bk.jwtsecurity.config;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bk.jwtsecurity.auth.dto.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Extracts the username from a JWT token.
     * @param  jwtToken the JWT token
     * @return          the username extracted from the token
     */
    public String extractUserNameFromJwt(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    /**
     * Extracts the claims from a JWT token.
     * @param  jwtToken the JWT token
     * @return          the claims extracted from the token
     */
    public Claims extractClaims(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(jwtToken).getBody();
    }

    /**
     * Extracts a specific claim from a JWT token.
     * @param  token          the JWT token
     * @param  claimsResolver the function to resolve the desired claim
     * @param  <T>            the type of the claim value
     * @return                the resolved claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates an access token for a given username.
     * @param  userName the username
     * @return          the generated access token
     */
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, jwtExpiration);
    }

    /**
     * Generates a refresh token for a given username.
     * @param  userName the username
     * @return          the generated refresh token
     */
    public String generateRefreshToken(String userName) {
        return createToken(new HashMap<>(), userName, refreshExpiration);
    }

    // TODO:Have to pass refresh token time in application properties.
    private String createToken(Map<String, Object> claims, String userName, long expiration) {
        return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Validates if a JWT token is valid for a given user.
     * @param  token       the JWT token
     * @param  userDetails the user details
     * @return             true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserNameFromJwt(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Refreshes the access token by validating the refresh token and generating a
     * new access token.
     * @param  request     the HTTP servlet request
     * @param  response    the HTTP servlet response
     * @throws IOException if an I/O error occurs
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = extractUserNameFromJwt(refreshToken);
        if (userEmail != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
            if (isTokenValid(refreshToken, userDetails)) {
                var accessToken = generateToken(userDetails.getUsername());
                var authResponse =
                        AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }

        }

    }

}
