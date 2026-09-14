package com.liferay.infrastructure.inbounds.controllers;

import com.liferay.application.usecases.GetApplicationsRankingUseCase;
import com.liferay.application.usecases.ProcessEnrollmentUseCase;
import com.liferay.domain.exceptions.AdmissionsAlreadyProcessedException;
import com.liferay.domain.exceptions.AdmissionsNotYetProcessedException;
import com.liferay.domain.models.Application;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.infrastructure.dtos.requests.ApplicationAdmissionRequest;
import com.liferay.infrastructure.dtos.requests.HouseRequest;
import com.liferay.infrastructure.dtos.responses.AcceptedApplicationRankResponse;
import com.liferay.infrastructure.dtos.responses.RejectedApplicationRankResponse;
import com.liferay.infrastructure.exceptions.InvalidAdmissionRequestException;
import com.liferay.infrastructure.mappers.CouncilRuleSetRequestMapper;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/applications")
@Slf4j
@RequiredArgsConstructor
public class ApplicationsController {
    private final CouncilRuleSetRequestMapper councilRuleSetRequestMapper;
    private final ProcessEnrollmentUseCase processEnrollmentUseCase;
    private final GetApplicationsRankingUseCase getApplicationsRankingUseCase;

    @PostMapping("/{academicYear}/admission")
    public ResponseEntity<String> postApplications(
          @PathVariable @Pattern(regexp = "\\d{4}-\\d{4}") final String academicYear,
          @RequestBody final ApplicationAdmissionRequest applicationAdmissionRequest) {

        log.debug("Received POST request for application admissions");

        // Validations
        if (!Objects.equals(applicationAdmissionRequest.councilRuleSetRequest().year(), academicYear)) {
            throw new InvalidAdmissionRequestException("Academic year mismatch");
        }

        if (Objects.equals(academicYear.split("-")[0], academicYear.split("-")[1])) {
            throw new InvalidAdmissionRequestException("Academic year malformed");
        }

        if (applicationAdmissionRequest.councilRuleSetRequest().places() != applicationAdmissionRequest.councilRuleSetRequest().houses().stream().mapToInt(HouseRequest::beds).sum()) {
            throw new InvalidAdmissionRequestException("Places and available house beds mismatch");
        }

        if (processEnrollmentUseCase.isAcademicYearProcessed(academicYear)) {
            throw new AdmissionsAlreadyProcessedException(
                  "Academic year " + academicYear + " has already been processed");
        }

        // Mapping to domain
        final List<Application> applications = applicationAdmissionRequest.applicationRequests();
        final CouncilPolicy councilPolicy =
              councilRuleSetRequestMapper.map(applicationAdmissionRequest.councilRuleSetRequest());

        // Processes the admission evaluation
        processEnrollmentUseCase.execute(applications, councilPolicy);

        return ResponseEntity.ok().body("Applications successfully processed");
    }

    @GetMapping("/{academicYear}/ranking")
    public ResponseEntity<List<AcceptedApplicationRankResponse>> getRankings(
          @PathVariable @Pattern(regexp = "\\d{4}-\\d{4}") final String academicYear
    ) {
        log.debug("Received GET request for application ranking");

        if (!processEnrollmentUseCase.isAcademicYearProcessed(academicYear)) {
            throw new AdmissionsNotYetProcessedException("Academic year " + academicYear + " hasn't yet been processed");
        }

        final List<AcceptedApplicationRankResponse> response =
              getApplicationsRankingUseCase.getAcceptedApplicationsRanking(academicYear);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{academicYear}/rejections")
    public ResponseEntity<List<RejectedApplicationRankResponse>> getRejections(
          @PathVariable @Pattern(regexp = "\\d{4}-\\d{4}") final String academicYear
    ) {
        log.debug("Received GET request for application rejections");

        if (!processEnrollmentUseCase.isAcademicYearProcessed(academicYear)) {
            throw new AdmissionsNotYetProcessedException("Academic year " + academicYear + " hasn't yet been processed");
        }

        final List<RejectedApplicationRankResponse> response =
              getApplicationsRankingUseCase.getRejectedApplicationsRanking(academicYear);

        return ResponseEntity.ok(response);
    }
}
