package com.spaceroom.models;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuthLoginResponse {

    private final String accessToken;
    private final String tokenType;
    private final LocalDateTime expiresAt;
    private final UsuarioModel usuario;
}
