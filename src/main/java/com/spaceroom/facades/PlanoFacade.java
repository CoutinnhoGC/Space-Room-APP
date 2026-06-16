package com.spaceroom.facades;

import com.spaceroom.applications.PlanoApplication;
import com.spaceroom.entities.Plano;
import com.spaceroom.models.PlanoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanoFacade {

    private final PlanoApplication planoApplication;

    public PlanoModel criar(PlanoModel model) {
        Plano plano = converterModelParaEntity(model);
        Plano planoSalvo = planoApplication.criar(plano);
        return converterEntityParaModel(planoSalvo);
    }

    public List<PlanoModel> listarTodos() {
        return planoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public PlanoModel buscarPorId(Integer idPlano) {
        Plano plano = planoApplication.buscarPorId(idPlano);
        return converterEntityParaModel(plano);
    }

    public PlanoModel atualizar(Integer idPlano, PlanoModel model) {
        Plano plano = converterModelParaEntity(model);
        Plano planoAtualizado = planoApplication.atualizar(idPlano, plano);
        return converterEntityParaModel(planoAtualizado);
    }

    public void deletar(Integer idPlano) {
        planoApplication.deletar(idPlano);
    }

    private Plano converterModelParaEntity(PlanoModel model) {
        return Plano.builder()
                .idPlano(model.getIdPlano())
                .nome(model.getNome())
                .valor(model.getValor())
                .descricao(model.getDescricao())
                .limiteUsuarios(model.getLimiteUsuarios())
                .limiteEspacos(model.getLimiteEspacos())
                .limiteReservasMes(model.getLimiteReservasMes())
                .vitrineIncluida(model.getVitrineIncluida())
                .ativo(model.getAtivo())
                .criadoEm(model.getCriadoEm())
                .build();
    }

    private PlanoModel converterEntityParaModel(Plano plano) {
        PlanoModel model = new PlanoModel();
        model.setIdPlano(plano.getIdPlano());
        model.setNome(plano.getNome());
        model.setValor(plano.getValor());
        model.setDescricao(plano.getDescricao());
        model.setLimiteUsuarios(plano.getLimiteUsuarios());
        model.setLimiteEspacos(plano.getLimiteEspacos());
        model.setLimiteReservasMes(plano.getLimiteReservasMes());
        model.setVitrineIncluida(plano.getVitrineIncluida());
        model.setAtivo(plano.getAtivo());
        model.setCriadoEm(plano.getCriadoEm());
        return model;
    }
}
