package com.liferay.domain.services;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.domain.models.admissions.AdmissionResolution;
import com.liferay.domain.models.admissions.ApplicationResolution;
import com.liferay.domain.models.housing.HouseAssignationBook;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class EnrollmentService {
    private final Logger log = Logger.getLogger(EnrollmentService.class.getName());

    final AdmissionService admissionService;
    final HousingService housingService;

    public EnrollmentService(
          final AdmissionService admissionService,
          final HousingService housingService
    ) {
        this.admissionService = admissionService;
        this.housingService = housingService;
    }

    public void processEnrollment(final List<Application> applications, final CouncilPolicy councilPolicy) {
        log.fine("Processing enrollment");
        final AdmissionResolution admissionResolution = admissionService.processAdmission(applications, councilPolicy);

        final HouseAssignationBook houseAssignationBook = housingService.assignHouses(
              mergeAcceptedApplications(admissionResolution), councilPolicy.housingScoreRules());

        log.fine("House assignation process finished: " + houseAssignationBook);
    }

    private List<Application> mergeAcceptedApplications(final AdmissionResolution admissionResolution) {
        return Stream.concat(
              admissionResolution.invitedApplications().stream()
                    .map(ApplicationResolution::application),
              admissionResolution.acceptedApplications().stream()
                    .map(ApplicationResolution::application)
        ).toList();
    }
}
