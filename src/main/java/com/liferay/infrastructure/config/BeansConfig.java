package com.liferay.infrastructure.config;

import com.liferay.domain.services.AdmissionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public AdmissionService admissionService() {
        return new AdmissionService();
    }
}
