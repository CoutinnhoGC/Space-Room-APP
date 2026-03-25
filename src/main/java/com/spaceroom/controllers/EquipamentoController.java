package com.spaceroom.controllers;

import com.spaceroom.facades.EquipamentoFacade;
import com.spaceroom.models.EquipamentoModel;
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
@RequestMapping("/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoFacade equipamentoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipamentoModel criar(@RequestBody @Valid EquipamentoModel model) {
        return equipamentoFacade.criar(model);
    }

    @GetMapping
    public List<EquipamentoModel> listarTodos() {
        return equipamentoFacade.listarTodos();
    }

    @GetMapping("/{idEquipamento}")
    public EquipamentoModel buscarPorId(@PathVariable Long idEquipamento) {
        return equipamentoFacade.buscarPorId(idEquipamento);
    }

    @PutMapping("/{idEquipamento}")
    public EquipamentoModel atualizar(@PathVariable Long idEquipamento,
                                      @RequestBody @Valid EquipamentoModel model) {
        return equipamentoFacade.atualizar(idEquipamento, model);
    }

    @DeleteMapping("/{idEquipamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idEquipamento) {
        equipamentoFacade.deletar(idEquipamento);
    }
}
