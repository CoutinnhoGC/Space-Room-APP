package com.spaceroom.repositories;

import com.spaceroom.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByIdUsuario(Long idUsuario);

    List<Reserva> findByIdInstituicao(Long idInstituicao);

    List<Reserva> findByIdEspaco(Long idEspaco);

    boolean existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(
            Long idEspaco,
            LocalDateTime dataFim,
            LocalDateTime dataInicio
    );
}