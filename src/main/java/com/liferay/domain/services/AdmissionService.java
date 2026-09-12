package com.liferay.domain.services;

import java.util.logging.Logger;

public class AdmissionService {
    final Logger log = Logger.getLogger(AdmissionService.class.getName());

    public AdmissionService() {}

    public void process() {
        log.fine("Admission process starting");
    }
}
