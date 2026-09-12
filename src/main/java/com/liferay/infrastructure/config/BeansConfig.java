package com.liferay.infrastructure.config;

import com.liferay.domain.services.AdmissionService;
import com.liferay.domain.services.BanningService;
import com.liferay.domain.services.HouseScoringService;
import com.liferay.domain.services.HousingAssignationService;
import com.liferay.domain.services.HousingService;
import com.liferay.domain.services.PreAdmissionFilterService;
import com.liferay.domain.services.RankingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public AdmissionService admissionService(
          final PreAdmissionFilterService preAdmissionFilterService,
          final RankingService rankingService,
          final HousingService housingService
    ) {
        return new AdmissionService(preAdmissionFilterService, rankingService, housingService);
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

    @Bean
    public HousingService housingService(
          final HouseScoringService houseScoringService, final HousingAssignationService housingAssignationService) {
        return new HousingService(houseScoringService, housingAssignationService);
    }

    @Bean
    public HouseScoringService houseScoringService() {
        return new HouseScoringService();
    }

    @Bean
    public HousingAssignationService housingAssignationService() {
        return new HousingAssignationService();
    }
}
