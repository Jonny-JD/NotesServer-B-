package com.jimmy_d.notesserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

@EnableJpaAuditing(modifyOnCreate = false)
@Configuration
public class AuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder
                .getContext()
                .getAuthentication())
                .map(Principal::getName);
    }
}
