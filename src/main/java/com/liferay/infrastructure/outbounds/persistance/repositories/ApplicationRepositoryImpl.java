package com.liferay.infrastructure.outbounds.persistance.repositories;

import com.liferay.domain.interfaces.ApplicationRepository;
import com.liferay.domain.models.admissions.AdmissionResolution;
import com.liferay.domain.models.admissions.ApplicationResolution;
import com.liferay.domain.models.admissions.ApplicationStatus;
import com.liferay.infrastructure.dtos.responses.AcceptedApplicationRankResponse;
import com.liferay.infrastructure.dtos.responses.RejectedApplicationRankResponse;
import com.liferay.infrastructure.outbounds.persistance.entities.ApplicationEntity;
import com.liferay.infrastructure.outbounds.persistance.entities.StudentEntity;
import com.liferay.infrastructure.outbounds.persistance.mappers.ApplicationResolutionEntityMapper;
import com.liferay.infrastructure.outbounds.persistance.repositories.jpa.ApplicationRepositoryJpa;
import com.liferay.infrastructure.outbounds.persistance.repositories.jpa.StudentRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepository {

    private final Logger log = Logger.getLogger(ApplicationRepositoryImpl.class.getName());
    private final ApplicationResolutionEntityMapper applicationResolutionEntityMapper;
    private final ApplicationRepositoryJpa applicationRepositoryJpa;
    private final StudentRepositoryJpa studentRepositoryJpa;

    @Override
    public void save(final AdmissionResolution admissionResolution) {
        log.fine("Saving admission resolution");

        final String academicYear = admissionResolution.year();
        final List<ApplicationResolution> applicationResolutions = extractAllApplicationResolutions(admissionResolution);

        final List<ApplicationEntity> applicationEntities =
              applicationResolutionEntityMapper.map(academicYear, applicationResolutions);

        setExistingStudentsIds(applicationEntities);

        applicationRepositoryJpa.saveAll(applicationEntities);
        log.fine("Admission resolution saved");
    }

    @Override
    public boolean isAcademicYearProcessed(final String academicYear) {
        return applicationRepositoryJpa.existsByAcademicYear(academicYear);
    }

    @Override
    public List<AcceptedApplicationRankResponse> getAcceptedApplicationsRanking(final String academicYear) {
        return applicationRepositoryJpa.findAllAcceptedRankingsWithHouses(academicYear);
    }

    @Override
    public List<RejectedApplicationRankResponse> getRejectedApplicationsRanking(final String academicYear) {
        final List<ApplicationEntity> applicationEntities = applicationRepositoryJpa.findAllByAcademicYearAndStatusInOrderByScoreDescAgeAtApplicationAscStudentFamilyNameAscStudentNameAsc(
              academicYear, List.of(ApplicationStatus.REJECTED, ApplicationStatus.BANNED));

        return mapApplicationEntityToRejectedApplicationRankResponse(applicationEntities);
    }

    private List<ApplicationResolution> extractAllApplicationResolutions(final AdmissionResolution admissionResolution) {
        return Stream.of(
                    admissionResolution.invitedApplications(),
                    admissionResolution.acceptedApplications(),
                    admissionResolution.rejectedApplications(),
                    admissionResolution.bannedApplications()
              )
              .flatMap(Collection::stream)
              .toList();
    }

    // Updates the student entity ids. This is because when saving the application resolutions,
    // the entities will always have their id as null (the sequential id, not the external id).
    // So the students will always be created even if they already exist.
    // To fix that, we recover the existing ones and set their ids in the entities.
    // JPA will later insert the non-existing entities and respect the ones with id.
    private void setExistingStudentsIds(final List<ApplicationEntity> applicationEntities) {
        final List<StudentEntity> studentEntities =
              applicationEntities.stream().map(ApplicationEntity::getStudent).toList();

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

    private List<RejectedApplicationRankResponse> mapApplicationEntityToRejectedApplicationRankResponse(
          final List<ApplicationEntity> applicationEntities) {
        return applicationEntities.stream()
              .map(applicationEntity -> new RejectedApplicationRankResponse(
                    applicationEntity.getStudent().getName(),
                    applicationEntity.getStudent().getFamilyName(),
                    applicationEntity.getScore(),
                    applicationEntity.getStatus().toString(),
                    applicationEntity.getRejectionFeedback()
              )).toList();
    }
}
