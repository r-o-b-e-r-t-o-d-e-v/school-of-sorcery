package com.liferay.domain.services;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.House;
import com.liferay.domain.models.HousingScoreRules;
import com.liferay.domain.models.housing.StudentHouseScorings;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HouseScoringService {
    private final Logger log = Logger.getLogger(HouseScoringService.class.getName());

    public List<StudentHouseScorings> getHouseScoring(
          final List<Application> acceptedStudents, final HousingScoreRules housingScoreRules) {
        log.fine("Calculating house scores for students");

        return calculateScores(acceptedStudents, housingScoreRules);
    }

    private List<StudentHouseScorings> calculateScores(
          final List<Application> students, final HousingScoreRules housingScoreRules) {

        return students.stream()
              .map(student ->
                    new StudentHouseScorings(student, calculateHouseScoringForStudent(student, housingScoreRules))
              )
              .toList();
    }

    private Map<House, Integer> calculateHouseScoringForStudent(
          final Application student, final HousingScoreRules housingScoreRules) {

        return housingScoreRules.houses().stream()
              .collect(Collectors.toMap(Function.identity(), house -> {
                  final int scoreVirtues = getScoreVirtues(student, house);
                  final int scoreFamily = getScoreFamily(student, house);
                  final int scoreWeaknesses = getScoreWeaknesses(student, house);

                  return scoreVirtues + scoreFamily + scoreWeaknesses;
              }));
    }

    private int getScoreVirtues(final Application application, final House house) {
        return application.virtues().stream()
              .mapToInt(virtue ->
                    house.characterTraitScoreRules().virtue().getOrDefault(virtue, 0))
              .sum();
    }

    private int getScoreFamily(final Application application, final House house) {
        return house.characterTraitScoreRules().family().getOrDefault(application.familyName(), 0);
    }

    private int getScoreWeaknesses(final Application application, final House house) {
        return application.weaknesses().stream()
              .mapToInt(weakness ->
                    house.characterTraitScoreRules().weakness().getOrDefault(weakness, 0))
              .sum();
    }
}
