package com.spaceroom.controllers;

import com.spaceroom.facades.EspacoFacade;
import com.spaceroom.models.EspacoModel;
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
@RequestMapping("/espacos")
@RequiredArgsConstructor
public class EspacoController {

    private final EspacoFacade espacoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EspacoModel criar(@RequestBody @Valid EspacoModel model) {
        return espacoFacade.criar(model);
    }

    @GetMapping
    public List<EspacoModel> listarTodos() {
        return espacoFacade.listarTodos();
    }

    @GetMapping("/{idEspaco}")
    public EspacoModel buscarPorId(@PathVariable Long idEspaco) {
        return espacoFacade.buscarPorId(idEspaco);
    }

    @PutMapping("/{idEspaco}")
    public EspacoModel atualizar(@PathVariable Long idEspaco,
                                 @RequestBody @Valid EspacoModel model) {
        return espacoFacade.atualizar(idEspaco, model);
    }

    @DeleteMapping("/{idEspaco}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idEspaco) {
        espacoFacade.deletar(idEspaco);
    }
}
