package com.spaceroom.applications;

import com.spaceroom.config.PermissionCodes;
import com.spaceroom.entities.CargoPermissao;
import com.spaceroom.entities.Espaco;
import com.spaceroom.entities.Permissao;
import com.spaceroom.entities.Reserva;
import com.spaceroom.entities.Usuario;
import com.spaceroom.entities.UsuarioPermissao;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.repositories.CargoPermissaoRepository;
import com.spaceroom.repositories.PermissaoRepository;
import com.spaceroom.repositories.UsuarioPermissaoRepository;
import com.spaceroom.repositories.UsuarioRepository;
import com.spaceroom.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutorizacaoApplication {

    private final UsuarioRepository usuarioRepository;
    private final CargoPermissaoRepository cargoPermissaoRepository;
    private final PermissaoRepository permissaoRepository;
    private final UsuarioPermissaoRepository usuarioPermissaoRepository;

    public Usuario obterUsuarioAtualObrigatorio() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser principal)) {
            throw new BusinessException("Usuario autenticado e obrigatorio para esta operacao.");
        }

        return usuarioRepository.findById(principal.idUsuario())
                .orElseThrow(() -> new BusinessException("Usuario autenticado nao encontrado."));
    }

    public boolean isAdminPlataforma(Usuario usuario) {
        return possuiPermissao(usuario, PermissionCodes.GERENCIAR_INSTITUICOES);
    }

    public boolean podeReservar(Usuario usuario) {
        return possuiPermissao(usuario, PermissionCodes.RESERVAR_ESPACO);
    }

    public boolean podeGerenciarUsuarios(Usuario usuario) {
        return possuiPermissao(usuario, PermissionCodes.GERENCIAR_USUARIOS);
    }

    public boolean podeGerenciarEspacos(Usuario usuario) {
        return possuiPermissao(usuario, PermissionCodes.GERENCIAR_ESPACOS);
    }

    public boolean podeAprovarReservas(Usuario usuario) {
        return possuiPermissao(usuario, PermissionCodes.APROVAR_RESERVAS);
    }

    public boolean podeGerenciarComunicados(Usuario usuario) {
        return possuiPermissao(usuario, PermissionCodes.GERENCIAR_COMUNICADOS);
    }

    public boolean podeVisualizarAuditoria(Usuario usuario) {
        return possuiPermissao(usuario, PermissionCodes.VISUALIZAR_AUDITORIA);
    }

    public boolean podeAprovarReservaNoEspaco(Usuario usuario, Espaco espaco) {
        if (usuario == null) {
            return false;
        }

        return isAdminPlataforma(usuario) || podeAprovarReservas(usuario);
    }

    public void validarCriacaoInstituicao() {
        Usuario usuarioAtual = obterUsuarioAtualObrigatorio();
        if (!isAdminPlataforma(usuarioAtual)) {
            throw new BusinessException("Somente os administradores da plataforma podem cadastrar instituicoes.");
        }
    }

    public void validarAcessoInstituicao(Long idInstituicao) {
        Usuario usuarioAtual = obterUsuarioAtualObrigatorio();
        if (isAdminPlataforma(usuarioAtual)) {
            return;
        }

        if (!usuarioAtual.getIdInstituicao().equals(idInstituicao)) {
            throw new BusinessException("Voce so pode acessar dados da sua propria instituicao.");
        }
    }

    public void validarAcessoUsuario(Usuario usuarioAlvo) {
        validarAcessoInstituicao(usuarioAlvo.getIdInstituicao());
    }

    public void validarAcessoEspaco(Espaco espaco) {
        validarAcessoInstituicao(espaco.getIdInstituicao());
    }

    public void validarAcessoReserva(Reserva reserva) {
        Usuario usuarioAtual = obterUsuarioAtualObrigatorio();
        if (isAdminPlataforma(usuarioAtual)) {
            return;
        }

        if (!usuarioAtual.getIdInstituicao().equals(reserva.getIdInstituicao())) {
            throw new BusinessException("Voce so pode acessar reservas da sua propria instituicao.");
        }

        if (!usuarioAtual.getIdUsuario().equals(reserva.getIdUsuario()) && !podeReservar(usuarioAtual)) {
            throw new BusinessException("Voce nao possui permissao para acessar esta reserva.");
        }
    }

    public void validarCriacaoOuEdicaoReserva(Reserva reserva) {
        Usuario usuarioAtual = obterUsuarioAtualObrigatorio();

        if (!usuarioAtual.getIdInstituicao().equals(reserva.getIdInstituicao())) {
            throw new BusinessException("Voce so pode reservar espacos da sua propria instituicao.");
        }

        if (!podeReservar(usuarioAtual)) {
            throw new BusinessException("Seu usuario nao possui permissao para criar reservas.");
        }

        boolean adminPlataforma = isAdminPlataforma(usuarioAtual);
        if (!adminPlataforma && !usuarioAtual.getIdUsuario().equals(reserva.getIdUsuario())) {
            throw new BusinessException("Voce so pode criar reservas em seu proprio nome.");
        }
    }

    public boolean resolverPodeReservar(Usuario usuario, Boolean valorSolicitado) {
        return valorSolicitado != null ? valorSolicitado : podeReservar(usuario);
    }

    public boolean resolverAdminPlataforma(Usuario usuario) {
        return isAdminPlataforma(usuario);
    }

    public void sincronizarPermissaoReserva(Usuario usuario, Boolean valorSolicitado) {
        if (valorSolicitado == null) {
            return;
        }

        boolean padraoCargo = buscarPermissaoCargo(usuario.getIdCargo(), PermissionCodes.RESERVAR_ESPACO)
                .orElse(false);
        sincronizarPermissaoUsuario(usuario.getIdUsuario(), PermissionCodes.RESERVAR_ESPACO, valorSolicitado, padraoCargo);
    }

    private boolean possuiPermissao(Usuario usuario, String nomePermissao) {
        Optional<Boolean> permissaoExplicita = buscarPermissaoUsuario(usuario.getIdUsuario(), nomePermissao);
        if (permissaoExplicita.isPresent()) {
            return permissaoExplicita.get();
        }

        return buscarPermissaoCargo(usuario.getIdCargo(), nomePermissao).orElse(false);
    }

    private Optional<Boolean> buscarPermissaoUsuario(Long idUsuario, String nomePermissao) {
        Optional<Permissao> permissao = permissaoRepository.findByNome(nomePermissao);
        if (permissao.isEmpty()) {
            return Optional.empty();
        }

        List<UsuarioPermissao> permissoes = usuarioPermissaoRepository.findByIdUsuario(idUsuario);
        return permissoes.stream()
                .filter(item -> item.getIdPermissao().equals(permissao.get().getIdPermissao()))
                .map(UsuarioPermissao::getConcedida)
                .findFirst();
    }

    private Optional<Boolean> buscarPermissaoCargo(Integer idCargo, String nomePermissao) {
        if (idCargo == null) {
            return Optional.empty();
        }

        Optional<Permissao> permissao = permissaoRepository.findByNome(nomePermissao);
        if (permissao.isEmpty()) {
            return Optional.empty();
        }

        List<CargoPermissao> permissoes = cargoPermissaoRepository.findByIdCargo(idCargo);
        return permissoes.stream()
                .anyMatch(item -> item.getIdPermissao().equals(permissao.get().getIdPermissao()))
                ? Optional.of(true)
                : Optional.empty();
    }

    private void sincronizarPermissaoUsuario(Long idUsuario, String nomePermissao, boolean valorSolicitado, boolean valorPadrao) {
        Permissao permissao = permissaoRepository.findByNome(nomePermissao)
                .orElseThrow(() -> new BusinessException("Permissao obrigatoria nao encontrada: " + nomePermissao));

        Optional<UsuarioPermissao> permissaoExistente = usuarioPermissaoRepository.findByIdUsuario(idUsuario)
                .stream()
                .filter(item -> item.getIdPermissao().equals(permissao.getIdPermissao()))
                .findFirst();

        if (valorSolicitado == valorPadrao) {
            permissaoExistente.ifPresent(usuarioPermissaoRepository::delete);
            return;
        }

        UsuarioPermissao usuarioPermissao = permissaoExistente.orElseGet(() -> UsuarioPermissao.builder()
                .idUsuario(idUsuario)
                .idPermissao(permissao.getIdPermissao())
                .build());
        usuarioPermissao.setConcedida(valorSolicitado);
        usuarioPermissaoRepository.save(usuarioPermissao);
    }
}
