package com.spaceroom.applications;

import com.spaceroom.entities.CargoPermissao;
import com.spaceroom.entities.CargoPermissaoId;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.CargoPermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoPermissaoApplication {

    private final CargoPermissaoRepository cargoPermissaoRepository;

    public CargoPermissao criar(CargoPermissao cargoPermissao) {
        return cargoPermissaoRepository.save(cargoPermissao);
    }

    public List<CargoPermissao> listarTodos() {
        return cargoPermissaoRepository.findAll();
    }

    public CargoPermissao buscarPorId(Integer idCargo, Integer idPermissao) {
        CargoPermissaoId id = new CargoPermissaoId(idCargo, idPermissao);
        return cargoPermissaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CargoPermissão não encontrada para idCargo: " + idCargo + " e idPermissao: " + idPermissao
                ));
    }

    public CargoPermissao atualizar(Integer idCargo, Integer idPermissao, CargoPermissao dadosAtualizados) {
        CargoPermissao cargoPermissaoExistente = buscarPorId(idCargo, idPermissao);

        cargoPermissaoExistente.setIdCargo(dadosAtualizados.getIdCargo());
        cargoPermissaoExistente.setIdPermissao(dadosAtualizados.getIdPermissao());

        return cargoPermissaoRepository.save(cargoPermissaoExistente);
    }

    public void deletar(Integer idCargo, Integer idPermissao) {
        CargoPermissao cargoPermissao = buscarPorId(idCargo, idPermissao);
        cargoPermissaoRepository.delete(cargoPermissao);
    }
}
