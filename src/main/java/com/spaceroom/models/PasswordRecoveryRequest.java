package com.spaceroom.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRecoveryRequest {

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail valido.")
    private String email;
}
