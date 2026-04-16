package com.spaceroom.applications;

import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.models.PasswordRecoveryResponse;
import com.spaceroom.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthApplication {

    private static final long RECOVERY_TOKEN_MINUTES = 30;

    private final UsuarioRepository usuarioRepository;

    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(normalizarEmail(email))
                .orElseThrow(() -> new BusinessException("Credenciais invalidas."));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new BusinessException("Este usuario esta inativo.");
        }

        if (usuario.getSenhaHash() == null || !usuario.getSenhaHash().equals(senha)) {
            throw new BusinessException("Credenciais invalidas.");
        }

        usuario.setUltimoLoginEm(LocalDateTime.now());
        usuario.setPrimeiroAcesso(false);
        return usuarioRepository.save(usuario);
    }

    public PasswordRecoveryResponse solicitarRecuperacaoSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(normalizarEmail(email))
                .orElseThrow(() -> new BusinessException("Nao existe usuario cadastrado com o e-mail informado."));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new BusinessException("Este usuario esta inativo.");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(RECOVERY_TOKEN_MINUTES);

        usuario.setTokenDefinicaoSenha(token);
        usuario.setTokenExpiracao(expiracao);
        usuarioRepository.save(usuario);

        return PasswordRecoveryResponse.builder()
                .message("Solicitacao registrada. Email de recuperacao preparado em modo mock.")
                .email(usuario.getEmail())
                .deliveryMode("mock")
                .recoveryToken(token)
                .expiresAt(expiracao)
                .build();
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
