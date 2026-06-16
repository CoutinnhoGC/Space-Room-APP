package com.spaceroom.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordRecoveryResponse {

    private final String message;
    private final String deliveryMode;
}
