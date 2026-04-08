package com.spaceroom.config;

import com.spaceroom.entities.Permissao;
import com.spaceroom.repositories.PermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PermissionBootstrap {

    private final PermissaoRepository permissaoRepository;

    @Bean
    public ApplicationRunner seedDefaultPermissions() {
        return args -> {
            criarSeNaoExistir(
                    PermissionCodes.GERENCIAR_INSTITUICOES,
                    "Permite cadastrar e administrar instituicoes da plataforma."
            );
            criarSeNaoExistir(
                    PermissionCodes.RESERVAR_ESPACO,
                    "Permite criar e alterar reservas de espacos."
            );
        };
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
}
