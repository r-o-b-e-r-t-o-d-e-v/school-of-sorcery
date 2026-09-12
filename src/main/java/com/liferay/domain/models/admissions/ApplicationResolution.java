package com.liferay.domain.models.admissions;

import com.liferay.domain.models.Application;

public record ApplicationResolution(
      Application application,
      ApplicationStatus applicationStatus,
      String rejectionFeedback,
      Integer score
) {
}
