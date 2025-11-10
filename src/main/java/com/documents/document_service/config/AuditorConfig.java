package com.documents.document_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.UUID;

@Configuration
public class AuditorConfig {

    @Bean(name = "auditorAware")
    public AuditorAware<UUID> auditorAware() {
        return new AuditorAwareImpl();
    }
}
