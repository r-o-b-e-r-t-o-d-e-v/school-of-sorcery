package com.liferay.domain.models;

import java.util.Map;

public record CharacterTraitScoreRules(
      Map<String, Integer> virtue,
      Map<String, Integer> family,
      Map<String, Integer> weakness
) {
}
