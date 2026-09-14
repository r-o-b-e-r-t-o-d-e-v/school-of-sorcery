package com.liferay.infrastructure.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AcceptedApplicationRankResponse(
      String name,
      String familyName,
      Integer score,
      String status,
      String house
) {
}
