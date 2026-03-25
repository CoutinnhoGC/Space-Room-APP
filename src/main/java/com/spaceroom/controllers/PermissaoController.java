package com.spaceroom.controllers;

import com.spaceroom.facades.PermissaoFacade;
import com.spaceroom.models.PermissaoModel;
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
@RequestMapping("/permissoes")
@RequiredArgsConstructor
public class PermissaoController {

    private final PermissaoFacade permissaoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PermissaoModel criar(@RequestBody @Valid PermissaoModel model) {
        return permissaoFacade.criar(model);
    }

    @GetMapping
    public List<PermissaoModel> listarTodas() {
        return permissaoFacade.listarTodas();
    }

    @GetMapping("/{idPermissao}")
    public PermissaoModel buscarPorId(@PathVariable Integer idPermissao) {
        return permissaoFacade.buscarPorId(idPermissao);
    }

    @PutMapping("/{idPermissao}")
    public PermissaoModel atualizar(@PathVariable Integer idPermissao,
                                    @RequestBody @Valid PermissaoModel model) {
        return permissaoFacade.atualizar(idPermissao, model);
    }

    @DeleteMapping("/{idPermissao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer idPermissao) {
        permissaoFacade.deletar(idPermissao);
    }
}
