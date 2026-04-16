package com.spaceroom.controllers;

import com.spaceroom.facades.AuthFacade;
import com.spaceroom.models.AuthLoginRequest;
import com.spaceroom.models.PasswordRecoveryRequest;
import com.spaceroom.models.PasswordRecoveryResponse;
import com.spaceroom.models.UsuarioModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/login")
    public UsuarioModel login(@RequestBody @Valid AuthLoginRequest request) {
        return authFacade.autenticar(request.getEmail(), request.getSenha());
    }

    @PostMapping("/forgot-password")
    public PasswordRecoveryResponse forgotPassword(@RequestBody @Valid PasswordRecoveryRequest request) {
        return authFacade.solicitarRecuperacaoSenha(request.getEmail());
    }
}
