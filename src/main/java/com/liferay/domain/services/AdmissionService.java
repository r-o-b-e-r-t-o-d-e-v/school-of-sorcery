package com.liferay.domain.services;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.domain.models.PreAdmissionResolution;

import java.util.List;
import java.util.logging.Logger;

public class AdmissionService {
    final Logger log = Logger.getLogger(AdmissionService.class.getName());

    final PreAdmissionFilterService preAdmissionFilterService;

    public AdmissionService(final PreAdmissionFilterService preAdmissionFilterService) {
        this.preAdmissionFilterService = preAdmissionFilterService;
    }

    public void process(final List<Application> applications, final CouncilPolicy councilPolicy) {
        log.fine("Admission process starting");

        final PreAdmissionResolution preAdmissionResolution = preAdmissionFilterService.resolve();
    }
}
