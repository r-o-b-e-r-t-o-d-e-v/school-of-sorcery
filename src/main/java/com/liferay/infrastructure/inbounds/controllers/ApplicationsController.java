package com.liferay.infrastructure.inbounds.controllers;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.domain.services.AdmissionService;
import com.liferay.infrastructure.dtos.requests.ApplicationAdmissionRequest;
import com.liferay.infrastructure.mappers.CouncilRuleSetRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/applications")
@Slf4j
@RequiredArgsConstructor
public class ApplicationsController {
    private final CouncilRuleSetRequestMapper councilRuleSetRequestMapper;
    private final AdmissionService admissionService;

    @PostMapping("/{course}/admission")
    public ResponseEntity<String> postApplications(
          @PathVariable String course,
          @RequestBody final ApplicationAdmissionRequest applicationAdmissionRequest) {
        log.debug("Received POST request for application admissions");

        // TODO some request data validation
        // 1. Path var 'course' should match the course in the request body
        // 2. Request body's 'places' and the total sum of all the beds among the houses should be equals

        // Mapping to domain
        final List<Application> applications = applicationAdmissionRequest.applicationRequests();
        final CouncilPolicy councilPolicy =
              councilRuleSetRequestMapper.map(applicationAdmissionRequest.councilRuleSetRequest());

        // Processes the admission evaluation
        admissionService.process(applications, councilPolicy);

        // TODO return the corresponding response
        return ResponseEntity.unprocessableEntity().build();
    }
}
