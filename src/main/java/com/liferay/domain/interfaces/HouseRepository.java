package com.liferay.domain.interfaces;

import com.liferay.domain.models.housing.HouseAssignationBook;

public interface HouseRepository {
    void save(final String academicYear, final HouseAssignationBook houseAssignationBook);
}
