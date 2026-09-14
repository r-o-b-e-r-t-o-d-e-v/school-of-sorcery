package com.liferay.domain.services;


import com.liferay.domain.models.AdmissionRules;
import com.liferay.domain.models.Application;
import com.liferay.domain.models.DateRange;
import com.liferay.domain.models.IntRange;
import com.liferay.domain.models.admissions.BannedApplication;
import com.liferay.domain.models.admissions.PreFilteredApplications;
import com.liferay.domain.utils.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BanningService {
    final Logger log = Logger.getLogger(BanningService.class.getName());

    public PreFilteredApplications filterApplications(final List<Application> applications, final AdmissionRules admissionRules) {
        final List<Application> candidates = new ArrayList<>();
        final List<BannedApplication> bannedApplications = new ArrayList<>();

        applications.forEach(application -> {
            if (isBannedByFamily(admissionRules, application)) {
                bannedApplications.add(new BannedApplication(application, Constants.BAN_REASON_BANNED_FAMILY));
            } else if (isBannedByAgeRange(admissionRules, application)) {
                bannedApplications.add(new BannedApplication(application, Constants.BAN_REASON_OUT_OF_AGE_RANGE));
            } else if (isBannedByWeakness(admissionRules, application)) {
                bannedApplications.add(new BannedApplication(application, Constants.BAN_REASON_UNACCEPTABLE_WEAKNESS));
            } else if (isBannedByDateRange(admissionRules, application)) {
                bannedApplications.add(new BannedApplication(application, Constants.BAN_REASON_OUT_OF_DATE_RANGE));
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
