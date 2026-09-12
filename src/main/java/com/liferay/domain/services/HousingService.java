package com.liferay.domain.services;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.HousingScoreRules;
import com.liferay.domain.models.housing.HouseAssignation;
import com.liferay.domain.models.housing.StudentHouseScorings;

import java.util.List;
import java.util.logging.Logger;

public class HousingService {
    private final Logger log = Logger.getLogger(HousingService.class.getName());

    private final HouseScoringService houseScoringService;
    private final HousingAssignationService housingAssignationService;

    public HousingService(
          final HouseScoringService houseScoringService, final HousingAssignationService housingAssignationService) {
        this.houseScoringService = houseScoringService;
        this.housingAssignationService = housingAssignationService;
    }

    public List<HouseAssignation> processHousing(
          final List<Application> acceptedStudents, final HousingScoreRules housingScoreRules) {

        log.fine("Housing process starting");

        // Calculate each student's scoring per houses
        final List<StudentHouseScorings> studentHouseScorings =
              houseScoringService.getHouseScoring(acceptedStudents, housingScoreRules);

        // Houses election based on available beds > highest scoring > input order
        return housingAssignationService.assignHouses(housingScoreRules.houses(), studentHouseScorings);
    }
}
