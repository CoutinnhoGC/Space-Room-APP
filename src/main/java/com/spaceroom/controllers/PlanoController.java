package com.spaceroom.controllers;

import com.spaceroom.facades.PlanoFacade;
import com.spaceroom.models.PlanoModel;
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
@RequestMapping("/planos")
@RequiredArgsConstructor
public class PlanoController {

    private final PlanoFacade planoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanoModel criar(@RequestBody @Valid PlanoModel model) {
        return planoFacade.criar(model);
    }

    @GetMapping
    public List<PlanoModel> listarTodos() {
        return planoFacade.listarTodos();
    }

    @GetMapping("/{idPlano}")
    public PlanoModel buscarPorId(@PathVariable Integer idPlano) {
        return planoFacade.buscarPorId(idPlano);
    }

    @PutMapping("/{idPlano}")
    public PlanoModel atualizar(@PathVariable Integer idPlano,
                                @RequestBody @Valid PlanoModel model) {
        return planoFacade.atualizar(idPlano, model);
    }

    @DeleteMapping("/{idPlano}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer idPlano) {
        planoFacade.deletar(idPlano);
    }
}
