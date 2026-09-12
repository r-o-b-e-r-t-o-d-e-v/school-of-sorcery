package com.liferay.domain.models;

import java.util.List;

public record AdmissionRules(
      DateRange applicationDates,
      IntRange ageRange,
      List<String> bannedFamilies,
      List<String> unacceptableWeaknesses
) {
}
