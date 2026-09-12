package com.liferay.infrastructure.dtos.requests;

import org.springframework.boot.context.properties.bind.Name;

import java.util.List;

public record ApplicationAdmissionRequest(
      @Name("council")
      CouncilRuleSetRequest councilRuleSetRequest,
      @Name("applications")
      List<ApplicationRequest> applicationRequests
) {
}
