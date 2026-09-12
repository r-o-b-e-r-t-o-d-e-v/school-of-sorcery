package com.liferay.domain.models;

import java.util.List;

public record PreAdmissionResolution(
    List<Application> invited,
    List<Application> candidates,
    List<BannedApplication> bannedApplications
) {
}
