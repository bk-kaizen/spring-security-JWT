package com.bk.jwtsecurity.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) class representing the authentication response.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    /**
     * The access token for authentication.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * The refresh token for authentication.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
