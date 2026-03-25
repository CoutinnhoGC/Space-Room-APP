package com.spaceroom.repositories;

import com.spaceroom.entities.CargoPermissao;
import com.spaceroom.entities.CargoPermissaoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargoPermissaoRepository extends JpaRepository<CargoPermissao, CargoPermissaoId> {

    List<CargoPermissao> findByIdCargo(Integer idCargo);

    List<CargoPermissao> findByIdPermissao(Integer idPermissao);
}
