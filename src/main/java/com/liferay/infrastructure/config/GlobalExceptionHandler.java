package com.liferay.infrastructure.config;

import com.liferay.domain.exceptions.AdmissionsAlreadyProcessedException;
import com.liferay.domain.exceptions.AdmissionsNotYetProcessedException;
import com.liferay.infrastructure.exceptions.InvalidAdmissionRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAdmissionRequestException.class)
    public ResponseEntity<ProblemDetail> invalidAdmissionRequestException(
          final InvalidAdmissionRequestException exception) {

        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Invalid Admission Request");
        problem.setDetail(exception.getMessage());

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(AdmissionsNotYetProcessedException.class)
    public ResponseEntity<ProblemDetail> admissionsNotYetProcessedException(
          final AdmissionsNotYetProcessedException exception) {

        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle(exception.getMessage());
        problem.setDetail(exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(AdmissionsAlreadyProcessedException.class)
    public ResponseEntity<ProblemDetail> admissionsAlreadyProcessedException(
          final AdmissionsAlreadyProcessedException exception) {

        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle(exception.getMessage());
        problem.setDetail(exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
}
