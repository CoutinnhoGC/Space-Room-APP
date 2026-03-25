package com.spaceroom.facades;

import com.spaceroom.applications.UsuarioPermissaoApplication;
import com.spaceroom.entities.UsuarioPermissao;
import com.spaceroom.models.UsuarioPermissaoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioPermissaoFacade {

    private final UsuarioPermissaoApplication usuarioPermissaoApplication;

    public UsuarioPermissaoModel criar(UsuarioPermissaoModel model) {
        UsuarioPermissao usuarioPermissao = converterModelParaEntity(model);
        UsuarioPermissao usuarioPermissaoSalva = usuarioPermissaoApplication.criar(usuarioPermissao);
        return converterEntityParaModel(usuarioPermissaoSalva);
    }

    public List<UsuarioPermissaoModel> listarTodos() {
        return usuarioPermissaoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public UsuarioPermissaoModel buscarPorId(Long idUsuario, Integer idPermissao) {
        UsuarioPermissao usuarioPermissao = usuarioPermissaoApplication.buscarPorId(idUsuario, idPermissao);
        return converterEntityParaModel(usuarioPermissao);
    }

    public UsuarioPermissaoModel atualizar(Long idUsuario, Integer idPermissao, UsuarioPermissaoModel model) {
        UsuarioPermissao usuarioPermissao = converterModelParaEntity(model);
        UsuarioPermissao usuarioPermissaoAtualizada = usuarioPermissaoApplication.atualizar(idUsuario, idPermissao, usuarioPermissao);
        return converterEntityParaModel(usuarioPermissaoAtualizada);
    }

    public void deletar(Long idUsuario, Integer idPermissao) {
        usuarioPermissaoApplication.deletar(idUsuario, idPermissao);
    }

    private UsuarioPermissao converterModelParaEntity(UsuarioPermissaoModel model) {
        return UsuarioPermissao.builder()
                .idUsuario(model.getIdUsuario())
                .idPermissao(model.getIdPermissao())
                .concedida(model.getConcedida())
                .build();
    }

    private UsuarioPermissaoModel converterEntityParaModel(UsuarioPermissao usuarioPermissao) {
        UsuarioPermissaoModel model = new UsuarioPermissaoModel();
        model.setIdUsuario(usuarioPermissao.getIdUsuario());
        model.setIdPermissao(usuarioPermissao.getIdPermissao());
        model.setConcedida(usuarioPermissao.getConcedida());
        return model;
    }
}
