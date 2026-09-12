package com.liferay.infrastructure.dtos.requests;

import java.time.LocalDate;
import java.util.List;

public record ApplicationRequest(
      String id,
      String firstName,
      String familyName,
      int age,
      List<String> virtues,
      List<String> weaknesses,
      LocalDate applicationDate
) {
}
