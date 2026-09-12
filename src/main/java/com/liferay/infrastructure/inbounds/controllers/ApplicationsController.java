package com.liferay.infrastructure.inbounds.controllers;

import com.liferay.infrastructure.dtos.requests.ApplicationAdmissionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
@Slf4j
public class ApplicationsController {

    @PostMapping("/<year/course>/calculate")
    public ResponseEntity<String> postApplications(
          @RequestBody final ApplicationAdmissionRequest applicationAdmissionRequest) {
        // TODO some request data validation

        // TODO process the admission evaluation

        // TODO return the corresponding response
        return ResponseEntity.unprocessableEntity().build();
    }
}
