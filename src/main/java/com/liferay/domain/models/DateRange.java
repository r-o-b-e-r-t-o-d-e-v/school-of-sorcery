package com.liferay.domain.models;

import java.time.LocalDate;

public record DateRange(
      LocalDate from,
      LocalDate to
) {
}
