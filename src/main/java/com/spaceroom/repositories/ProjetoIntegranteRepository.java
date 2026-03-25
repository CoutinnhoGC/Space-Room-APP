package com.spaceroom.repositories;

import com.spaceroom.entities.ProjetoIntegrante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetoIntegranteRepository extends JpaRepository<ProjetoIntegrante, Long> {

    List<ProjetoIntegrante> findByIdProjeto(Long idProjeto);

    List<ProjetoIntegrante> findByIdUsuario(Long idUsuario);
}
