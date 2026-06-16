package com.spaceroom.applications;

import com.spaceroom.config.PermissionCodes;
import com.spaceroom.entities.Cargo;
import com.spaceroom.entities.Espaco;
import com.spaceroom.entities.Instituicao;
import com.spaceroom.entities.Permissao;
import com.spaceroom.entities.Reserva;
import com.spaceroom.entities.TipoInstituicao;
import com.spaceroom.entities.Usuario;
import com.spaceroom.entities.UsuarioPermissao;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.repositories.CargoRepository;
import com.spaceroom.repositories.InstituicaoRepository;
import com.spaceroom.repositories.PermissaoRepository;
import com.spaceroom.repositories.UsuarioPermissaoRepository;
import com.spaceroom.repositories.UsuarioRepository;
import com.spaceroom.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AutorizacaoApplication {

    private static final Set<String> CARGOS_ESCOLA_COM_RESERVA = Set.of(
            "diretor",
            "diretora",
            "vice diretor",
            "vice diretora",
            "vice-diretor",
            "vice-diretora",
            "docente",
            "professor",
            "professora",
            "coordenador",
            "coordenadora"
    );

    private static final Set<String> CARGOS_EMPRESA_COM_RESERVA = Set.of(
            "dono",
            "dona",
            "proprietario",
            "proprietaria",
            "socio",
            "socia",
            "gerente",
            "gestor",
            "gestora",
            "administrador",
            "administradora"
    );

    private final UsuarioRepository usuarioRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final CargoRepository cargoRepository;
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
        return buscarPermissaoUsuario(usuario.getIdUsuario(), PermissionCodes.GERENCIAR_INSTITUICOES)
                .orElse(false);
    }

    public boolean podeReservar(Usuario usuario) {
        Optional<Boolean> permissaoExplicita = buscarPermissaoUsuario(usuario.getIdUsuario(), PermissionCodes.RESERVAR_ESPACO);
        if (permissaoExplicita.isPresent()) {
            return permissaoExplicita.get();
        }

        return calcularPermissaoPadraoReserva(usuario);
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

        boolean padraoCargo = calcularPermissaoPadraoReserva(usuario);
        sincronizarPermissaoUsuario(usuario.getIdUsuario(), PermissionCodes.RESERVAR_ESPACO, valorSolicitado, padraoCargo);
    }

    private boolean calcularPermissaoPadraoReserva(Usuario usuario) {
        Instituicao instituicao = instituicaoRepository.findById(usuario.getIdInstituicao()).orElse(null);
        Cargo cargo = cargoRepository.findById(usuario.getIdCargo()).orElse(null);

        if (instituicao == null || cargo == null || cargo.getNome() == null) {
            return false;
        }

        String nomeCargoNormalizado = normalizar(cargo.getNome());
        Set<String> cargosPermitidos = usaRegrasEscolares(instituicao.getTipo())
                ? CARGOS_ESCOLA_COM_RESERVA
                : CARGOS_EMPRESA_COM_RESERVA;

        return cargosPermitidos.contains(nomeCargoNormalizado);
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

    private boolean usaRegrasEscolares(TipoInstituicao tipoInstituicao) {
        return tipoInstituicao == TipoInstituicao.ESCOLA
                || tipoInstituicao == TipoInstituicao.FACULDADE
                || tipoInstituicao == TipoInstituicao.UNIVERSIDADE
                || tipoInstituicao == TipoInstituicao.SENAI;
    }

    private String normalizar(String valor) {
        String semAcento = Normalizer.normalize(valor, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");
    }
}
