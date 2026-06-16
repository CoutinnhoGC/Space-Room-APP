package com.spaceroom.facades;

import com.spaceroom.applications.UsuarioApplication;
import com.spaceroom.entities.Usuario;
import com.spaceroom.models.UsuarioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioFacade {

    private final UsuarioApplication usuarioApplication;

    public UsuarioModel criar(UsuarioModel model) {
        Usuario usuario = converterModelParaEntity(model);
        return usuarioApplication.toModel(usuarioApplication.criar(usuario, model.getPodeReservar()));
    }

    public List<UsuarioModel> listarTodos() {
        return usuarioApplication.listarTodos()
                .stream()
                .map(usuarioApplication::toModel)
                .toList();
    }

    public UsuarioModel buscarPorId(Long idUsuario) {
        return usuarioApplication.toModel(usuarioApplication.buscarPorId(idUsuario));
    }

    public UsuarioModel atualizar(Long idUsuario, UsuarioModel model) {
        Usuario usuario = converterModelParaEntity(model);
        return usuarioApplication.toModel(usuarioApplication.atualizar(idUsuario, usuario, model.getPodeReservar()));
    }

    public void deletar(Long idUsuario) {
        usuarioApplication.deletar(idUsuario);
    }

    private Usuario converterModelParaEntity(UsuarioModel model) {
        return Usuario.builder()
                .idUsuario(model.getIdUsuario())
                .idInstituicao(model.getIdInstituicao())
                .idCargo(model.getIdCargo())
                .nome(model.getNome())
                .email(model.getEmail())
                .senhaHash(model.getSenhaHash())
                .primeiroAcesso(model.getPrimeiroAcesso())
                .tokenDefinicaoSenha(model.getTokenDefinicaoSenha())
                .tokenExpiracao(model.getTokenExpiracao())
                .ultimoLoginEm(model.getUltimoLoginEm())
                .ativo(model.getAtivo())
                .criadoEm(model.getCriadoEm())
                .atualizadoEm(model.getAtualizadoEm())
                .build();
    }
}
