package com.liferay.domain.services;


import com.liferay.domain.models.AdmissionRules;
import com.liferay.domain.models.Application;
import com.liferay.domain.models.admissions.BannedApplication;
import com.liferay.domain.models.DateRange;
import com.liferay.domain.models.IntRange;
import com.liferay.domain.models.admissions.PreFilteredApplications;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BanningService {
    final Logger log = Logger.getLogger(BanningService.class.getName());

    public PreFilteredApplications filterApplications(final List<Application> applications, final AdmissionRules admissionRules) {
        final List<Application> candidates = new ArrayList<>();
        final List<BannedApplication> bannedApplications = new ArrayList<>();

        // TODO Ban reason can be improved
        applications.forEach(application -> {
            if (isBannedByFamily(admissionRules, application)) {
                bannedApplications.add(new BannedApplication(application, "Banned family"));
            } else if (isBannedByAgeRange(admissionRules, application)) {
                bannedApplications.add(new BannedApplication(application, "Out of age range"));
            } else if (isBannedByWeakness(admissionRules, application)) {
                bannedApplications.add(new BannedApplication(application, "Unacceptable weakness"));
            } else if (isBannedByDateRange(admissionRules, application)) {
                bannedApplications.add(new BannedApplication(application, "Out of date range"));
            } else {
                candidates.add(application);
            }
        });

        return new PreFilteredApplications(candidates, bannedApplications);
    }

    private static boolean isBannedByFamily(final AdmissionRules admissionRules, final Application application) {
        return admissionRules.bannedFamilies().contains(application.familyName());
    }

    private boolean isBannedByAgeRange(final AdmissionRules admissionRules, final Application application) {
        return isOutOfAgeRange(admissionRules.ageRange(), application.age());
    }

    private boolean isOutOfAgeRange(final IntRange ageRange, final int age) {
        return age < ageRange.min() || age > ageRange.max();
    }

    private static boolean isBannedByWeakness(final AdmissionRules admissionRules, final Application application) {
        return admissionRules.unacceptableWeaknesses().stream()
              .anyMatch(application.weaknesses()::contains);
    }

    private boolean isBannedByDateRange(final AdmissionRules admissionRules, final Application application) {
        return isOutOfDateRange(admissionRules.applicationDates(), application.applicationDate());
    }

    private boolean isOutOfDateRange(final DateRange applicationDates, final LocalDate localDate) {
        return localDate.isAfter(applicationDates.to()) || localDate.isBefore(applicationDates.from());
    }
}
