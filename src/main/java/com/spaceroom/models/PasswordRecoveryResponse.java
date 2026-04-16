package com.spaceroom.models;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PasswordRecoveryResponse {

    private final String message;
    private final String email;
    private final String deliveryMode;
    private final String recoveryToken;
    private final LocalDateTime expiresAt;
}
