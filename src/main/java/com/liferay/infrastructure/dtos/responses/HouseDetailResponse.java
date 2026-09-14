package com.liferay.infrastructure.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record HouseDetailResponse(
      String houseName,
      int beds,
      List<String> students
) {
}
