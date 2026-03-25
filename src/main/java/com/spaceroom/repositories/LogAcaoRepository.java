package com.spaceroom.repositories;

import com.spaceroom.entities.LogAcao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogAcaoRepository extends JpaRepository<LogAcao, Long> {

    List<LogAcao> findByIdUsuario(Long idUsuario);
}
