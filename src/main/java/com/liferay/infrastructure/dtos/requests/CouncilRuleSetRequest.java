package com.liferay.infrastructure.dtos.requests;

import com.liferay.domain.models.IntRange;
import com.liferay.domain.models.DateRange;

import java.util.List;

public record CouncilRuleSetRequest(
      String year,
      DateRange applicationDates,
      IntRange ageRange,
      int places,
      List<String> bannedFamilies,
      List<String> unacceptableWeaknesses,
      List<String> invitations,
      PointsRequest points,
      List<HouseRequest> houses
) {
}
