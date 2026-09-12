package com.liferay.infrastructure.inbounds.controllers;

import com.liferay.domain.services.AdmissionService;
import com.liferay.infrastructure.dtos.requests.ApplicationAdmissionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/applications")
@Slf4j
@RequiredArgsConstructor
public class ApplicationsController {
    private final AdmissionService admissionService;

    @PostMapping("/{course}/admission")
    public ResponseEntity<String> postApplications(
          @PathVariable String course,
          @RequestBody final ApplicationAdmissionRequest applicationAdmissionRequest) {
        log.debug("Received POST request for application admissions");

        // TODO some request data validation

        // TODO mapping to domain

        // TODO process the admission evaluation
        admissionService.process();

        // TODO return the corresponding response
        return ResponseEntity.unprocessableEntity().build();
    }
}
