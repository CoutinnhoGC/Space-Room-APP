package com.spaceroom.applications;

import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioApplication {

    private final UsuarioRepository usuarioRepository;
    private final AutorizacaoApplication autorizacaoApplication;

    @Autowired
    public UsuarioApplication(UsuarioRepository usuarioRepository, AutorizacaoApplication autorizacaoApplication) {
        this.usuarioRepository = usuarioRepository;
        this.autorizacaoApplication = autorizacaoApplication;
    }

    public UsuarioApplication(UsuarioRepository usuarioRepository) {
        this(usuarioRepository, null);
    }

    public Usuario criar(Usuario usuario) {
        return criar(usuario, null);
    }

    public Usuario criar(Usuario usuario, Boolean podeReservar) {
        String emailNormalizado = normalizarEmail(usuario.getEmail());
        usuario.setEmail(emailNormalizado);
        validarEmailDuplicado(emailNormalizado, null);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        sincronizarPermissaoReserva(usuarioSalvo, podeReservar);
        return usuarioRepository.findById(usuarioSalvo.getIdUsuario()).orElse(usuarioSalvo);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario nao encontrado para o id: " + idUsuario
                ));
    }

    public Usuario atualizar(Long idUsuario, Usuario dadosAtualizados) {
        return atualizar(idUsuario, dadosAtualizados, null);
    }

    public Usuario atualizar(Long idUsuario, Usuario dadosAtualizados, Boolean podeReservar) {
        Usuario usuarioExistente = buscarPorId(idUsuario);
        String emailNormalizado = normalizarEmail(dadosAtualizados.getEmail());

        validarEmailDuplicado(emailNormalizado, idUsuario);

        usuarioExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        usuarioExistente.setIdCargo(dadosAtualizados.getIdCargo());
        usuarioExistente.setNome(dadosAtualizados.getNome());
        usuarioExistente.setEmail(emailNormalizado);
        usuarioExistente.setSenhaHash(dadosAtualizados.getSenhaHash());
        usuarioExistente.setPrimeiroAcesso(dadosAtualizados.getPrimeiroAcesso());
        usuarioExistente.setTokenDefinicaoSenha(dadosAtualizados.getTokenDefinicaoSenha());
        usuarioExistente.setTokenExpiracao(dadosAtualizados.getTokenExpiracao());
        usuarioExistente.setUltimoLoginEm(dadosAtualizados.getUltimoLoginEm());
        usuarioExistente.setAtivo(dadosAtualizados.getAtivo());

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        sincronizarPermissaoReserva(usuarioAtualizado, podeReservar);
        return usuarioRepository.findById(usuarioAtualizado.getIdUsuario()).orElse(usuarioAtualizado);
    }

    public void deletar(Long idUsuario) {
        Usuario usuario = buscarPorId(idUsuario);
        usuarioRepository.delete(usuario);
    }

    private void validarEmailDuplicado(String email, Long idUsuarioAtual) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            boolean emailPertenceOutroUsuario = idUsuarioAtual == null || !usuario.getIdUsuario().equals(idUsuarioAtual);
            if (emailPertenceOutroUsuario) {
                throw new BusinessException("Ja existe usuario cadastrado com o email informado.");
            }
        });
    }

    private void sincronizarPermissaoReserva(Usuario usuario, Boolean podeReservar) {
        if (autorizacaoApplication == null) {
            return;
        }

        autorizacaoApplication.sincronizarPermissaoReserva(usuario, podeReservar);
    }

    private String normalizarEmail(String email) {
        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}
