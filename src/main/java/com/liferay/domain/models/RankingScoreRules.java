package com.liferay.domain.models;

import java.util.List;

public record RankingScoreRules(
      CharacterTraitScoreRules characterTraitScoreRules,
      List<AgePoints> age
) {
}
