package com.liferay.domain.models.admissions;

import com.liferay.domain.models.Application;

public record BannedApplication(
      Application application,
      String banReason
) {
}
