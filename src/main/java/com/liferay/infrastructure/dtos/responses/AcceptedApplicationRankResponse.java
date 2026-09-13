package com.liferay.infrastructure.dtos.responses;

public record AcceptedApplicationRankResponse(
      String name,
      String familyName,
      Integer score,
      String status,
      String houseName
) {
}
