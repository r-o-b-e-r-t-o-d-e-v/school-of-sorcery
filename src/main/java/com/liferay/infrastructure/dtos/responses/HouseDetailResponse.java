package com.liferay.infrastructure.dtos.responses;

import java.util.List;

public record HouseDetailResponse(
      String houseName,
      int beds,
      List<String> students
) {
}
