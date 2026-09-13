package com.liferay.application.usecases;

import com.liferay.domain.interfaces.ApplicationRepository;
import com.liferay.infrastructure.dtos.responses.AcceptedApplicationRankResponse;
import com.liferay.infrastructure.dtos.responses.RejectedApplicationRankResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetApplicationsRankingUseCase {

    private final ApplicationRepository applicationRepository;

    public List<AcceptedApplicationRankResponse> getAcceptedApplicationsRanking(final String academicYear) {
        return applicationRepository.getAcceptedApplicationsRanking(academicYear);
    }

    public List<RejectedApplicationRankResponse> getRejectedApplicationsRanking(final String academicYear) {
        return applicationRepository.getRejectedApplicationsRanking(academicYear);
    }
}
