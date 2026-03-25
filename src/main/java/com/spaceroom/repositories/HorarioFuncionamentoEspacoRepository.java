package com.spaceroom.repositories;

import com.spaceroom.entities.HorarioFuncionamentoEspaco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioFuncionamentoEspacoRepository extends JpaRepository<HorarioFuncionamentoEspaco, Long> {

    List<HorarioFuncionamentoEspaco> findByIdEspaco(Long idEspaco);
}
