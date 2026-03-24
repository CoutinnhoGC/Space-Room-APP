package com.spaceroom.controllers;

import com.spaceroom.entities.Reserva;
import com.spaceroom.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @PostMapping
    public Reserva criar(@RequestBody Reserva reserva) {
        return service.salvar(reserva);
    }

    @GetMapping
    public List<Reserva> listar() {
        return service.listar();
    }
}