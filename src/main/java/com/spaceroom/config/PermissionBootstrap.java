package com.spaceroom.config;

import com.spaceroom.entities.Cargo;
import com.spaceroom.entities.CargoPermissao;
import com.spaceroom.entities.Permissao;
import com.spaceroom.entities.TipoInstituicao;
import com.spaceroom.repositories.CargoPermissaoRepository;
import com.spaceroom.repositories.CargoRepository;
import com.spaceroom.repositories.PermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class PermissionBootstrap {

    private static final Set<String> CARGOS_GESTAO_ESCOLAR = Set.of(
            "diretor",
            "diretora",
            "vice diretor",
            "vice diretora",
            "vice-diretor",
            "vice-diretora",
            "coordenador",
            "coordenadora"
    );

    private static final Set<String> CARGOS_GESTAO_CORPORATIVA = Set.of(
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

    private static final Set<String> CARGOS_OPERACIONAIS_COM_RESERVA = Set.of(
            "docente",
            "professor",
            "professora"
    );

    private static final Set<String> CARGOS_ADMIN_PLATAFORMA = Set.of(
            "administrador da plataforma",
            "admin plataforma",
            "super admin"
    );

    private static final Set<String> CARGOS_ADMIN_INSTITUICAO = Set.of(
            "administrador da instituicao",
            "admin instituicao",
            "proprietario da instituicao"
    );

    private static final Map<TipoInstituicao, List<String>> CARGOS_PADRAO_POR_TIPO = Map.of(
            TipoInstituicao.ESCOLA, List.of("Diretor", "Vice-diretor", "Coordenador", "Professor", "Aluno"),
            TipoInstituicao.FACULDADE, List.of("Diretor", "Vice-diretor", "Coordenador", "Professor", "Aluno"),
            TipoInstituicao.UNIVERSIDADE, List.of("Diretor", "Vice-diretor", "Coordenador", "Professor", "Aluno"),
            TipoInstituicao.SENAI, List.of("Diretor", "Vice-diretor", "Coordenador", "Professor", "Aluno"),
            TipoInstituicao.EMPRESA, List.of("CEO", "Diretor", "Gerente", "Supervisor", "Colaborador"),
            TipoInstituicao.COWORKING, List.of("Gestor", "Recepcionista", "Membro"),
            TipoInstituicao.OUTRO, List.of("Coordenador", "Pesquisador", "Tecnico")
    );

    private final PermissaoRepository permissaoRepository;
    private final CargoRepository cargoRepository;
    private final CargoPermissaoRepository cargoPermissaoRepository;

    @Bean
    public ApplicationRunner seedDefaultPermissions() {
        return args -> {
            criarSeNaoExistir(PermissionCodes.GERENCIAR_INSTITUICOES,
                    "Permite cadastrar e administrar instituicoes da plataforma.");
            criarSeNaoExistir(PermissionCodes.GERENCIAR_USUARIOS,
                    "Permite cadastrar, editar e desativar usuarios da instituicao.");
            criarSeNaoExistir(PermissionCodes.GERENCIAR_ESPACOS,
                    "Permite cadastrar e manter espacos, subespacos e suas regras operacionais.");
            criarSeNaoExistir(PermissionCodes.RESERVAR_ESPACO,
                    "Permite criar e alterar reservas de espacos.");
            criarSeNaoExistir(PermissionCodes.APROVAR_RESERVAS,
                    "Permite aprovar ou reprovar reservas pendentes de validacao.");
            criarSeNaoExistir(PermissionCodes.GERENCIAR_COMUNICADOS,
                    "Permite publicar avisos, murais e notificacoes gerenciais.");
            criarSeNaoExistir(PermissionCodes.VISUALIZAR_AUDITORIA,
                    "Permite consultar trilhas de auditoria e logs administrativos.");
            criarSeNaoExistir(PermissionCodes.GERENCIAR_PLANOS,
                    "Permite administrar planos, limites e modulos comerciais da plataforma.");

            sincronizarCargosPadrao();
            sincronizarPermissoesPadraoPorCargo();
        };
    }

    private void sincronizarCargosPadrao() {
        criarCargoSistemaSeNaoExistir("Administrador da Plataforma", null,
                "Cargo global de sistema com acesso a todas as instituicoes, usuarios, planos e configuracoes.");
        criarCargoSistemaSeNaoExistir("Administrador da Instituicao", null,
                "Cargo institucional de sistema com gestao completa limitada a propria instituicao.");

        CARGOS_PADRAO_POR_TIPO.forEach((tipo, cargos) -> cargos.forEach(nome ->
                criarCargoSistemaSeNaoExistir(nome, tipo, "Cargo padrao para instituicoes do tipo " + tipo.name() + ".")));
    }

    private void criarCargoSistemaSeNaoExistir(String nome, TipoInstituicao tipoInstituicao, String descricao) {
        cargoRepository.findByNome(nome)
                .map(cargo -> {
                    if (cargo.getSistema() == null) {
                        cargo.setSistema(true);
                    }
                    if (cargo.getPersonalizado() == null) {
                        cargo.setPersonalizado(false);
                    }
                    if (cargo.getAtivo() == null) {
                        cargo.setAtivo(true);
                    }
                    if (cargo.getTipoInstituicao() == null && tipoInstituicao != null) {
                        cargo.setTipoInstituicao(tipoInstituicao);
                    }
                    return cargoRepository.save(cargo);
                })
                .orElseGet(() -> cargoRepository.save(Cargo.builder()
                        .nome(nome)
                        .descricao(descricao)
                        .tipoInstituicao(tipoInstituicao)
                        .sistema(true)
                        .personalizado(false)
                        .ativo(true)
                        .build()));
    }

    private void criarSeNaoExistir(String nome, String descricao) {
        permissaoRepository.findByNome(nome)
                .orElseGet(() -> permissaoRepository.save(
                        Permissao.builder()
                                .nome(nome)
                                .descricao(descricao)
                                .build()
                ));
    }

    private void sincronizarPermissoesPadraoPorCargo() {
        List<Cargo> cargos = cargoRepository.findAll();
        cargos.forEach(cargo -> permissoesPadraoParaCargo(cargo)
                .forEach(nomePermissao -> vincularPermissaoAoCargoSeNecessario(cargo.getIdCargo(), nomePermissao)));
    }

    private Set<String> permissoesPadraoParaCargo(Cargo cargo) {
        if (cargo == null || cargo.getNome() == null) {
            return Set.of();
        }

        String nomeNormalizado = normalizar(cargo.getNome());
        if (CARGOS_ADMIN_PLATAFORMA.contains(nomeNormalizado)) {
            return Set.of(
                    PermissionCodes.GERENCIAR_INSTITUICOES,
                    PermissionCodes.GERENCIAR_USUARIOS,
                    PermissionCodes.GERENCIAR_ESPACOS,
                    PermissionCodes.RESERVAR_ESPACO,
                    PermissionCodes.APROVAR_RESERVAS,
                    PermissionCodes.GERENCIAR_COMUNICADOS,
                    PermissionCodes.VISUALIZAR_AUDITORIA,
                    PermissionCodes.GERENCIAR_PLANOS
            );
        }

        if (CARGOS_ADMIN_INSTITUICAO.contains(nomeNormalizado)) {
            return Set.of(
                    PermissionCodes.GERENCIAR_USUARIOS,
                    PermissionCodes.GERENCIAR_ESPACOS,
                    PermissionCodes.RESERVAR_ESPACO,
                    PermissionCodes.APROVAR_RESERVAS,
                    PermissionCodes.GERENCIAR_COMUNICADOS,
                    PermissionCodes.VISUALIZAR_AUDITORIA
            );
        }

        if (CARGOS_GESTAO_ESCOLAR.contains(nomeNormalizado) || CARGOS_GESTAO_CORPORATIVA.contains(nomeNormalizado)) {
            return Set.of(
                    PermissionCodes.RESERVAR_ESPACO,
                    PermissionCodes.GERENCIAR_USUARIOS,
                    PermissionCodes.GERENCIAR_ESPACOS,
                    PermissionCodes.APROVAR_RESERVAS,
                    PermissionCodes.GERENCIAR_COMUNICADOS,
                    PermissionCodes.VISUALIZAR_AUDITORIA
            );
        }

        if (CARGOS_OPERACIONAIS_COM_RESERVA.contains(nomeNormalizado)) {
            return Set.of(PermissionCodes.RESERVAR_ESPACO);
        }

        return Set.of();
    }

    private void vincularPermissaoAoCargoSeNecessario(Integer idCargo, String nomePermissao) {
        Permissao permissao = permissaoRepository.findByNome(nomePermissao).orElse(null);
        if (permissao == null) {
            return;
        }

        boolean jaExiste = cargoPermissaoRepository.findByIdCargo(idCargo)
                .stream()
                .anyMatch(item -> item.getIdPermissao().equals(permissao.getIdPermissao()));

        if (jaExiste) {
            return;
        }

        cargoPermissaoRepository.save(CargoPermissao.builder()
                .idCargo(idCargo)
                .idPermissao(permissao.getIdPermissao())
                .build());
    }

    private String normalizar(String valor) {
        String semAcento = Normalizer.normalize(valor, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");
    }
}
