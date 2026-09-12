package com.liferay.domain.models;

public record HouseRule(
      String name,
      int beds,
      CharacterTraitScoreRules characterTraitScoreRules
) {
}
