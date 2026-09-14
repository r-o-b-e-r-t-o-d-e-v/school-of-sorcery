package com.liferay.infrastructure.outbounds.persistance.repositories.utils;

import com.liferay.infrastructure.outbounds.persistance.entities.StudentEntity;
import com.liferay.infrastructure.outbounds.persistance.repositories.jpa.StudentRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RepositoryHelper {
    private final StudentRepositoryJpa studentRepositoryJpa;

    // Updates the student entity ids. This is because when saving the application resolutions,
    // the entities will always have their id as null (the sequential id, not the external id).
    // So the students will always be created even if they already exist.
    // To fix that, we recover the existing ones and set their ids in the entities.
    // JPA will later insert the non-existing entities and respect the ones with id.
    public void setExistingStudentsIds(final List<StudentEntity> studentEntities) {
        final Map<String, StudentEntity> studentsByExternalId =
              studentEntities.stream()
                    .collect(Collectors.toMap(
                          StudentEntity::getExternalId,
                          Function.identity()
                    ));

        final List<String> studentEntitiesExternalIds =
              studentEntities.stream().map(StudentEntity::getExternalId).toList();

        final List<StudentEntity> existingStudentEntities =
              studentRepositoryJpa.findAllByExternalIdIn(studentEntitiesExternalIds);

        for (final StudentEntity student : existingStudentEntities) {
            studentsByExternalId.get(student.getExternalId()).setId(student.getId());
        }
    }
}
