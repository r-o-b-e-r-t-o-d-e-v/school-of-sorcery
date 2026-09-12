package com.liferay.infrastructure.dtos.requests;

public record HouseRequest(
      String name,
      int beds,
      HousePointsRequest points
) {
}
