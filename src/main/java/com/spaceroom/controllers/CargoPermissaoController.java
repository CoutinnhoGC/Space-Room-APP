package com.spaceroom.controllers;

import com.spaceroom.facades.CargoPermissaoFacade;
import com.spaceroom.models.CargoPermissaoModel;
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
@RequestMapping("/cargo-permissoes")
@RequiredArgsConstructor
public class CargoPermissaoController {

    private final CargoPermissaoFacade cargoPermissaoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CargoPermissaoModel criar(@RequestBody @Valid CargoPermissaoModel model) {
        return cargoPermissaoFacade.criar(model);
    }

    @GetMapping
    public List<CargoPermissaoModel> listarTodos() {
        return cargoPermissaoFacade.listarTodos();
    }

    @GetMapping("/{idCargo}/{idPermissao}")
    public CargoPermissaoModel buscarPorId(@PathVariable Integer idCargo,
                                           @PathVariable Integer idPermissao) {
        return cargoPermissaoFacade.buscarPorId(idCargo, idPermissao);
    }

    @PutMapping("/{idCargo}/{idPermissao}")
    public CargoPermissaoModel atualizar(@PathVariable Integer idCargo,
                                         @PathVariable Integer idPermissao,
                                         @RequestBody @Valid CargoPermissaoModel model) {
        return cargoPermissaoFacade.atualizar(idCargo, idPermissao, model);
    }

    @DeleteMapping("/{idCargo}/{idPermissao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer idCargo,
                        @PathVariable Integer idPermissao) {
        cargoPermissaoFacade.deletar(idCargo, idPermissao);
    }
}
