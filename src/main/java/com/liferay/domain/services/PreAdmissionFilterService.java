package com.liferay.domain.services;

import com.liferay.domain.models.PreAdmissionResolution;

import java.util.ArrayList;

public class PreAdmissionFilterService {
    public PreAdmissionResolution resolve() {
        return new PreAdmissionResolution(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
