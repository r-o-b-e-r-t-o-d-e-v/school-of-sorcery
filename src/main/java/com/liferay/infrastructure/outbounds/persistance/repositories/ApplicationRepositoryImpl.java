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
import com.liferay.infrastructure.outbounds.persistance.repositories.utils.RepositoryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepository {
    private final Logger log = Logger.getLogger(ApplicationRepositoryImpl.class.getName());

    private final RepositoryHelper repositoryHelper;
    private final ApplicationResolutionEntityMapper applicationResolutionEntityMapper;
    private final ApplicationRepositoryJpa applicationRepositoryJpa;

    @Override
    public void save(final AdmissionResolution admissionResolution) {
        log.fine("Saving admission resolution");

        final String academicYear = admissionResolution.year();
        final List<ApplicationResolution> applicationResolutions = extractAllApplicationResolutions(admissionResolution);

        final List<ApplicationEntity> applicationEntities =
              applicationResolutionEntityMapper.map(academicYear, applicationResolutions);

        final List<StudentEntity> studentEntities =
              applicationEntities.stream().map(ApplicationEntity::getStudent).toList();

        repositoryHelper.setExistingStudentsIds(studentEntities);

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

    private List<RejectedApplicationRankResponse> mapApplicationEntityToRejectedApplicationRankResponse(
          final List<ApplicationEntity> applicationEntities) {
        return applicationEntities.stream()
              .map(applicationEntity -> new RejectedApplicationRankResponse(
                    applicationEntity.getStudent().getName(),
                    applicationEntity.getStudent().getFamilyName(),
                    applicationEntity.getScore(),
                    applicationEntity.getStatus().toString(),
                    applicationEntity.getRejectionReason()
              )).toList();
    }
}
