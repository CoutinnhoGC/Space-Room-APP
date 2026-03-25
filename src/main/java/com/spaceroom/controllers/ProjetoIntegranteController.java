package com.spaceroom.controllers;

import com.spaceroom.facades.ProjetoIntegranteFacade;
import com.spaceroom.models.ProjetoIntegranteModel;
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
@RequestMapping("/projeto-integrantes")
@RequiredArgsConstructor
public class ProjetoIntegranteController {

    private final ProjetoIntegranteFacade projetoIntegranteFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjetoIntegranteModel criar(@RequestBody @Valid ProjetoIntegranteModel model) {
        return projetoIntegranteFacade.criar(model);
    }

    @GetMapping
    public List<ProjetoIntegranteModel> listarTodos() {
        return projetoIntegranteFacade.listarTodos();
    }

    @GetMapping("/{idProjetoIntegrante}")
    public ProjetoIntegranteModel buscarPorId(@PathVariable Long idProjetoIntegrante) {
        return projetoIntegranteFacade.buscarPorId(idProjetoIntegrante);
    }

    @PutMapping("/{idProjetoIntegrante}")
    public ProjetoIntegranteModel atualizar(@PathVariable Long idProjetoIntegrante,
                                            @RequestBody @Valid ProjetoIntegranteModel model) {
        return projetoIntegranteFacade.atualizar(idProjetoIntegrante, model);
    }

    @DeleteMapping("/{idProjetoIntegrante}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idProjetoIntegrante) {
        projetoIntegranteFacade.deletar(idProjetoIntegrante);
    }
}
