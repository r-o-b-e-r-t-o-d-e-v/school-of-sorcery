package com.liferay.infrastructure.dtos.requests;

import java.util.List;

public record ApplicationAdmissionRequest(
      List<ApplicationRequest> applicationRequests,
      CouncilRuleSetRequest councilRuleSetRequest
) {
}
