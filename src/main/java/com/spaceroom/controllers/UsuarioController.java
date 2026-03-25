package com.spaceroom.controllers;

import com.spaceroom.facades.UsuarioFacade;
import com.spaceroom.models.UsuarioModel;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioFacade usuarioFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel criar(@RequestBody @Valid UsuarioModel model) {
        return usuarioFacade.criar(model);
    }

    @GetMapping
    public List<UsuarioModel> listarTodos() {
        return usuarioFacade.listarTodos();
    }

    @GetMapping("/{idUsuario}")
    public UsuarioModel buscarPorId(@PathVariable Long idUsuario) {
        return usuarioFacade.buscarPorId(idUsuario);
    }

    @PutMapping("/{idUsuario}")
    public UsuarioModel atualizar(@PathVariable Long idUsuario,
                                  @RequestBody @Valid UsuarioModel model) {
        return usuarioFacade.atualizar(idUsuario, model);
    }

    @DeleteMapping("/{idUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idUsuario) {
        usuarioFacade.deletar(idUsuario);
    }
}
