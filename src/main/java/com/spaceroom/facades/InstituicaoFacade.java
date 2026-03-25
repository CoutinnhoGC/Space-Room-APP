package com.spaceroom.facades;

import com.spaceroom.applications.InstituicaoApplication;
import com.spaceroom.entities.Instituicao;
import com.spaceroom.models.InstituicaoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InstituicaoFacade {

    private final InstituicaoApplication instituicaoApplication;

    public InstituicaoModel criar(InstituicaoModel model) {
        Instituicao instituicao = converterModelParaEntity(model);
        Instituicao instituicaoSalva = instituicaoApplication.criar(instituicao);
        return converterEntityParaModel(instituicaoSalva);
    }

    public List<InstituicaoModel> listarTodas() {
        return instituicaoApplication.listarTodas()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public InstituicaoModel buscarPorId(Long idInstituicao) {
        Instituicao instituicao = instituicaoApplication.buscarPorId(idInstituicao);
        return converterEntityParaModel(instituicao);
    }

    public InstituicaoModel atualizar(Long idInstituicao, InstituicaoModel model) {
        Instituicao instituicao = converterModelParaEntity(model);
        Instituicao instituicaoAtualizada = instituicaoApplication.atualizar(idInstituicao, instituicao);
        return converterEntityParaModel(instituicaoAtualizada);
    }

    public void deletar(Long idInstituicao) {
        instituicaoApplication.deletar(idInstituicao);
    }

    private Instituicao converterModelParaEntity(InstituicaoModel model) {
        return Instituicao.builder()
                .idInstituicao(model.getIdInstituicao())
                .idPlano(model.getIdPlano())
                .nomeFantasia(model.getNomeFantasia())
                .razaoSocial(model.getRazaoSocial())
                .cnpj(model.getCnpj())
                .email(model.getEmail())
                .telefone(model.getTelefone())
                .responsavel(model.getResponsavel())
                .endereco(model.getEndereco())
                .cidade(model.getCidade())
                .estado(model.getEstado())
                .cep(model.getCep())
                .tipo(model.getTipo())
                .vitrineHabilitada(model.getVitrineHabilitada())
                .ativo(model.getAtivo())
                .criadoEm(model.getCriadoEm())
                .atualizadoEm(model.getAtualizadoEm())
                .build();
    }

    private InstituicaoModel converterEntityParaModel(Instituicao instituicao) {
        InstituicaoModel model = new InstituicaoModel();
        model.setIdInstituicao(instituicao.getIdInstituicao());
        model.setIdPlano(instituicao.getIdPlano());
        model.setNomeFantasia(instituicao.getNomeFantasia());
        model.setRazaoSocial(instituicao.getRazaoSocial());
        model.setCnpj(instituicao.getCnpj());
        model.setEmail(instituicao.getEmail());
        model.setTelefone(instituicao.getTelefone());
        model.setResponsavel(instituicao.getResponsavel());
        model.setEndereco(instituicao.getEndereco());
        model.setCidade(instituicao.getCidade());
        model.setEstado(instituicao.getEstado());
        model.setCep(instituicao.getCep());
        model.setTipo(instituicao.getTipo());
        model.setVitrineHabilitada(instituicao.getVitrineHabilitada());
        model.setAtivo(instituicao.getAtivo());
        model.setCriadoEm(instituicao.getCriadoEm());
        model.setAtualizadoEm(instituicao.getAtualizadoEm());
        return model;
    }
}
