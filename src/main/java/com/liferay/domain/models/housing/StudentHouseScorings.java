package com.liferay.domain.models.housing;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.House;

import java.util.Map;

public record StudentHouseScorings(
      Application application,
      Map<House, Integer> houses
) {
}
