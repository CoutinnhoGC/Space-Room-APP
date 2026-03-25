package com.spaceroom.controllers;

import com.spaceroom.facades.ReservaFacade;
import com.spaceroom.models.ReservaModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaFacade reservaFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaModel criar(@RequestBody @Valid ReservaModel model) {
        return reservaFacade.criar(model);
    }

    @GetMapping
    public List<ReservaModel> listarTodas() {
        return reservaFacade.listarTodas();
    }

    @GetMapping("/{idReserva}")
    public ReservaModel buscarPorId(@PathVariable Long idReserva) {
        return reservaFacade.buscarPorId(idReserva);
    }

    @PutMapping("/{idReserva}")
    public ReservaModel atualizar(@PathVariable Long idReserva,
                                  @RequestBody @Valid ReservaModel model) {
        return reservaFacade.atualizar(idReserva, model);
    }

    @DeleteMapping("/{idReserva}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long idReserva) {
        reservaFacade.deletar(idReserva);
    }
}