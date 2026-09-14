package com.liferay.infrastructure.outbounds.persistance.repositories.utils;

import com.liferay.infrastructure.outbounds.persistance.entities.StudentEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RepositoryUtils {
    public String formatStudentFullName(final StudentEntity studentEntity) {
        return String.format("%s %s", studentEntity.getName(), studentEntity.getFamilyName());
    }
}
