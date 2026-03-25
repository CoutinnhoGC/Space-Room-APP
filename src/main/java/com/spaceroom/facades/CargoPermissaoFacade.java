package com.spaceroom.facades;

import com.spaceroom.applications.CargoPermissaoApplication;
import com.spaceroom.entities.CargoPermissao;
import com.spaceroom.models.CargoPermissaoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CargoPermissaoFacade {

    private final CargoPermissaoApplication cargoPermissaoApplication;

    public CargoPermissaoModel criar(CargoPermissaoModel model) {
        CargoPermissao cargoPermissao = converterModelParaEntity(model);
        CargoPermissao cargoPermissaoSalva = cargoPermissaoApplication.criar(cargoPermissao);
        return converterEntityParaModel(cargoPermissaoSalva);
    }

    public List<CargoPermissaoModel> listarTodos() {
        return cargoPermissaoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public CargoPermissaoModel buscarPorId(Integer idCargo, Integer idPermissao) {
        CargoPermissao cargoPermissao = cargoPermissaoApplication.buscarPorId(idCargo, idPermissao);
        return converterEntityParaModel(cargoPermissao);
    }

    public CargoPermissaoModel atualizar(Integer idCargo, Integer idPermissao, CargoPermissaoModel model) {
        CargoPermissao cargoPermissao = converterModelParaEntity(model);
        CargoPermissao cargoPermissaoAtualizada = cargoPermissaoApplication.atualizar(idCargo, idPermissao, cargoPermissao);
        return converterEntityParaModel(cargoPermissaoAtualizada);
    }

    public void deletar(Integer idCargo, Integer idPermissao) {
        cargoPermissaoApplication.deletar(idCargo, idPermissao);
    }

    private CargoPermissao converterModelParaEntity(CargoPermissaoModel model) {
        return CargoPermissao.builder()
                .idCargo(model.getIdCargo())
                .idPermissao(model.getIdPermissao())
                .build();
    }

    private CargoPermissaoModel converterEntityParaModel(CargoPermissao cargoPermissao) {
        CargoPermissaoModel model = new CargoPermissaoModel();
        model.setIdCargo(cargoPermissao.getIdCargo());
        model.setIdPermissao(cargoPermissao.getIdPermissao());
        return model;
    }
}
