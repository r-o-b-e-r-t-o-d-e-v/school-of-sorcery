package com.liferay.infrastructure.dtos.responses;

public record RejectedApplicationRankResponse(
      String name,
      String familyName,
      Integer score,
      String status,
      String rejectionFeedback
) {
}
