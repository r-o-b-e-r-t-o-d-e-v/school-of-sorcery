package com.liferay.domain.models;

import java.time.LocalDate;
import java.util.List;

public record Application(
      String id,
      String firstName,
      String familyName,
      int age,
      List<String> virtues,
      List<String> weaknesses,
      LocalDate applicationDate
) {
}
