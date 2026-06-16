package com.spaceroom.controllers;

import com.spaceroom.facades.AuthFacade;
import com.spaceroom.models.AuthLoginRequest;
import com.spaceroom.models.AuthLoginResponse;
import com.spaceroom.models.PasswordRecoveryRequest;
import com.spaceroom.models.PasswordRecoveryResponse;
import com.spaceroom.models.UsuarioModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/login")
    public AuthLoginResponse login(@RequestBody @Valid AuthLoginRequest request, HttpServletRequest httpRequest) {
        return authFacade.autenticar(request.getEmail(), request.getSenha(), httpRequest.getRemoteAddr());
    }

    @GetMapping("/me")
    public UsuarioModel me() {
        return authFacade.obterUsuarioAtual();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        authFacade.logout();
    }

    @PostMapping("/forgot-password")
    public PasswordRecoveryResponse forgotPassword(@RequestBody @Valid PasswordRecoveryRequest request) {
        return authFacade.solicitarRecuperacaoSenha(request.getEmail());
    }
}
