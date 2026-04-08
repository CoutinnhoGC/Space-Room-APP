package com.spaceroom.facades;

import com.spaceroom.applications.AutorizacaoApplication;
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
    private final AutorizacaoApplication autorizacaoApplication;

    public UsuarioModel criar(UsuarioModel model) {
        Usuario usuario = converterModelParaEntity(model);
        Usuario usuarioSalvo = usuarioApplication.criar(usuario, model.getPodeReservar());
        return converterEntityParaModel(usuarioSalvo);
    }

    public List<UsuarioModel> listarTodos() {
        return usuarioApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public UsuarioModel buscarPorId(Long idUsuario) {
        Usuario usuario = usuarioApplication.buscarPorId(idUsuario);
        return converterEntityParaModel(usuario);
    }

    public UsuarioModel atualizar(Long idUsuario, UsuarioModel model) {
        Usuario usuario = converterModelParaEntity(model);
        Usuario usuarioAtualizado = usuarioApplication.atualizar(idUsuario, usuario, model.getPodeReservar());
        return converterEntityParaModel(usuarioAtualizado);
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

    private UsuarioModel converterEntityParaModel(Usuario usuario) {
        UsuarioModel model = new UsuarioModel();
        model.setIdUsuario(usuario.getIdUsuario());
        model.setIdInstituicao(usuario.getIdInstituicao());
        model.setIdCargo(usuario.getIdCargo());
        model.setNome(usuario.getNome());
        model.setEmail(usuario.getEmail());
        model.setSenhaHash(usuario.getSenhaHash());
        model.setPrimeiroAcesso(usuario.getPrimeiroAcesso());
        model.setTokenDefinicaoSenha(usuario.getTokenDefinicaoSenha());
        model.setTokenExpiracao(usuario.getTokenExpiracao());
        model.setUltimoLoginEm(usuario.getUltimoLoginEm());
        model.setAtivo(usuario.getAtivo());
        model.setPodeReservar(autorizacaoApplication.resolverPodeReservar(usuario, null));
        model.setAdminPlataforma(autorizacaoApplication.resolverAdminPlataforma(usuario));
        model.setCriadoEm(usuario.getCriadoEm());
        model.setAtualizadoEm(usuario.getAtualizadoEm());
        return model;
    }
}
