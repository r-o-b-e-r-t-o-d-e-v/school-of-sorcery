package com.liferay.domain.models;

import java.util.List;

public record PreFilteredApplications(
      List<Application> candidates,
      List<BannedApplication> banned
) {
}
