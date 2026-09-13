package com.liferay.infrastructure.inbounds.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/houses")
@Slf4j
@RequiredArgsConstructor
public class HousesController {

    @GetMapping("/{course}")
    public ResponseEntity<String> getHouses(@PathVariable String course) {
        log.debug("Received GET request for houses");

        // TODO

        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/{course}/{house}")
    public ResponseEntity<String> getHouseDetail(@PathVariable String course, @PathVariable String house) {
        log.debug("Received GET request for house detail: {}", house);

        // TODO

        return ResponseEntity.unprocessableEntity().build();
    }
}
