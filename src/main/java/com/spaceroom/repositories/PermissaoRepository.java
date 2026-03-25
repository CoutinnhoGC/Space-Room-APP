package com.spaceroom.repositories;

import com.spaceroom.entities.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {

    Optional<Permissao> findByNome(String nome);
}
