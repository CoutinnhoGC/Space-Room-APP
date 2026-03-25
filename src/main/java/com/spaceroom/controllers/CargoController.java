package com.spaceroom.controllers;

import com.spaceroom.facades.CargoFacade;
import com.spaceroom.models.CargoModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cargos")
@RequiredArgsConstructor
public class CargoController {

    private final CargoFacade cargoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CargoModel criar(@RequestBody @Valid CargoModel model) {
        return cargoFacade.criar(model);
    }

    @GetMapping
    public List<CargoModel> listarTodos() {
        return cargoFacade.listarTodos();
    }

    @GetMapping("/{idCargo}")
    public CargoModel buscarPorId(@PathVariable Integer idCargo) {
        return cargoFacade.buscarPorId(idCargo);
    }

    @PutMapping("/{idCargo}")
    public CargoModel atualizar(@PathVariable Integer idCargo,
                                @RequestBody @Valid CargoModel model) {
        return cargoFacade.atualizar(idCargo, model);
    }

    @DeleteMapping("/{idCargo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer idCargo) {
        cargoFacade.deletar(idCargo);
    }
}
