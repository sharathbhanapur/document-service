package com.documents.document_service.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides a dummy AuditorAware bean for testing to satisfy Spring Data auditing dependencies.
 */
@TestConfiguration
public class TestAuditorAwareConfig {

    @Bean(name = "auditorAware")
    public AuditorAware<UUID> auditorAware() {
        // Always returns a random UUID as auditor for tests.
        return () -> Optional.of(UUID.randomUUID());
    }
}
