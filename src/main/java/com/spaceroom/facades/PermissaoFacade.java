package com.spaceroom.facades;

import com.spaceroom.applications.PermissaoApplication;
import com.spaceroom.entities.Permissao;
import com.spaceroom.models.PermissaoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissaoFacade {

    private final PermissaoApplication permissaoApplication;

    public PermissaoModel criar(PermissaoModel model) {
        Permissao permissao = converterModelParaEntity(model);
        Permissao permissaoSalva = permissaoApplication.criar(permissao);
        return converterEntityParaModel(permissaoSalva);
    }

    public List<PermissaoModel> listarTodas() {
        return permissaoApplication.listarTodas()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public PermissaoModel buscarPorId(Integer idPermissao) {
        Permissao permissao = permissaoApplication.buscarPorId(idPermissao);
        return converterEntityParaModel(permissao);
    }

    public PermissaoModel atualizar(Integer idPermissao, PermissaoModel model) {
        Permissao permissao = converterModelParaEntity(model);
        Permissao permissaoAtualizada = permissaoApplication.atualizar(idPermissao, permissao);
        return converterEntityParaModel(permissaoAtualizada);
    }

    public void deletar(Integer idPermissao) {
        permissaoApplication.deletar(idPermissao);
    }

    private Permissao converterModelParaEntity(PermissaoModel model) {
        return Permissao.builder()
                .idPermissao(model.getIdPermissao())
                .nome(model.getNome())
                .descricao(model.getDescricao())
                .build();
    }

    private PermissaoModel converterEntityParaModel(Permissao permissao) {
        PermissaoModel model = new PermissaoModel();
        model.setIdPermissao(permissao.getIdPermissao());
        model.setNome(permissao.getNome());
        model.setDescricao(permissao.getDescricao());
        return model;
    }
}
