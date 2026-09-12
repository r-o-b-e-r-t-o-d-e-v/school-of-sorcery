package com.liferay.domain.models.admissions;

import com.liferay.domain.models.Application;

import java.util.List;

public record PreFilteredApplications(
      List<Application> candidates,
      List<BannedApplication> banned
) {
}
