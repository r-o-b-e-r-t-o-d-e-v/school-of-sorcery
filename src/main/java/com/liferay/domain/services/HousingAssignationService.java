package com.liferay.domain.services;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.House;
import com.liferay.domain.models.housing.HouseAssignation;
import com.liferay.domain.models.housing.StudentHouseScorings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// Houses election based on available beds > highest scoring > input order
public class HousingAssignationService {
    private final Logger log = Logger.getLogger(HousingAssignationService.class.getName());

    public List<HouseAssignation> assignHouses(
          final List<House> houses, final List<StudentHouseScorings> studentHouseScorings) {

        log.fine("Housing assignation starting");

        final List<HouseAssignation> houseAssignations =
              houses.stream().map(house -> new HouseAssignation(house, new ArrayList<>())).toList();

        studentHouseScorings.forEach(studentHouseScoring -> {
            final Application student = studentHouseScoring.application();
            final List<House> availableHouses = getHousesWithAvailableBeds(houseAssignations);
            final House chosenHouse = getHighestScoredHouse(availableHouses, studentHouseScoring);
            assignHouse(houseAssignations, chosenHouse, student);
        });

        return houseAssignations;
    }

    private List<House> getHousesWithAvailableBeds(final List<HouseAssignation> houseAssignations) {
        return houseAssignations.stream()
              .filter(houseAssignation ->
                    houseAssignation.students().size() < houseAssignation.house().beds())
              .map(HouseAssignation::house)
              .toList();
    }

    // Get the highest score house, keeping priority for the first one in the list (which is the first one in the list of houses)
    private House getHighestScoredHouse(
          final List<House> availableHouses, final StudentHouseScorings studentHouseScoring) {

        return availableHouses.stream()
              .reduce((house1, house2) ->
                    studentHouseScoring.houses().get(house1)
                          >= studentHouseScoring.houses().get(house2)
                          ? house1
                          : house2)
              .orElseThrow();
    }

    private void assignHouse(final List<HouseAssignation> houseAssignations, final House chosenHouse, final Application student) {
        houseAssignations.stream()
              .filter(houseAssignation -> houseAssignation.house().equals(chosenHouse))
              .findFirst()
              .orElseThrow()
              .students().add(student);
    }
}
