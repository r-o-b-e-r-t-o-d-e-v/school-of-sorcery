package com.liferay.infrastructure.outbounds.persistance.mappers;

import com.liferay.domain.models.Application;
import com.liferay.infrastructure.outbounds.persistance.entities.StudentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentEntityMapper {
    public StudentEntity map(final Application application) {
        return StudentEntity.builder()
              .externalId(application.id())
              .name(application.firstName())
              .familyName(application.familyName())
              .build();
    }
}
