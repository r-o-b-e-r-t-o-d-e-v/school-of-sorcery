package com.liferay.domain.services;

import com.liferay.domain.models.Application;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.domain.models.admissions.AdmissionResolution;
import com.liferay.domain.models.admissions.ApplicationResolution;
import com.liferay.domain.models.admissions.ApplicationStatus;
import com.liferay.domain.models.admissions.BannedApplication;
import com.liferay.domain.models.admissions.PreAdmissionResolution;
import com.liferay.domain.utils.Constants;

import java.util.List;
import java.util.logging.Logger;

public class AdmissionService {
    private final Logger log = Logger.getLogger(AdmissionService.class.getName());

    final PreAdmissionFilterService preAdmissionFilterService;
    final RankingService rankingService;

    public AdmissionService(
          final PreAdmissionFilterService preAdmissionFilterService,
          final RankingService rankingService
    ) {
        this.preAdmissionFilterService = preAdmissionFilterService;
        this.rankingService = rankingService;
    }

    public AdmissionResolution processAdmission(final List<Application> applications, final CouncilPolicy councilPolicy) {
        log.fine("Admission process starting");

        final PreAdmissionResolution preAdmissionResolution = preAdmissionFilterService.resolve(applications, councilPolicy);

        final RankingService.OrderedRanking applicationsRanking = rankingService.calculateRanking(
              preAdmissionResolution.candidates(), councilPolicy.rankingScoreRules());

        log.fine("Processing admission resolution");
        final AdmissionResolution admissionResolution = resolveAdmissions(
              councilPolicy,
              preAdmissionResolution.invited(),
              preAdmissionResolution.bannedApplications(),
              applicationsRanking
        );

        log.fine("Admission resolution finished" + admissionResolution);
        return admissionResolution;
    }

    private AdmissionResolution resolveAdmissions(
          final CouncilPolicy councilPolicy,
          final List<Application> invitations,
          final List<BannedApplication> bannedApplications,
          final RankingService.OrderedRanking orderedRanking
    ) {
        final int availablePlaces = councilPolicy.places() - invitations.size();

        final List<ApplicationResolution> invited = getInvitedApplicationsResolution(invitations);
        final List<ApplicationResolution> accepted = getAcceptedApplicationsResolution(availablePlaces, orderedRanking);
        final List<ApplicationResolution> rejected = getRejectedApplicationsResolution(availablePlaces, orderedRanking);
        final List<ApplicationResolution> banned = getBannedApplicationsResolution(bannedApplications);

        return new AdmissionResolution(councilPolicy.year(), invited, accepted, rejected, banned);
    }

    private List<ApplicationResolution> getInvitedApplicationsResolution(final List<Application> invitations) {

        return invitations.stream()
              .map(invited -> new ApplicationResolution(
                    invited,
                    ApplicationStatus.INVITED,
                    null,
                    null)
              )
              .toList();
    }

    private List<ApplicationResolution> getAcceptedApplicationsResolution(
          final int availablePlaces, final RankingService.OrderedRanking orderedRanking) {

        return orderedRanking.getApplicationScores().stream()
              .limit(availablePlaces)
              .map(accepted -> new ApplicationResolution(
                    accepted.application(),
                    ApplicationStatus.ACCEPTED,
                    null,
                    accepted.score())
              )
              .toList();
    }

    private List<ApplicationResolution> getRejectedApplicationsResolution(
          final int availablePlaces, final RankingService.OrderedRanking orderedRanking) {

        return orderedRanking.getApplicationScores().stream()
              .skip(availablePlaces)
              .map(rejected -> new ApplicationResolution(
                    rejected.application(),
                    ApplicationStatus.REJECTED,
                    Constants.REJECTION_REASON_LOW_SCORES,
                    rejected.score())
              )
              .toList();
    }

    private List<ApplicationResolution> getBannedApplicationsResolution(
          final List<BannedApplication> bannedApplications) {

        return bannedApplications.stream()
              .map(banned -> new ApplicationResolution(
                    banned.application(),
                    ApplicationStatus.BANNED,
                    banned.banReason(),
                    null)
              )
              .toList();
    }
}
