package com.spaceroom.services;

import com.spaceroom.entities.Reserva;
import com.spaceroom.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository repository;

    public Reserva salvar(Reserva reserva) {
        return repository.save(reserva);
    }

    public List<Reserva> listar() {
        return repository.findAll();
    }
}