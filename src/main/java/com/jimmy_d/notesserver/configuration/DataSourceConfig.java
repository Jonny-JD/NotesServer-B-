package com.jimmy_d.notesserver.configuration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final Environment env;

    @Value("${spring.datasource.url}")
    private String url;

    @Bean
    @SneakyThrows
    public DataSource dataSource() {
        String username;
        String password;

        if (env.acceptsProfiles(Profiles.of("prod"))) {
            Path usernamePath = Paths.get("/run/secrets/db_username");
            Path passwordPath = Paths.get("/run/secrets/db_password");

            username = Files.readString(usernamePath).trim();
            password = Files.readString(passwordPath).trim();
        } else {
            username = env.getProperty("spring.datasource.username");
            password = env.getProperty("spring.datasource.password");
        }

        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
