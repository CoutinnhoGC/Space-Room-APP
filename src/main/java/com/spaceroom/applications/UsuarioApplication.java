package com.spaceroom.applications;

import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioApplication {

    private final UsuarioRepository usuarioRepository;

    public Usuario criar(Usuario usuario) {
        validarEmailDuplicado(usuario.getEmail(), null);
        return usuarioRepository.save(usuario);
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
        Usuario usuarioExistente = buscarPorId(idUsuario);

        validarEmailDuplicado(dadosAtualizados.getEmail(), idUsuario);

        usuarioExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        usuarioExistente.setIdCargo(dadosAtualizados.getIdCargo());
        usuarioExistente.setNome(dadosAtualizados.getNome());
        usuarioExistente.setEmail(dadosAtualizados.getEmail());
        usuarioExistente.setSenhaHash(dadosAtualizados.getSenhaHash());
        usuarioExistente.setPrimeiroAcesso(dadosAtualizados.getPrimeiroAcesso());
        usuarioExistente.setTokenDefinicaoSenha(dadosAtualizados.getTokenDefinicaoSenha());
        usuarioExistente.setTokenExpiracao(dadosAtualizados.getTokenExpiracao());
        usuarioExistente.setUltimoLoginEm(dadosAtualizados.getUltimoLoginEm());
        usuarioExistente.setAtivo(dadosAtualizados.getAtivo());

        return usuarioRepository.save(usuarioExistente);
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
}
