package com.liferay.domain.models.admissions;

import com.liferay.domain.models.Application;

public record ApplicationScore(
      Application application,
      int score
) {
}
