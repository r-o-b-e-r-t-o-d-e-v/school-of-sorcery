package com.liferay.domain.models;

public record BannedApplication(
      Application application,
      String banReason
) {
}
