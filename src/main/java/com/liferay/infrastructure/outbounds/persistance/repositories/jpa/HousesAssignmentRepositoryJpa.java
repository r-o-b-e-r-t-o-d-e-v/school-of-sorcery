package com.liferay.infrastructure.outbounds.persistance.repositories.jpa;

import com.liferay.infrastructure.outbounds.persistance.entities.HouseAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HousesAssignmentRepositoryJpa extends JpaRepository<HouseAssignmentEntity, Long> {
}
