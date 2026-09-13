package com.liferay.application.usecases;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.domain.services.EnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class ProcessEnrollmentUseCase {
    final EnrollmentService enrollmentService;

    public ProcessEnrollmentUseCase(final EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Transactional
    public void execute(final List<Application> applications, final CouncilPolicy councilPolicy) {
        log.debug("Process enrollment use case");

        enrollmentService.processEnrollment(applications, councilPolicy);
    }
}
