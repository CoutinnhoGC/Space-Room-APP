package com.spaceroom.repositories;

import com.spaceroom.entities.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    Optional<Cargo> findByNome(String nome);
}
