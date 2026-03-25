package com.spaceroom.controllers;

import com.spaceroom.facades.ReservaEquipamentoFacade;
import com.spaceroom.models.ReservaEquipamentoModel;
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
@RequestMapping("/reserva-equipamentos")
@RequiredArgsConstructor
public class ReservaEquipamentoController {

    private final ReservaEquipamentoFacade reservaEquipamentoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaEquipamentoModel criar(@RequestBody @Valid ReservaEquipamentoModel model) {
        return reservaEquipamentoFacade.criar(model);
    }

    @GetMapping
    public List<ReservaEquipamentoModel> listarTodos() {
        return reservaEquipamentoFacade.listarTodos();
    }

    @GetMapping("/{idReservaEquipamento}")
    public ReservaEquipamentoModel buscarPorId(@PathVariable Long idReservaEquipamento) {
        return reservaEquipamentoFacade.buscarPorId(idReservaEquipamento);
    }

    @PutMapping("/{idReservaEquipamento}")
    public ReservaEquipamentoModel atualizar(@PathVariable Long idReservaEquipamento,
                                             @RequestBody @Valid ReservaEquipamentoModel model) {
        return reservaEquipamentoFacade.atualizar(idReservaEquipamento, model);
    }

    @DeleteMapping("/{idReservaEquipamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idReservaEquipamento) {
        reservaEquipamentoFacade.deletar(idReservaEquipamento);
    }
}
