package com.liferay.domain.models;

public record House(
      String name,
      int beds,
      CharacterTraitScoreRules characterTraitScoreRules
) {
}
