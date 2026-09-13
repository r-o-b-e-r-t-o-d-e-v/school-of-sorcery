package com.liferay.infrastructure.outbounds.persistance.repositories.jpa;

import com.liferay.infrastructure.outbounds.persistance.entities.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HousesRepositoryJpa extends JpaRepository<HouseEntity, Long> {
    List<HouseEntity> findAllByNameIn(final List<String> houseEntitiesNames);
}
