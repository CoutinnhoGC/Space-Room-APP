package com.spaceroom.applications;

import com.spaceroom.entities.Cargo;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoApplication {

    private final CargoRepository cargoRepository;

    public Cargo criar(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    public List<Cargo> listarTodos() {
        return cargoRepository.findAll();
    }

    public Cargo buscarPorId(Integer idCargo) {
        return cargoRepository.findById(idCargo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cargo nao encontrado para o id: " + idCargo
                ));
    }

    public Cargo atualizar(Integer idCargo, Cargo dadosAtualizados) {
        Cargo cargoExistente = buscarPorId(idCargo);

        cargoExistente.setNome(dadosAtualizados.getNome());
        cargoExistente.setDescricao(dadosAtualizados.getDescricao());
        cargoExistente.setAtivo(dadosAtualizados.getAtivo());

        return cargoRepository.save(cargoExistente);
    }

    public void deletar(Integer idCargo) {
        Cargo cargo = buscarPorId(idCargo);
        cargoRepository.delete(cargo);
    }
}
