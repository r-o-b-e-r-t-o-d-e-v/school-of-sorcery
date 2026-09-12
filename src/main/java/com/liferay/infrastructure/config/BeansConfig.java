package com.liferay.infrastructure.config;

import com.liferay.domain.services.AdmissionService;
import com.liferay.domain.services.BanningService;
import com.liferay.domain.services.PreAdmissionFilterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public AdmissionService admissionService(final PreAdmissionFilterService preAdmissionFilterService) {
        return new AdmissionService(preAdmissionFilterService);
    }

    @Bean
    public PreAdmissionFilterService preAdmissionFilterService(final BanningService banningService) {
        return new PreAdmissionFilterService(banningService);
    }

    @Bean
    public BanningService banningService() {
        return new BanningService();
    }
}
