package com.liferay.infrastructure.dtos.requests;

public record AgePointsRequest(
      int from,
      int to,
      int points
) {
}
