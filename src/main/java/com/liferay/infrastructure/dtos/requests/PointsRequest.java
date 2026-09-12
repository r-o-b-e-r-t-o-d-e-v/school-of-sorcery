package com.liferay.infrastructure.dtos.requests;

import java.util.List;
import java.util.Map;

public record PointsRequest(
      Map<String, Integer> virtue,
      Map<String, Integer> family,
      Map<String, Integer> weakness,
      List<AgePointsRequest> age
) {
}
