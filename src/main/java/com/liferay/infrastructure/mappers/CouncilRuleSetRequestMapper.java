package com.liferay.infrastructure.mappers;

import com.liferay.domain.models.AdmissionRules;
import com.liferay.domain.models.CharacterTraitScoreRules;
import com.liferay.domain.models.CouncilPolicy;
import com.liferay.domain.models.House;
import com.liferay.domain.models.HousingScoreRules;
import com.liferay.domain.models.RankingScoreRules;
import com.liferay.infrastructure.dtos.requests.CouncilRuleSetRequest;
import com.liferay.infrastructure.dtos.requests.HouseRequest;
import com.liferay.infrastructure.dtos.requests.PointsRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class CouncilRuleSetRequestMapper {

    private CouncilRuleSetRequestMapper() {
    }

    public CouncilPolicy map(final CouncilRuleSetRequest councilRuleSetRequest) {
        return new CouncilPolicy(
              councilRuleSetRequest.year(),
              councilRuleSetRequest.invitations(),
              councilRuleSetRequest.places(),
              mapAdmissionRules(councilRuleSetRequest),
              mapRankingScoreRules(councilRuleSetRequest.points()),
              mapHousingScoreRules(councilRuleSetRequest.houses())
        );
    }

    private AdmissionRules mapAdmissionRules(final CouncilRuleSetRequest councilRuleSetRequest) {
        return new AdmissionRules(
              councilRuleSetRequest.applicationDates(),
              councilRuleSetRequest.ageRange(),
              councilRuleSetRequest.bannedFamilies(),
              councilRuleSetRequest.unacceptableWeaknesses()
        );
    }

    private RankingScoreRules mapRankingScoreRules(final PointsRequest points) {
        return new RankingScoreRules(
              mapCharacterTraitScoreRules(points),
              points.age()
        );
    }

    private HousingScoreRules mapHousingScoreRules(final List<HouseRequest> houseRequests) {
        return new HousingScoreRules(
              houseRequests.stream()
                    .map(this::mapHouseRule)
                    .toList()
        );
    }

    private House mapHouseRule(HouseRequest houseRequest) {
        return new House(
              houseRequest.name(),
              houseRequest.beds(),
              mapCharacterTraitScoreRules(houseRequest)
        );
    }

    private CharacterTraitScoreRules mapCharacterTraitScoreRules(final PointsRequest pointsRequest) {
        return new CharacterTraitScoreRules(
              pointsRequest.virtue(),
              pointsRequest.family(),
              pointsRequest.weakness()
        );
    }

    private CharacterTraitScoreRules mapCharacterTraitScoreRules(final HouseRequest houseRequest) {
        return new CharacterTraitScoreRules(
              houseRequest.points().virtue(),
              houseRequest.points().family(),
              houseRequest.points().weakness()
        );
    }
}
