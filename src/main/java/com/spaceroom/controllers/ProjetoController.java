package com.spaceroom.controllers;

import com.spaceroom.facades.ProjetoFacade;
import com.spaceroom.models.ProjetoModel;
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
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoFacade projetoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjetoModel criar(@RequestBody @Valid ProjetoModel model) {
        return projetoFacade.criar(model);
    }

    @GetMapping
    public List<ProjetoModel> listarTodos() {
        return projetoFacade.listarTodos();
    }

    @GetMapping("/{idProjeto}")
    public ProjetoModel buscarPorId(@PathVariable Long idProjeto) {
        return projetoFacade.buscarPorId(idProjeto);
    }

    @PutMapping("/{idProjeto}")
    public ProjetoModel atualizar(@PathVariable Long idProjeto,
                                  @RequestBody @Valid ProjetoModel model) {
        return projetoFacade.atualizar(idProjeto, model);
    }

    @DeleteMapping("/{idProjeto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idProjeto) {
        projetoFacade.deletar(idProjeto);
    }
}
