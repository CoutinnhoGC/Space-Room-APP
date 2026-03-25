package com.spaceroom.repositories;

import com.spaceroom.entities.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findByIdInstituicao(Long idInstituicao);

    List<Projeto> findByCriadoPor(Long criadoPor);
}
