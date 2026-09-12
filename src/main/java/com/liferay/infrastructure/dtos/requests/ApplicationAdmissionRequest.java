package com.liferay.infrastructure.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liferay.domain.models.Application;

import java.util.List;

public record ApplicationAdmissionRequest(
      @JsonProperty("council") CouncilRuleSetRequest councilRuleSetRequest,
      @JsonProperty("applications") List<Application> applicationRequests
) {
}
