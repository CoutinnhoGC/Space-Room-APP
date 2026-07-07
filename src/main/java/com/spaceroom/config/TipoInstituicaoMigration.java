package com.spaceroom.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class TipoInstituicaoMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoInstituicaoMigration.class);

    private final JdbcTemplate jdbcTemplate;

    @Bean
    public ApplicationRunner syncTipoInstituicaoEnum() {
        return args -> {
            String databaseName = jdbcTemplate.execute((ConnectionCallback<String>) connection -> connection.getMetaData().getDatabaseProductName());
            if (databaseName == null || !databaseName.toLowerCase().contains("postgresql")) {
                return;
            }

            Boolean enumExists = jdbcTemplate.queryForObject(
                    "select exists (select 1 from pg_type where typname = 'tipo_instituicao')",
                    Boolean.class
            );
            if (!Boolean.TRUE.equals(enumExists)) {
                return;
            }

            jdbcTemplate.execute("alter type tipo_instituicao add value if not exists 'INSTITUICAO_ENSINO'");
            jdbcTemplate.execute("alter type tipo_instituicao add value if not exists 'ORGAO_PUBLICO'");
            jdbcTemplate.execute("alter type tipo_instituicao add value if not exists 'CENTRO_PESQUISA'");
            jdbcTemplate.update("""
                    update instituicao
                    set tipo = 'INSTITUICAO_ENSINO'::tipo_instituicao
                    where tipo::text in ('ESCOLA', 'FACULDADE', 'UNIVERSIDADE', 'SENAI')
                    """);
            LOGGER.info("TipoInstituicao enum sincronizado com categorias atuais.");
        };
    }
}
