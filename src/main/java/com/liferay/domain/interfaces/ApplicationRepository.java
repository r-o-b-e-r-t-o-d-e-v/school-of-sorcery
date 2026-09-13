package com.liferay.domain.interfaces;

import com.liferay.domain.models.admissions.AdmissionResolution;

public interface ApplicationRepository {
    void save(final AdmissionResolution admissionResolution);
}
