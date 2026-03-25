package com.spaceroom.repositories;

import com.spaceroom.entities.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Integer> {

    Optional<Plano> findByNome(String nome);
}
