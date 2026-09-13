package com.liferay.application.usecases;

import com.liferay.domain.interfaces.HouseRepository;
import com.liferay.infrastructure.dtos.responses.HouseDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetHousesUseCase {

    private final HouseRepository houseRepository;

    public List<String> getAvailableHouses(final String academicYear) {
        return houseRepository.getAvailableHouses(academicYear);
    }

    public Optional<HouseDetailResponse> getHouseDetail(final String academicYear, final String houseName) {
        return houseRepository.getHouseDetail(academicYear, houseName);
    }
}
