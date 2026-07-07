package com.spaceroom.applications;

import com.spaceroom.entities.Cargo;
import com.spaceroom.entities.Instituicao;
import com.spaceroom.entities.TipoInstituicao;
import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.CargoRepository;
import com.spaceroom.repositories.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CargoApplication {

    private final CargoRepository cargoRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final AutorizacaoApplication autorizacaoApplication;

    @Autowired
    public CargoApplication(CargoRepository cargoRepository,
                            InstituicaoRepository instituicaoRepository,
                            AutorizacaoApplication autorizacaoApplication) {
        this.cargoRepository = cargoRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.autorizacaoApplication = autorizacaoApplication;
    }

    public CargoApplication(CargoRepository cargoRepository) {
        this(cargoRepository, null, null);
    }

    public Cargo criar(Cargo cargo) {
        validarGerenciamentoCargo(null, cargo);
        if (autorizacaoApplication != null) {
            aplicarDefaults(cargo);
        }
        return cargoRepository.save(cargo);
    }

    public List<Cargo> listarTodos() {
        List<Cargo> cargos = cargoRepository.findAll();
        if (autorizacaoApplication == null) {
            return cargos;
        }

        Usuario usuarioAtual = autorizacaoApplication.obterUsuarioAtualObrigatorio();
        if (autorizacaoApplication.isAdminPlataforma(usuarioAtual)) {
            return cargos;
        }

        TipoInstituicao tipoInstituicao = instituicaoRepository == null
                ? null
                : instituicaoRepository.findById(usuarioAtual.getIdInstituicao())
                .map(Instituicao::getTipo)
                .orElse(null);

        return cargos.stream()
                .filter(cargo -> cargo.getAtivo() == null || cargo.getAtivo())
                .filter(cargo -> !Boolean.TRUE.equals(cargo.getSistema()) || !autorizacaoApplication.cargoConcedeAdminPlataforma(cargo.getIdCargo()))
                .filter(cargo -> cargo.getIdInstituicao() == null || Objects.equals(cargo.getIdInstituicao(), usuarioAtual.getIdInstituicao()))
                .filter(cargo -> cargo.getTipoInstituicao() == null || cargo.getTipoInstituicao().equals(tipoInstituicao))
                .toList();
    }

    public Cargo buscarPorId(Integer idCargo) {
        return cargoRepository.findById(idCargo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cargo não encontrado para o id: " + idCargo
                ));
    }

    public Cargo atualizar(Integer idCargo, Cargo dadosAtualizados) {
        Cargo cargoExistente = buscarPorId(idCargo);
        validarGerenciamentoCargo(cargoExistente, dadosAtualizados);

        cargoExistente.setNome(dadosAtualizados.getNome());
        cargoExistente.setDescricao(dadosAtualizados.getDescricao());
        cargoExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        cargoExistente.setTipoInstituicao(dadosAtualizados.getTipoInstituicao());
        cargoExistente.setSistema(dadosAtualizados.getSistema());
        cargoExistente.setPersonalizado(dadosAtualizados.getPersonalizado());
        cargoExistente.setAtivo(dadosAtualizados.getAtivo());
        if (autorizacaoApplication != null) {
            aplicarDefaults(cargoExistente);
        }

        return cargoRepository.save(cargoExistente);
    }

    public void deletar(Integer idCargo) {
        Cargo cargo = buscarPorId(idCargo);
        validarGerenciamentoCargo(cargo, cargo);
        if (Boolean.TRUE.equals(cargo.getSistema())) {
            throw new BusinessException("Cargos de sistema não podem ser removidos.");
        }
        cargoRepository.delete(cargo);
    }

    private void validarGerenciamentoCargo(Cargo cargoExistente, Cargo dadosSolicitados) {
        if (autorizacaoApplication == null) {
            return;
        }

        Usuario usuarioAtual = autorizacaoApplication.obterUsuarioAtualObrigatorio();
        boolean adminPlataforma = autorizacaoApplication.isAdminPlataforma(usuarioAtual);
        if (!adminPlataforma && !autorizacaoApplication.podeGerenciarUsuarios(usuarioAtual)) {
            throw new BusinessException("Você não possui permissão para gerenciar cargos.");
        }

        boolean cargoAtualAdminPlataforma = cargoExistente != null && autorizacaoApplication.cargoConcedeAdminPlataforma(cargoExistente.getIdCargo());
        if (!adminPlataforma && (cargoAtualAdminPlataforma || Boolean.TRUE.equals(dadosSolicitados.getSistema()))) {
            throw new BusinessException("Somente administradores da plataforma podem gerenciar cargos de sistema.");
        }

        if (!adminPlataforma) {
            dadosSolicitados.setIdInstituicao(usuarioAtual.getIdInstituicao());
            dadosSolicitados.setSistema(false);
            dadosSolicitados.setPersonalizado(true);
        }
    }

    private void aplicarDefaults(Cargo cargo) {
        if (cargo.getAtivo() == null) {
            cargo.setAtivo(true);
        }
        if (cargo.getSistema() == null) {
            cargo.setSistema(false);
        }
        if (cargo.getPersonalizado() == null) {
            cargo.setPersonalizado(!Boolean.TRUE.equals(cargo.getSistema()));
        }
    }
}
