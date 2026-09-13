package com.liferay.infrastructure.outbounds.persistance.repositories.jpa;

import com.liferay.infrastructure.outbounds.persistance.entities.HouseAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HousesAssignmentRepositoryJpa extends JpaRepository<HouseAssignmentEntity, Long> {
    List<HouseAssignmentEntity> findByAcademicYearAndHouseName(final String academicYear, final String houseName);
}
