package com.liferay.domain.interfaces;

import com.liferay.domain.models.housing.HouseAssignationBook;
import com.liferay.infrastructure.dtos.responses.HouseDetailResponse;

import java.util.List;
import java.util.Optional;

public interface HouseRepository {
    void save(final String academicYear, final HouseAssignationBook houseAssignationBook);
    List<String> getAvailableHouses(final String academicYear);
    Optional<HouseDetailResponse> getHouseDetail(final String academicYear, final String houseName);
}
