package com.spaceroom.controllers;

import com.spaceroom.facades.HorarioFuncionamentoEspacoFacade;
import com.spaceroom.models.HorarioFuncionamentoEspacoModel;
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
@RequestMapping("/horarios-funcionamento-espaco")
@RequiredArgsConstructor
public class HorarioFuncionamentoEspacoController {

    private final HorarioFuncionamentoEspacoFacade horarioFuncionamentoEspacoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HorarioFuncionamentoEspacoModel criar(@RequestBody @Valid HorarioFuncionamentoEspacoModel model) {
        return horarioFuncionamentoEspacoFacade.criar(model);
    }

    @GetMapping
    public List<HorarioFuncionamentoEspacoModel> listarTodos() {
        return horarioFuncionamentoEspacoFacade.listarTodos();
    }

    @GetMapping("/{idHorario}")
    public HorarioFuncionamentoEspacoModel buscarPorId(@PathVariable Long idHorario) {
        return horarioFuncionamentoEspacoFacade.buscarPorId(idHorario);
    }

    @PutMapping("/{idHorario}")
    public HorarioFuncionamentoEspacoModel atualizar(@PathVariable Long idHorario,
                                                     @RequestBody @Valid HorarioFuncionamentoEspacoModel model) {
        return horarioFuncionamentoEspacoFacade.atualizar(idHorario, model);
    }

    @DeleteMapping("/{idHorario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idHorario) {
        horarioFuncionamentoEspacoFacade.deletar(idHorario);
    }
}
