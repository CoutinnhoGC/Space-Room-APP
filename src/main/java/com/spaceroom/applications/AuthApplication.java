package com.spaceroom.applications;

import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.models.AuthLoginResponse;
import com.spaceroom.models.PasswordRecoveryResponse;
import com.spaceroom.repositories.UsuarioRepository;
import com.spaceroom.security.JwtService;
import com.spaceroom.security.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthApplication {

    private static final long RECOVERY_TOKEN_MINUTES = 30;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;
    private final UsuarioApplication usuarioApplication;

    public AuthLoginResponse autenticar(String email, String senha, String clientIp) {
        String emailNormalizado = normalizarEmail(email);
        loginAttemptService.ensureAllowed(emailNormalizado, clientIp);

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> credenciaisInvalidas(emailNormalizado, clientIp));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new BusinessException("Este usuário está inativo.");
        }

        if (!senhaConfere(usuario, senha)) {
            throw credenciaisInvalidas(emailNormalizado, clientIp);
        }

        if (!isHashBcrypt(usuario.getSenhaHash())) {
            usuario.setSenhaHash(passwordEncoder.encode(senha));
        }

        usuario.setUltimoLoginEm(LocalDateTime.now());
        usuario.setPrimeiroAcesso(false);
        usuario.setSessaoRevogadaEm(null);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        loginAttemptService.recordSuccess(emailNormalizado, clientIp);

        String token = jwtService.generateToken(usuarioAtualizado);
        return AuthLoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresAt(jwtService.getExpirationDate(token))
                .usuario(usuarioApplication.toModel(usuarioAtualizado))
                .build();
    }

    public void logout() {
        Usuario usuario = usuarioApplication.obterUsuarioAtual();
        usuario.setSessaoRevogadaEm(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    public Usuario obterUsuarioAtual() {
        return usuarioApplication.obterUsuarioAtual();
    }

    public PasswordRecoveryResponse solicitarRecuperacaoSenha(String email) {
        usuarioRepository.findByEmail(normalizarEmail(email)).ifPresent(usuario -> {
            if (Boolean.FALSE.equals(usuario.getAtivo())) {
                return;
            }

            String token = UUID.randomUUID().toString();
            LocalDateTime expiracao = LocalDateTime.now().plusMinutes(RECOVERY_TOKEN_MINUTES);
            usuario.setTokenDefinicaoSenha(token);
            usuario.setTokenExpiracao(expiracao);
            usuarioRepository.save(usuario);
        });

        return PasswordRecoveryResponse.builder()
                .message("Se existir uma conta vinculada a este e-mail, a solicitacao de recuperacao sera processada.")
                .deliveryMode("mock")
                .build();
    }

    private boolean senhaConfere(Usuario usuario, String senha) {
        if (usuario.getSenhaHash() == null || senha == null || senha.isBlank()) {
            return false;
        }

        return passwordEncoder.matches(senha, usuario.getSenhaHash()) || usuario.getSenhaHash().equals(senha);
    }

    private boolean isHashBcrypt(String valor) {
        return valor != null && valor.matches("^\\$2[aby]\\$.{56}$");
    }

    private BusinessException credenciaisInvalidas(String email, String clientIp) {
        loginAttemptService.recordFailure(email, clientIp);
        return new BusinessException("Credenciais invalidas.");
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
