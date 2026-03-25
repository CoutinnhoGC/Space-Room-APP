package com.spaceroom.facades;

import com.spaceroom.applications.ProjetoIntegranteApplication;
import com.spaceroom.entities.ProjetoIntegrante;
import com.spaceroom.models.ProjetoIntegranteModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjetoIntegranteFacade {

    private final ProjetoIntegranteApplication projetoIntegranteApplication;

    public ProjetoIntegranteModel criar(ProjetoIntegranteModel model) {
        ProjetoIntegrante projetoIntegrante = converterModelParaEntity(model);
        ProjetoIntegrante projetoIntegranteSalvo = projetoIntegranteApplication.criar(projetoIntegrante);
        return converterEntityParaModel(projetoIntegranteSalvo);
    }

    public List<ProjetoIntegranteModel> listarTodos() {
        return projetoIntegranteApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public ProjetoIntegranteModel buscarPorId(Long idProjetoIntegrante) {
        ProjetoIntegrante projetoIntegrante = projetoIntegranteApplication.buscarPorId(idProjetoIntegrante);
        return converterEntityParaModel(projetoIntegrante);
    }

    public ProjetoIntegranteModel atualizar(Long idProjetoIntegrante, ProjetoIntegranteModel model) {
        ProjetoIntegrante projetoIntegrante = converterModelParaEntity(model);
        ProjetoIntegrante projetoIntegranteAtualizado = projetoIntegranteApplication.atualizar(idProjetoIntegrante, projetoIntegrante);
        return converterEntityParaModel(projetoIntegranteAtualizado);
    }

    public void deletar(Long idProjetoIntegrante) {
        projetoIntegranteApplication.deletar(idProjetoIntegrante);
    }

    private ProjetoIntegrante converterModelParaEntity(ProjetoIntegranteModel model) {
        return ProjetoIntegrante.builder()
                .idProjetoIntegrante(model.getIdProjetoIntegrante())
                .idProjeto(model.getIdProjeto())
                .idUsuario(model.getIdUsuario())
                .funcao(model.getFuncao())
                .build();
    }

    private ProjetoIntegranteModel converterEntityParaModel(ProjetoIntegrante projetoIntegrante) {
        ProjetoIntegranteModel model = new ProjetoIntegranteModel();
        model.setIdProjetoIntegrante(projetoIntegrante.getIdProjetoIntegrante());
        model.setIdProjeto(projetoIntegrante.getIdProjeto());
        model.setIdUsuario(projetoIntegrante.getIdUsuario());
        model.setFuncao(projetoIntegrante.getFuncao());
        return model;
    }
}
