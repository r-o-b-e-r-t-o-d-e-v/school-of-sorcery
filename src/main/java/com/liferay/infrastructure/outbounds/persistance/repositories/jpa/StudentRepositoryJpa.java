package com.liferay.infrastructure.outbounds.persistance.repositories.jpa;

import com.liferay.infrastructure.outbounds.persistance.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepositoryJpa extends JpaRepository<StudentEntity, Long> {
    List<StudentEntity> findAllByExternalIdIn(final List<String> externalIds);
}
