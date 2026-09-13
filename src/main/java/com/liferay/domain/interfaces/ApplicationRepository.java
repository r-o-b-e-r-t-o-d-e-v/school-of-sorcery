package com.liferay.domain.interfaces;

import com.liferay.domain.models.admissions.AdmissionResolution;
import com.liferay.infrastructure.dtos.responses.AcceptedApplicationRankResponse;
import com.liferay.infrastructure.dtos.responses.RejectedApplicationRankResponse;

import java.util.List;

public interface ApplicationRepository {
    void save(final AdmissionResolution admissionResolution);
    boolean isAcademicYearProcessed(final String academicYear);
    List<AcceptedApplicationRankResponse> getAcceptedApplicationsRanking(final String academicYear);
    List<RejectedApplicationRankResponse> getRejectedApplicationsRanking(final String academicYear);
}
