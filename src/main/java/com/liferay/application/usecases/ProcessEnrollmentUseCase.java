package com.liferay.application.usecases;

import com.liferay.domain.interfaces.ApplicationRepository;
import com.liferay.domain.models.Application;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.domain.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessEnrollmentUseCase {
    final EnrollmentService enrollmentService;
    final ApplicationRepository applicationRepository;

    @Transactional
    public void execute(final List<Application> applications, final CouncilPolicy councilPolicy) {
        log.debug("Process enrollment use case");

        enrollmentService.processEnrollment(applications, councilPolicy);
    }

    public boolean isAcademicYearProcessed(final String academicYear) {
        return applicationRepository.isAcademicYearProcessed(academicYear);
    }
}
