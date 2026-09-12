package com.liferay.infrastructure.dtos.requests;

import java.util.Map;

public record HousePointsRequest(
      Map<String, Integer> virtue,
      Map<String, Integer> weakness,
      Map<String, Integer> family
) {
}
