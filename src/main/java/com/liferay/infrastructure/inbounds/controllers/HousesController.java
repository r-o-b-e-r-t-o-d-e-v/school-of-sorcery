package com.liferay.infrastructure.inbounds.controllers;

import com.liferay.application.usecases.GetHousesUseCase;
import com.liferay.infrastructure.dtos.responses.HouseDetailResponse;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/houses")
@Slf4j
@RequiredArgsConstructor
public class HousesController {

    private final GetHousesUseCase getHousesUseCase;

    @GetMapping("/{academicYear}")
    public ResponseEntity<List<String>> getHouses(
          @PathVariable @Pattern(regexp = "\\d{4}-\\d{4}") final String academicYear) {

        log.debug("Received GET request for houses");

        final List<String> houses = getHousesUseCase.getAvailableHouses(academicYear);
        return ResponseEntity.ok(houses);
    }

    @GetMapping("/{academicYear}/{houseName}")
    public ResponseEntity<HouseDetailResponse> getHouseDetail(
          @PathVariable @Pattern(regexp = "\\d{4}-\\d{4}") final String academicYear,
          @PathVariable final String houseName) {

        log.debug("Received GET request for house detail: {}", houseName);

        final String normalizedHouseName = houseName.toLowerCase();

        return getHousesUseCase.getHouseDetail(academicYear, normalizedHouseName)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
    }
}
