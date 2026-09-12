package com.liferay.domain.models.admissions;

import com.liferay.domain.models.Application;

import java.util.List;

public record PreAdmissionResolution(
    List<Application> invited,
    List<Application> candidates,
    List<BannedApplication> bannedApplications
) {
}
