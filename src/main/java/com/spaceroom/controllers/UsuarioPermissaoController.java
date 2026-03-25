package com.spaceroom.controllers;

import com.spaceroom.facades.UsuarioPermissaoFacade;
import com.spaceroom.models.UsuarioPermissaoModel;
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
@RequestMapping("/usuario-permissoes")
@RequiredArgsConstructor
public class UsuarioPermissaoController {

    private final UsuarioPermissaoFacade usuarioPermissaoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioPermissaoModel criar(@RequestBody @Valid UsuarioPermissaoModel model) {
        return usuarioPermissaoFacade.criar(model);
    }

    @GetMapping
    public List<UsuarioPermissaoModel> listarTodos() {
        return usuarioPermissaoFacade.listarTodos();
    }

    @GetMapping("/{idUsuario}/{idPermissao}")
    public UsuarioPermissaoModel buscarPorId(@PathVariable Long idUsuario,
                                             @PathVariable Integer idPermissao) {
        return usuarioPermissaoFacade.buscarPorId(idUsuario, idPermissao);
    }

    @PutMapping("/{idUsuario}/{idPermissao}")
    public UsuarioPermissaoModel atualizar(@PathVariable Long idUsuario,
                                           @PathVariable Integer idPermissao,
                                           @RequestBody @Valid UsuarioPermissaoModel model) {
        return usuarioPermissaoFacade.atualizar(idUsuario, idPermissao, model);
    }

    @DeleteMapping("/{idUsuario}/{idPermissao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idUsuario,
                        @PathVariable Integer idPermissao) {
        usuarioPermissaoFacade.deletar(idUsuario, idPermissao);
    }
}
