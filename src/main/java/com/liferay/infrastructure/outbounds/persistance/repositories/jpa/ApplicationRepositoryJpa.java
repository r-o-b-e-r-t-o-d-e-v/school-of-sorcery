package com.liferay.infrastructure.outbounds.persistance.repositories.jpa;

import com.liferay.infrastructure.outbounds.persistance.entities.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepositoryJpa extends JpaRepository<ApplicationEntity, Long> {}
