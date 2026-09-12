package com.liferay.domain.models.housing;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.House;

import java.util.List;

public record HouseAssignation(
      House house,
      List<Application> students
) {
}
