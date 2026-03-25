package com.spaceroom.repositories;

import com.spaceroom.entities.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

    List<Instituicao> findByIdPlano(Integer idPlano);

    Optional<Instituicao> findByCnpj(String cnpj);
}
