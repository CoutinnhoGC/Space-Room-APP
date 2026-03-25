package com.spaceroom.applications;

import com.spaceroom.entities.Equipamento;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoApplication {

    private final EquipamentoRepository equipamentoRepository;

    public Equipamento criar(Equipamento equipamento) {
        return equipamentoRepository.save(equipamento);
    }

    public List<Equipamento> listarTodos() {
        return equipamentoRepository.findAll();
    }

    public Equipamento buscarPorId(Long idEquipamento) {
        return equipamentoRepository.findById(idEquipamento)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipamento nao encontrado para o id: " + idEquipamento
                ));
    }

    public Equipamento atualizar(Long idEquipamento, Equipamento dadosAtualizados) {
        Equipamento equipamentoExistente = buscarPorId(idEquipamento);

        equipamentoExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        equipamentoExistente.setIdEspaco(dadosAtualizados.getIdEspaco());
        equipamentoExistente.setNome(dadosAtualizados.getNome());
        equipamentoExistente.setDescricao(dadosAtualizados.getDescricao());
        equipamentoExistente.setPatrimonio(dadosAtualizados.getPatrimonio());
        equipamentoExistente.setStatus(dadosAtualizados.getStatus());
        equipamentoExistente.setQuantidadeTotal(dadosAtualizados.getQuantidadeTotal());
        equipamentoExistente.setAtivo(dadosAtualizados.getAtivo());

        return equipamentoRepository.save(equipamentoExistente);
    }

    public void deletar(Long idEquipamento) {
        Equipamento equipamento = buscarPorId(idEquipamento);
        equipamentoRepository.delete(equipamento);
    }
}
