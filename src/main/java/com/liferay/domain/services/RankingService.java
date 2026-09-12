package com.liferay.domain.services;

import com.liferay.domain.models.AgePoints;
import com.liferay.domain.models.Application;
import com.liferay.domain.models.RankingScoreRules;
import com.liferay.domain.models.admissions.ApplicationScore;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

// TODO For the moment, I won't include invited students along with the rest of candidates
public class RankingService {
    private final Logger log = Logger.getLogger(RankingService.class.getName());

    public static class OrderedRanking {
        private final List<ApplicationScore> applicationScores;

        private OrderedRanking(final List<ApplicationScore> applicationScores) {
            this.applicationScores = applicationScores;
        }

        public List<ApplicationScore> getApplicationScores() {
            return applicationScores;
        }
    }

    public OrderedRanking calculateRanking(
          final List<Application> candidates, final RankingScoreRules rankingScoreRules) {

        log.fine("Ranking applications starting");

        log.fine("Calculating ranking scores");
        // Calculating scores
        final List<ApplicationScore> applicationScores = calculateScores(candidates, rankingScoreRules);

        log.fine("Sorting ranking scores");
        // Sorting the ranking
        return new OrderedRanking(sortRanking(applicationScores));
    }

    private List<ApplicationScore> calculateScores(
          final List<Application> candidates, final RankingScoreRules rankingScoreRules) {
        return candidates.stream()
              .map(application -> {
                  // Weakness values are supposed to already come with the minus sign, however, if they
                  // are introduced without the sign they may alter the results of the ranking, so I feel
                  // it's better to ignore the sign they are coming with and always assure a subtract operation
                  final int newScore =
                        calculateNewPointsByVirtue(rankingScoreRules, application)
                              + calculateNewPointsByFamily(rankingScoreRules, application)
                              - calculateNewPointsByWeaknesses(rankingScoreRules, application)
                              + calculateNewPointsByAge(rankingScoreRules, application);
                  return new ApplicationScore(application, newScore);
              })
              .toList();
    }

    private int calculateNewPointsByVirtue(final RankingScoreRules rankingScoreRules, final Application application) {
        return application.virtues().stream()
              .mapToInt(virtue -> rankingScoreRules.characterTraitScoreRules().virtue().getOrDefault(virtue, 0))
              .sum();
    }

    private int calculateNewPointsByFamily(final RankingScoreRules rankingScoreRules, final Application application) {
        return rankingScoreRules.characterTraitScoreRules().family().getOrDefault(application.familyName(), 0);
    }

    private int calculateNewPointsByWeaknesses(final RankingScoreRules rankingScoreRules, final Application application) {
        return application.weaknesses().stream()
              .mapToInt(weakness -> rankingScoreRules.characterTraitScoreRules().weakness().getOrDefault(weakness, 0))
              .map(Math::abs)
              .sum();
    }

    private int calculateNewPointsByAge(final RankingScoreRules rankingScoreRules, final Application application) {
        return rankingScoreRules.age().stream()
              .filter(agePoint -> application.age() >= agePoint.from() && application.age() <= agePoint.to())
              .findFirst()
              .map(AgePoints::points)
              .orElse(0);
    }

    private List<ApplicationScore> sortRanking(final List<ApplicationScore> applicationScores) {
        return applicationScores.stream()
              .sorted(
                    Comparator
                          .comparingInt(ApplicationScore::score)
                          .reversed()
                          .thenComparingInt(applicationScore -> applicationScore.application().age())
                          .thenComparing(applicationScore -> applicationScore.application().familyName())
                          .thenComparing(applicationScore -> applicationScore.application().firstName())
              )
              .toList();
    }
}
