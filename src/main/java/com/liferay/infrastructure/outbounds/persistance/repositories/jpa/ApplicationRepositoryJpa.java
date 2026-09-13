package com.liferay.infrastructure.outbounds.persistance.repositories.jpa;

import com.liferay.domain.models.admissions.ApplicationStatus;
import com.liferay.infrastructure.dtos.responses.AcceptedApplicationRankResponse;
import com.liferay.infrastructure.outbounds.persistance.entities.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ApplicationRepositoryJpa extends JpaRepository<ApplicationEntity, Long> {
    boolean existsByAcademicYear(final String academicYear);

    @Query(value = """
          SELECT
              students.name AS name,
              students.family_name AS familyName,
              applications.score AS score,
              applications.status AS status,
              houses.name AS house
          FROM applications
          JOIN students
              ON students.id = applications.student_id
          LEFT JOIN house_assignments
              ON house_assignments.student_id = applications.student_id
             AND house_assignments.academic_year = applications.academic_year
          LEFT JOIN houses
              ON houses.id = house_assignments.house_id
          WHERE applications.academic_year = :academicYear
            AND applications.status IN ('INVITED', 'ACCEPTED')
          ORDER BY
              applications.status DESC,
              applications.score DESC,
              applications.age_at_application ASC,
              students.family_name ASC,
              students.name ASC
          """, nativeQuery = true)
    List<AcceptedApplicationRankResponse> findAllAcceptedRankingsWithHouses(final String academicYear);

    List<ApplicationEntity> findAllByAcademicYearAndStatusInOrderByScoreDescAgeAtApplicationAscStudentFamilyNameAscStudentNameAsc(
          final String academicYear, final Collection<ApplicationStatus> statuses);
}
