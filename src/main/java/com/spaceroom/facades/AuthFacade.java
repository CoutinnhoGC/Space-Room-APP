package com.spaceroom.facades;

import com.spaceroom.applications.AuthApplication;
import com.spaceroom.applications.UsuarioApplication;
import com.spaceroom.models.AuthLoginResponse;
import com.spaceroom.models.PasswordRecoveryResponse;
import com.spaceroom.models.UsuarioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthApplication authApplication;
    private final UsuarioApplication usuarioApplication;

    public AuthLoginResponse autenticar(String email, String senha, String clientIp) {
        return authApplication.autenticar(email, senha, clientIp);
    }

    public UsuarioModel obterUsuarioAtual() {
        return usuarioApplication.toModel(authApplication.obterUsuarioAtual());
    }

    public void logout() {
        authApplication.logout();
    }

    public PasswordRecoveryResponse solicitarRecuperacaoSenha(String email) {
        return authApplication.solicitarRecuperacaoSenha(email);
    }
}
