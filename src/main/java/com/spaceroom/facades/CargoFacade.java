package com.spaceroom.facades;

import com.spaceroom.applications.CargoApplication;
import com.spaceroom.entities.Cargo;
import com.spaceroom.models.CargoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CargoFacade {

    private final CargoApplication cargoApplication;

    public CargoModel criar(CargoModel model) {
        Cargo cargo = converterModelParaEntity(model);
        Cargo cargoSalvo = cargoApplication.criar(cargo);
        return converterEntityParaModel(cargoSalvo);
    }

    public List<CargoModel> listarTodos() {
        return cargoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public CargoModel buscarPorId(Integer idCargo) {
        Cargo cargo = cargoApplication.buscarPorId(idCargo);
        return converterEntityParaModel(cargo);
    }

    public CargoModel atualizar(Integer idCargo, CargoModel model) {
        Cargo cargo = converterModelParaEntity(model);
        Cargo cargoAtualizado = cargoApplication.atualizar(idCargo, cargo);
        return converterEntityParaModel(cargoAtualizado);
    }

    public void deletar(Integer idCargo) {
        cargoApplication.deletar(idCargo);
    }

    private Cargo converterModelParaEntity(CargoModel model) {
        return Cargo.builder()
                .idCargo(model.getIdCargo())
                .nome(model.getNome())
                .descricao(model.getDescricao())
                .idInstituicao(model.getIdInstituicao())
                .tipoInstituicao(model.getTipoInstituicao())
                .sistema(model.getSistema())
                .personalizado(model.getPersonalizado())
                .ativo(model.getAtivo())
                .build();
    }

    private CargoModel converterEntityParaModel(Cargo cargo) {
        CargoModel model = new CargoModel();
        model.setIdCargo(cargo.getIdCargo());
        model.setNome(cargo.getNome());
        model.setDescricao(cargo.getDescricao());
        model.setIdInstituicao(cargo.getIdInstituicao());
        model.setTipoInstituicao(cargo.getTipoInstituicao());
        model.setSistema(cargo.getSistema());
        model.setPersonalizado(cargo.getPersonalizado());
        model.setAtivo(cargo.getAtivo());
        return model;
    }
}
