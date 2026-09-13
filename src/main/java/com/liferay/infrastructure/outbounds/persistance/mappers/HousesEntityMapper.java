package com.liferay.infrastructure.outbounds.persistance.mappers;

import com.liferay.domain.models.House;
import com.liferay.domain.models.housing.HouseAssignationBook;
import com.liferay.domain.models.housing.HouseResidents;
import com.liferay.infrastructure.outbounds.persistance.entities.HouseAssignmentEntity;
import com.liferay.infrastructure.outbounds.persistance.entities.HouseBeddingEntity;
import com.liferay.infrastructure.outbounds.persistance.entities.HouseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HousesEntityMapper {

    private final StudentEntityMapper studentEntityMapper;

    public List<HouseEntity> mapHouses(final List<House> houses) {
        return houses.stream().map(this::mapHouse).toList();
    }

    public HouseEntity mapHouse(final House house) {
        return HouseEntity.builder()
              .name(house.name())
              .build();
    }

    public List<HouseAssignmentEntity> mapHouseAssignments(final String academicYear, final HouseAssignationBook houseAssignationBook) {
        return houseAssignationBook.stream()
              .map(residents -> mapHouseResidents(academicYear, residents))
              .flatMap(List::stream)
              .toList();
    }

    private List<HouseAssignmentEntity> mapHouseResidents(final String academicYear, final HouseResidents houseResidents) {
        final HouseEntity houseEntity = mapHouse(houseResidents.house());

        return houseResidents.students().stream()
              .map(student ->
                    HouseAssignmentEntity.builder()
                          .academicYear(academicYear)
                          .house(houseEntity)
                          .student(studentEntityMapper.map(student))
                          .build()
              )
              .toList();
    }

    public List<HouseBeddingEntity> mapHouseBedding(final String academicYear, final List<House> house) {
        return house.stream()
              .map(houseEntity -> mapHouseBedding(academicYear, houseEntity))
              .toList();
    }

    public HouseBeddingEntity mapHouseBedding(final String academicYear, final House house) {
        return HouseBeddingEntity.builder()
              .academicYear(academicYear)
              .house(mapHouse(house))
              .totalBeds(house.beds())
              .build();
    }
}
