package com.spaceroom.controllers;

import com.spaceroom.facades.InstituicaoFacade;
import com.spaceroom.models.InstituicaoModel;
import com.spaceroom.models.InstituicaoResumoModel;
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
@RequestMapping("/instituicoes")
@RequiredArgsConstructor
public class InstituicaoController {

    private final InstituicaoFacade instituicaoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstituicaoModel criar(@RequestBody @Valid InstituicaoModel model) {
        return instituicaoFacade.criar(model);
    }

    @GetMapping
    public List<InstituicaoModel> listarTodas() {
        return instituicaoFacade.listarTodas();
    }

    @GetMapping("/resumo")
    public List<InstituicaoResumoModel> listarResumo() {
        return instituicaoFacade.listarResumo();
    }

    @GetMapping("/{idInstituicao}")
    public InstituicaoModel buscarPorId(@PathVariable Long idInstituicao) {
        return instituicaoFacade.buscarPorId(idInstituicao);
    }

    @PutMapping("/{idInstituicao}")
    public InstituicaoModel atualizar(@PathVariable Long idInstituicao,
                                      @RequestBody @Valid InstituicaoModel model) {
        return instituicaoFacade.atualizar(idInstituicao, model);
    }

    @DeleteMapping("/{idInstituicao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idInstituicao) {
        instituicaoFacade.deletar(idInstituicao);
    }
}
