package com.spaceroom.repositories;

import com.spaceroom.entities.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findByIdInstituicao(Long idInstituicao);

    List<Equipamento> findByIdEspaco(Long idEspaco);

    Optional<Equipamento> findByPatrimonio(String patrimonio);
}
