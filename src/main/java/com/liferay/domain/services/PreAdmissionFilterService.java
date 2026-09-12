package com.liferay.domain.services;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.domain.models.admissions.PreAdmissionResolution;
import com.liferay.domain.models.admissions.PreFilteredApplications;

import java.util.List;
import java.util.logging.Logger;

public class PreAdmissionFilterService {
    private final Logger log = Logger.getLogger(PreAdmissionFilterService.class.getName());
    private final BanningService banningService;

    public PreAdmissionFilterService(final BanningService banningService) {
        this.banningService = banningService;
    }

    public PreAdmissionResolution resolve(final List<Application> applications, final CouncilPolicy councilPolicy) {
        log.fine("Initial admission filtering");

        final List<Application> invited = extractInvitedApplications(applications, councilPolicy);
        final List<Application> remainingCandidates = extractNonInvitedCandidatesApplications(applications, invited);

        final PreFilteredApplications preFilteredApplications =
              banningService.filterApplications(remainingCandidates, councilPolicy.admissionRules());

        log.fine(
              String.format("Initial admission filter finished. Invited: %d; Candidates: %d; Banned: %d",
                    invited.size(),
                    preFilteredApplications.candidates().size(),
                    preFilteredApplications.banned().size())
        );

        return new PreAdmissionResolution(invited, preFilteredApplications.candidates(), preFilteredApplications.banned());
    }

    private List<Application> extractInvitedApplications(
          final List<Application> applications, final CouncilPolicy councilPolicy) {
        return councilPolicy.invitations().stream()
              .flatMap(fullName ->
                    applications.stream()
                          .filter(application -> fullName.equals(formatFullName(application)))
              )
              .toList();
    }

    private static String formatFullName(final Application application) {
        return String.format("%s %s", application.firstName(), application.familyName());
    }

    private List<Application> extractNonInvitedCandidatesApplications(
          final List<Application> applications, final List<Application> invited) {
        return applications.stream()
              .filter(application -> !invited.contains(application))
              .toList();
    }
}
