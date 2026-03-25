package com.spaceroom.facades;

import com.spaceroom.applications.ProjetoApplication;
import com.spaceroom.entities.Projeto;
import com.spaceroom.entities.StatusProjeto;
import com.spaceroom.models.ProjetoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjetoFacade {

    private final ProjetoApplication projetoApplication;

    public ProjetoModel criar(ProjetoModel model) {
        Projeto projeto = converterModelParaEntity(model);
        Projeto projetoSalvo = projetoApplication.criar(projeto);
        return converterEntityParaModel(projetoSalvo);
    }

    public List<ProjetoModel> listarTodos() {
        return projetoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public ProjetoModel buscarPorId(Long idProjeto) {
        Projeto projeto = projetoApplication.buscarPorId(idProjeto);
        return converterEntityParaModel(projeto);
    }

    public ProjetoModel atualizar(Long idProjeto, ProjetoModel model) {
        Projeto projeto = converterModelParaEntity(model);
        Projeto projetoAtualizado = projetoApplication.atualizar(idProjeto, projeto);
        return converterEntityParaModel(projetoAtualizado);
    }

    public void deletar(Long idProjeto) {
        projetoApplication.deletar(idProjeto);
    }

    private Projeto converterModelParaEntity(ProjetoModel model) {
        return Projeto.builder()
                .idProjeto(model.getIdProjeto())
                .idInstituicao(model.getIdInstituicao())
                .titulo(model.getTitulo())
                .resumo(model.getResumo())
                .descricao(model.getDescricao())
                .imagemUrl(model.getImagemUrl())
                .linkExterno(model.getLinkExterno())
                .status(model.getStatus() != null ? model.getStatus() : StatusProjeto.PENDENTE)
                .criadoPor(model.getCriadoPor())
                .criadoEm(model.getCriadoEm())
                .atualizadoEm(model.getAtualizadoEm())
                .build();
    }

    private ProjetoModel converterEntityParaModel(Projeto projeto) {
        ProjetoModel model = new ProjetoModel();
        model.setIdProjeto(projeto.getIdProjeto());
        model.setIdInstituicao(projeto.getIdInstituicao());
        model.setTitulo(projeto.getTitulo());
        model.setResumo(projeto.getResumo());
        model.setDescricao(projeto.getDescricao());
        model.setImagemUrl(projeto.getImagemUrl());
        model.setLinkExterno(projeto.getLinkExterno());
        model.setStatus(projeto.getStatus());
        model.setCriadoPor(projeto.getCriadoPor());
        model.setCriadoEm(projeto.getCriadoEm());
        model.setAtualizadoEm(projeto.getAtualizadoEm());
        return model;
    }
}
