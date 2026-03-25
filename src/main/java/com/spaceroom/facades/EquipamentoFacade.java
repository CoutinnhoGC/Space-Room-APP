package com.spaceroom.facades;

import com.spaceroom.applications.EquipamentoApplication;
import com.spaceroom.entities.Equipamento;
import com.spaceroom.entities.StatusEquipamento;
import com.spaceroom.models.EquipamentoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EquipamentoFacade {

    private final EquipamentoApplication equipamentoApplication;

    public EquipamentoModel criar(EquipamentoModel model) {
        Equipamento equipamento = converterModelParaEntity(model);
        Equipamento equipamentoSalvo = equipamentoApplication.criar(equipamento);
        return converterEntityParaModel(equipamentoSalvo);
    }

    public List<EquipamentoModel> listarTodos() {
        return equipamentoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public EquipamentoModel buscarPorId(Long idEquipamento) {
        Equipamento equipamento = equipamentoApplication.buscarPorId(idEquipamento);
        return converterEntityParaModel(equipamento);
    }

    public EquipamentoModel atualizar(Long idEquipamento, EquipamentoModel model) {
        Equipamento equipamento = converterModelParaEntity(model);
        Equipamento equipamentoAtualizado = equipamentoApplication.atualizar(idEquipamento, equipamento);
        return converterEntityParaModel(equipamentoAtualizado);
    }

    public void deletar(Long idEquipamento) {
        equipamentoApplication.deletar(idEquipamento);
    }

    private Equipamento converterModelParaEntity(EquipamentoModel model) {
        return Equipamento.builder()
                .idEquipamento(model.getIdEquipamento())
                .idInstituicao(model.getIdInstituicao())
                .idEspaco(model.getIdEspaco())
                .nome(model.getNome())
                .descricao(model.getDescricao())
                .patrimonio(model.getPatrimonio())
                .status(model.getStatus() != null ? model.getStatus() : StatusEquipamento.DISPONIVEL)
                .quantidadeTotal(model.getQuantidadeTotal())
                .ativo(model.getAtivo())
                .criadoEm(model.getCriadoEm())
                .atualizadoEm(model.getAtualizadoEm())
                .build();
    }

    private EquipamentoModel converterEntityParaModel(Equipamento equipamento) {
        EquipamentoModel model = new EquipamentoModel();
        model.setIdEquipamento(equipamento.getIdEquipamento());
        model.setIdInstituicao(equipamento.getIdInstituicao());
        model.setIdEspaco(equipamento.getIdEspaco());
        model.setNome(equipamento.getNome());
        model.setDescricao(equipamento.getDescricao());
        model.setPatrimonio(equipamento.getPatrimonio());
        model.setStatus(equipamento.getStatus());
        model.setQuantidadeTotal(equipamento.getQuantidadeTotal());
        model.setAtivo(equipamento.getAtivo());
        model.setCriadoEm(equipamento.getCriadoEm());
        model.setAtualizadoEm(equipamento.getAtualizadoEm());
        return model;
    }
}
