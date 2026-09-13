package com.liferay.infrastructure.outbounds.persistance.mappers;

import com.liferay.domain.models.admissions.ApplicationResolution;
import com.liferay.infrastructure.outbounds.persistance.entities.ApplicationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationResolutionEntityMapper {

    private final StudentEntityMapper studentEntityMapper;

    public List<ApplicationEntity> map(final String academicYear, final List<ApplicationResolution> applicationResolutions) {
        return applicationResolutions.stream()
              .map(applicationResolution -> map(academicYear, applicationResolution))
              .toList();
    }

    public ApplicationEntity map(final String academicYear, final ApplicationResolution applicationResolution) {
        return ApplicationEntity.builder()
              .academicYear(academicYear)
              .applicationDate(applicationResolution.application().applicationDate())
              .student(studentEntityMapper.map(applicationResolution.application()))
              .status(applicationResolution.applicationStatus())
              .rejectionFeedback(applicationResolution.rejectionFeedback())
              .score(applicationResolution.score())
              .build();
    }
}
