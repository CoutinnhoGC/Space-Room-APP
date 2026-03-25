package com.spaceroom.repositories;

import com.spaceroom.entities.ReservaEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaEquipamentoRepository extends JpaRepository<ReservaEquipamento, Long> {

    List<ReservaEquipamento> findByIdReserva(Long idReserva);

    List<ReservaEquipamento> findByIdEquipamento(Long idEquipamento);
}
