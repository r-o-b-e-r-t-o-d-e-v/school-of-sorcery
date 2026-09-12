package com.liferay.infrastructure.config;

import com.liferay.domain.services.AdmissionService;
import com.liferay.domain.services.BanningService;
import com.liferay.domain.services.PreAdmissionFilterService;
import com.liferay.domain.services.RankingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public AdmissionService admissionService(
          final PreAdmissionFilterService preAdmissionFilterService,
          final RankingService rankingService
    ) {
        return new AdmissionService(preAdmissionFilterService, rankingService);
    }

    @Bean
    public PreAdmissionFilterService preAdmissionFilterService(final BanningService banningService) {
        return new PreAdmissionFilterService(banningService);
    }

    @Bean
    public BanningService banningService() {
        return new BanningService();
    }

    @Bean
    public RankingService rankingService() {
        return new RankingService();
    }
}
