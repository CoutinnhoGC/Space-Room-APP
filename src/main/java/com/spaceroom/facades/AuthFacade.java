package com.spaceroom.facades;

import com.spaceroom.applications.AuthApplication;
import com.spaceroom.entities.Usuario;
import com.spaceroom.models.PasswordRecoveryResponse;
import com.spaceroom.models.UsuarioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthApplication authApplication;
    private final UsuarioFacade usuarioFacade;

    public UsuarioModel autenticar(String email, String senha) {
        Usuario usuario = authApplication.autenticar(email, senha);
        return usuarioFacade.buscarPorId(usuario.getIdUsuario());
    }

    public PasswordRecoveryResponse solicitarRecuperacaoSenha(String email) {
        return authApplication.solicitarRecuperacaoSenha(email);
    }
}
