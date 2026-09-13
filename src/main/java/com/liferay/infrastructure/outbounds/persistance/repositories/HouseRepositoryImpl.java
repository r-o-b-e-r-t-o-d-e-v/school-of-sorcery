package com.liferay.infrastructure.outbounds.persistance.repositories;

import com.liferay.domain.interfaces.HouseRepository;
import com.liferay.domain.models.House;
import com.liferay.domain.models.housing.HouseAssignationBook;
import com.liferay.domain.models.housing.HouseResidents;
import com.liferay.infrastructure.dtos.responses.HouseDetailResponse;
import com.liferay.infrastructure.outbounds.persistance.entities.HouseAssignmentEntity;
import com.liferay.infrastructure.outbounds.persistance.entities.HouseBeddingEntity;
import com.liferay.infrastructure.outbounds.persistance.entities.HouseEntity;
import com.liferay.infrastructure.outbounds.persistance.entities.StudentEntity;
import com.liferay.infrastructure.outbounds.persistance.mappers.HousesEntityMapper;
import com.liferay.infrastructure.outbounds.persistance.repositories.jpa.HousesAssignmentRepositoryJpa;
import com.liferay.infrastructure.outbounds.persistance.repositories.jpa.HousesBeddingRepositoryJpa;
import com.liferay.infrastructure.outbounds.persistance.repositories.jpa.HousesRepositoryJpa;
import com.liferay.infrastructure.outbounds.persistance.repositories.jpa.StudentRepositoryJpa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class HouseRepositoryImpl implements HouseRepository {

    private final Logger log = Logger.getLogger(HouseRepositoryImpl.class.getName());
    private final HousesRepositoryJpa housesRepositoryJpa;
    private final HousesAssignmentRepositoryJpa housesAssignmentRepositoryJpa;
    private final HousesBeddingRepositoryJpa housesBeddingRepositoryJpa;
    private final StudentRepositoryJpa studentRepositoryJpa;
    private final HousesEntityMapper housesEntityMapper;

    @Override
    @Transactional
    public void save(final String academicYear, final HouseAssignationBook houseAssignationBook) {
        log.fine("Saving house assignments");

        final List<House> houses = houseAssignationBook.stream().map(HouseResidents::house).toList();
        final List<HouseEntity> houseEntities = housesEntityMapper.mapHouses(houses);

        final Map<String, Long> houseIdsByName = upsertHouses(houseEntities);
        saveHouseBeddings(academicYear, houses, houseIdsByName);
        saveHouseAssignments(academicYear, houseAssignationBook, houseIdsByName);

        log.fine("House assignments saved");
    }

    @Override
    public List<String> getAvailableHouses(final String academicYear) {
        return housesBeddingRepositoryJpa.findAllByAcademicYear(academicYear).stream()
              .map(HouseBeddingEntity::getHouse)
              .map(HouseEntity::getName)
              .toList();
    }

    @Override
    public Optional<HouseDetailResponse> getHouseDetail(final String academicYear, final String houseName) {
        return housesBeddingRepositoryJpa
              .findByAcademicYearAndHouseName(academicYear, houseName)
              .map(bedding -> {
                  final List<String> assignedStudents = housesAssignmentRepositoryJpa
                        .findByAcademicYearAndHouseName(academicYear, houseName)
                        .stream()
                        .map(this::formatFullName)
                        .toList();

                  return new HouseDetailResponse(
                        bedding.getHouse().getName(),
                        bedding.getTotalBeds(),
                        assignedStudents
                  );
              });
    }

    // Save the houses that don't exist in DB.
    // Then updates the houses ids. This is because when saving the house assignments,
    // the houses will be created twice since their ids will always be null.
    private Map<String, Long> upsertHouses(final List<HouseEntity> houseEntities) {
        final List<String> houseEntitiesNames =
              houseEntities.stream().map(HouseEntity::getName).toList();

        final List<HouseEntity> existingHouseEntities =
              housesRepositoryJpa.findAllByNameIn(houseEntitiesNames);

        final Map<String, HouseEntity> houseEntitiesByName =
              houseEntities.stream()
                    .collect(Collectors.toMap(
                          HouseEntity::getName,
                          Function.identity()
                    ));

        for (final HouseEntity house : existingHouseEntities) {
            houseEntitiesByName.get(house.getName()).setId(house.getId());
        }

        return housesRepositoryJpa.saveAll(houseEntities).stream()
              .collect(Collectors.toMap(HouseEntity::getName, HouseEntity::getId));
    }

    private void saveHouseBeddings(final String academicYear, final List<House> houses, final Map<String, Long> houseIdsByName) {
        final List<HouseBeddingEntity> houseBeddingEntities = housesEntityMapper.mapHouseBedding(academicYear, houses);

        houseBeddingEntities.forEach(houseBeddingEntity ->
              houseBeddingEntity.getHouse().setId(houseIdsByName.get(houseBeddingEntity.getHouse().getName())));

        housesBeddingRepositoryJpa.saveAll(houseBeddingEntities);
    }

    private void saveHouseAssignments(final String academicYear, final HouseAssignationBook houseAssignationBook, final Map<String, Long> houseIdsByName) {
        final List<HouseAssignmentEntity> houseAssignmentEntities =
              housesEntityMapper.mapHouseAssignments(academicYear, houseAssignationBook);

        houseAssignmentEntities.forEach(houseAssignmentEntity ->
              houseAssignmentEntity.getHouse().setId(houseIdsByName.get(houseAssignmentEntity.getHouse().getName())));


        final List<StudentEntity> studentEntities = houseAssignmentEntities.stream()
              .map(HouseAssignmentEntity::getStudent)
              .toList();

        setExistingStudentsIds(studentEntities);

        housesAssignmentRepositoryJpa.saveAll(houseAssignmentEntities);
    }

    // TODO this can be extracted since ApplicationRepositoryImpl is also using it
    // Updates the student entity ids. This is because when saving the application resolutions,
    // the entities will always have their id as null (the sequential id, not the external id).
    // So the students will always be created even if they already exist.
    // To fix that, we recover the existing ones and set their ids in the entities.
    // JPA will later insert the non-existing entities and respect the ones with id.
    private void setExistingStudentsIds(final List<StudentEntity> studentEntities) {
        final Map<String, StudentEntity> studentsByExternalId =
              studentEntities.stream()
                    .collect(Collectors.toMap(
                          StudentEntity::getExternalId,
                          Function.identity()
                    ));

        final List<String> studentEntitiesExternalIds =
              studentEntities.stream().map(StudentEntity::getExternalId).toList();

        final List<StudentEntity> existingStudentEntities =
              studentRepositoryJpa.findAllByExternalIdIn(studentEntitiesExternalIds);

        for (final StudentEntity student : existingStudentEntities) {
            studentsByExternalId.get(student.getExternalId()).setId(student.getId());
        }
    }

    // TODO improve this
    private String formatFullName(final HouseAssignmentEntity houseAssignmentEntity) {
        return String.format("%s %s", houseAssignmentEntity.getStudent().getName(), houseAssignmentEntity.getStudent().getFamilyName());
    }
}
