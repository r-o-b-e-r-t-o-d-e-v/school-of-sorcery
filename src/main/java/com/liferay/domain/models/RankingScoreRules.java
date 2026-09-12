package com.liferay.domain.models;

import com.liferay.infrastructure.dtos.requests.AgePointsRequest;

import java.util.List;

public record RankingScoreRules(
      CharacterTraitScoreRules characterTraitScoreRules,
      List<AgePointsRequest> age
) {
}
