package com.spaceroom.facades;

import com.spaceroom.applications.EspacoApplication;
import com.spaceroom.entities.Espaco;
import com.spaceroom.models.EspacoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EspacoFacade {

    private final EspacoApplication espacoApplication;

    public EspacoModel criar(EspacoModel model) {
        Espaco espaco = converterModelParaEntity(model);
        Espaco espacoSalvo = espacoApplication.criar(espaco);
        return converterEntityParaModel(espacoSalvo);
    }

    public List<EspacoModel> listarTodos() {
        return espacoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public EspacoModel buscarPorId(Long idEspaco) {
        Espaco espaco = espacoApplication.buscarPorId(idEspaco);
        return converterEntityParaModel(espaco);
    }

    public EspacoModel atualizar(Long idEspaco, EspacoModel model) {
        Espaco espaco = converterModelParaEntity(model);
        Espaco espacoAtualizado = espacoApplication.atualizar(idEspaco, espaco);
        return converterEntityParaModel(espacoAtualizado);
    }

    public void deletar(Long idEspaco) {
        espacoApplication.deletar(idEspaco);
    }

    private Espaco converterModelParaEntity(EspacoModel model) {
        return Espaco.builder()
                .idEspaco(model.getIdEspaco())
                .idInstituicao(model.getIdInstituicao())
                .idEspacoPai(model.getIdEspacoPai())
                .nome(model.getNome())
                .descricao(model.getDescricao())
                .tipo(model.getTipo())
                .localizacao(model.getLocalizacao())
                .capacidade(model.getCapacidade())
                .recursosFixos(model.getRecursosFixos())
                .imagemUrl(model.getImagemUrl())
                .codigoUnidade(model.getCodigoUnidade())
                .permiteSubespacos(model.getPermiteSubespacos())
                .exigeAprovacao(model.getExigeAprovacao())
                .idResponsavelEspaco(model.getIdResponsavelEspaco())
                .ativo(model.getAtivo())
                .criadoEm(model.getCriadoEm())
                .atualizadoEm(model.getAtualizadoEm())
                .build();
    }

    private EspacoModel converterEntityParaModel(Espaco espaco) {
        EspacoModel model = new EspacoModel();
        model.setIdEspaco(espaco.getIdEspaco());
        model.setIdInstituicao(espaco.getIdInstituicao());
        model.setIdEspacoPai(espaco.getIdEspacoPai());
        model.setNome(espaco.getNome());
        model.setDescricao(espaco.getDescricao());
        model.setTipo(espaco.getTipo());
        model.setLocalizacao(espaco.getLocalizacao());
        model.setCapacidade(espaco.getCapacidade());
        model.setRecursosFixos(espaco.getRecursosFixos());
        model.setImagemUrl(espaco.getImagemUrl());
        model.setCodigoUnidade(espaco.getCodigoUnidade());
        model.setPermiteSubespacos(espaco.getPermiteSubespacos());
        model.setExigeAprovacao(espaco.getExigeAprovacao());
        model.setIdResponsavelEspaco(espaco.getIdResponsavelEspaco());
        model.setAtivo(espaco.getAtivo());
        model.setCriadoEm(espaco.getCriadoEm());
        model.setAtualizadoEm(espaco.getAtualizadoEm());
        return model;
    }
}
