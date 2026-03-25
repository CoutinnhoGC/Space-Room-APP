package com.spaceroom.controllers;

import com.spaceroom.facades.LogAcaoFacade;
import com.spaceroom.models.LogAcaoModel;
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
@RequestMapping("/logs-acao")
@RequiredArgsConstructor
public class LogAcaoController {

    private final LogAcaoFacade logAcaoFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogAcaoModel criar(@RequestBody @Valid LogAcaoModel model) {
        return logAcaoFacade.criar(model);
    }

    @GetMapping
    public List<LogAcaoModel> listarTodos() {
        return logAcaoFacade.listarTodos();
    }

    @GetMapping("/{idLog}")
    public LogAcaoModel buscarPorId(@PathVariable Long idLog) {
        return logAcaoFacade.buscarPorId(idLog);
    }

    @PutMapping("/{idLog}")
    public LogAcaoModel atualizar(@PathVariable Long idLog,
                                  @RequestBody @Valid LogAcaoModel model) {
        return logAcaoFacade.atualizar(idLog, model);
    }

    @DeleteMapping("/{idLog}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idLog) {
        logAcaoFacade.deletar(idLog);
    }
}
