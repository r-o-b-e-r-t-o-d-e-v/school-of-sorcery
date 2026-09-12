package com.liferay.domain.models;

import java.util.List;

public record CouncilPolicy(
      String year,
      List<String> invitations,
      int places,
      AdmissionRules admissionRules,
      RankingScoreRules rankingScoreRules,
      HousingScoreRules housingScoreRules
) {
}
