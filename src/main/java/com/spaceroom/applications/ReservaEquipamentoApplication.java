package com.spaceroom.applications;

import com.spaceroom.entities.ReservaEquipamento;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.ReservaEquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaEquipamentoApplication {

    private final ReservaEquipamentoRepository reservaEquipamentoRepository;

    public ReservaEquipamento criar(ReservaEquipamento reservaEquipamento) {
        return reservaEquipamentoRepository.save(reservaEquipamento);
    }

    public List<ReservaEquipamento> listarTodos() {
        return reservaEquipamentoRepository.findAll();
    }

    public ReservaEquipamento buscarPorId(Long idReservaEquipamento) {
        return reservaEquipamentoRepository.findById(idReservaEquipamento)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipamento da reserva não encontrado para o id: " + idReservaEquipamento
                ));
    }

    public ReservaEquipamento atualizar(Long idReservaEquipamento, ReservaEquipamento dadosAtualizados) {
        ReservaEquipamento reservaEquipamentoExistente = buscarPorId(idReservaEquipamento);

        reservaEquipamentoExistente.setIdReserva(dadosAtualizados.getIdReserva());
        reservaEquipamentoExistente.setIdEquipamento(dadosAtualizados.getIdEquipamento());
        reservaEquipamentoExistente.setQuantidade(dadosAtualizados.getQuantidade());

        return reservaEquipamentoRepository.save(reservaEquipamentoExistente);
    }

    public void deletar(Long idReservaEquipamento) {
        ReservaEquipamento reservaEquipamento = buscarPorId(idReservaEquipamento);
        reservaEquipamentoRepository.delete(reservaEquipamento);
    }
}
