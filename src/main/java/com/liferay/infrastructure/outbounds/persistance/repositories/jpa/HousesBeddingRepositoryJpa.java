package com.liferay.infrastructure.outbounds.persistance.repositories.jpa;

import com.liferay.infrastructure.outbounds.persistance.entities.HouseBeddingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HousesBeddingRepositoryJpa extends JpaRepository<HouseBeddingEntity, Long> {
    Optional<HouseBeddingEntity> findByAcademicYearAndHouseName(final String academicYear, final String houseName);
    List<HouseBeddingEntity> findAllByAcademicYear(final String academicYear);
}
