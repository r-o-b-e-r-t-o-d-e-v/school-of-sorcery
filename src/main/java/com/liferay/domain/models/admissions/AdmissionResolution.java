package com.liferay.domain.models.admissions;

import java.util.List;

public record AdmissionResolution(
      String year,
      List<ApplicationResolution> invitedApplications,
      List<ApplicationResolution> acceptedApplications,
      List<ApplicationResolution> rejectedApplications,
      List<ApplicationResolution> bannedApplications
) {
}
