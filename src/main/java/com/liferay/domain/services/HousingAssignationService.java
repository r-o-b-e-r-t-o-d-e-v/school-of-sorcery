package com.liferay.domain.services;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.House;
import com.liferay.domain.models.housing.HouseAssignationBook;
import com.liferay.domain.models.housing.HouseResidents;
import com.liferay.domain.models.housing.StudentHouseScorings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

// Houses election based on available beds > highest scoring > input order
public class HousingAssignationService {
    private final Logger log = Logger.getLogger(HousingAssignationService.class.getName());

    public HouseAssignationBook assignHouses(
          final List<House> houses, final List<StudentHouseScorings> studentHouseScorings) {

        log.fine("Housing assignation starting");

        final HouseAssignationBook houseAssignationBook =
              (HouseAssignationBook) houses.stream()
                    .map(house -> new HouseResidents(house, new ArrayList<>()))
                    .toList();

        studentHouseScorings.forEach(studentHouseScoring ->
              handleHouseAssignation(
                    studentHouseScoring.application(),
                    studentHouseScoring.housesScoring(),
                    houseAssignationBook
              )
        );

        return houseAssignationBook;
    }

    private void handleHouseAssignation(
          final Application student,
          final Map<House, Integer> housesScoring,
          final HouseAssignationBook houseAssignationBook) {

        final List<House> availableHouses = getHousesWithAvailableBeds(houseAssignationBook);
        final House chosenHouse = getHighestScoredHouse(availableHouses, housesScoring);

        assignHouse(chosenHouse, student, houseAssignationBook);
    }

    private List<House> getHousesWithAvailableBeds(final HouseAssignationBook houseAssignationBook) {
        return houseAssignationBook.stream()
              .filter(houseResidents ->
                    houseResidents.students().size() < houseResidents.house().beds())
              .map(HouseResidents::house)
              .toList();
    }

    // Get the highest score house, keeping priority for the first one
    // in the list (which is the first one in the list of housesScoring)
    private House getHighestScoredHouse(final List<House> availableHouses, final Map<House, Integer> housesScoring) {
        return availableHouses.stream()
              .reduce((house1, house2) ->
                    housesScoring.get(house1) >= housesScoring.get(house2)
                          ? house1
                          : house2)
              .orElseThrow();
    }

    private void assignHouse(
          final House chosenHouse, final Application student, final HouseAssignationBook houseAssignationBook) {
        houseAssignationBook.stream()
              .filter(houseResidents -> houseResidents.house().equals(chosenHouse))
              .findFirst()
              .orElseThrow()
              .students().add(student);
    }
}
