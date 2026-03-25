package com.spaceroom.repositories;

import com.spaceroom.entities.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspacoRepository extends JpaRepository<Espaco, Long> {

    List<Espaco> findByIdInstituicao(Long idInstituicao);
}
