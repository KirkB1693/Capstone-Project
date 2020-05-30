package com.example.android.baseballbythenumbers.generators;

import android.content.Context;
import android.widget.ProgressBar;

import com.example.android.baseballbythenumbers.data.League;
import com.example.android.baseballbythenumbers.data.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationGenerator {

    Context context;

    private static final int DEFAULT_LEAGUES = 2;

    private static final int DEFAULT_DIVISION_SIZE = 5;

    private static final int DEFAULT_NUMBER_OF_DIVISIONS = 3;

    private static final String DEFAULT_LEAGUE_NAME = "League ";

    private static final int[] DEFAULT_TEAM_MAKEUP_WITH_DH = new int[]{5, 3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1};

    private static final int[] DEFAULT_TEAM_MAKEUP_WITHOUT_DH = new int[]{5, 3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 0};

    public OrganizationGenerator(Context context) {
        this.context = context;
    }

    public Organization generateOrganization(String organizationName, int currentYear, boolean interleaguePlay, int seriesLength,int numberOfLeagues, List<String> leagueNames, List<Boolean> useDH, int numOfTeamsPerDivision,
                                             int numberOfDivisions, int countriesToInclude, int[] teamMakeup, String userTeamName, String userTeamCity, ProgressBar progressBar) {
        List<League> leagues = new ArrayList<>();
        LeagueGenerator leagueGenerator = new LeagueGenerator(context);

        TeamNameGenerator teamNameGenerator = new TeamNameGenerator(context);
        teamNameGenerator.removeUserTeamNameFromList(userTeamName);  // Makes sure that another team doesn't have the same name as the user picked
        teamNameGenerator.addUserTeamNameToList(userTeamName);      // Adds the name where it won't be picked
        CityGenerator cityGenerator = new CityGenerator(context, numberOfDivisions, countriesToInclude);

        if (numberOfLeagues < 1 || numberOfLeagues > 4) {
            numberOfLeagues = DEFAULT_LEAGUES;
        }

        if (useDH.isEmpty() || useDH.size() != numberOfLeagues) {
            for (int j = 0; j < numberOfLeagues; j++) {
                if (useDH.get(j) == null) {
                    useDH.add(j, false);
                }
            }
        }

        if (leagueNames.isEmpty()) {
            for (int i = 0; i < numberOfLeagues; i++) {
                leagueNames.add(DEFAULT_LEAGUE_NAME + (i + 1));
            }
        }

        if (numOfTeamsPerDivision < 3 || numOfTeamsPerDivision > 8) {
            numOfTeamsPerDivision = DEFAULT_DIVISION_SIZE;
        }
        if (numberOfDivisions < 0 || numberOfDivisions > 4) {
            numberOfDivisions = DEFAULT_NUMBER_OF_DIVISIONS;
        }




        Organization newOrganization = new Organization(organizationName, userTeamName, currentYear, interleaguePlay, seriesLength,null, null);

        for (int i = 0; i < numberOfLeagues; i++) {
            if (useDH.get(i)) {
                teamMakeup = DEFAULT_TEAM_MAKEUP_WITH_DH;
            } else {
                teamMakeup = DEFAULT_TEAM_MAKEUP_WITHOUT_DH;
            }
            if (i < leagueNames.size()) {
                leagues.add(leagueGenerator.generateLeague(leagueNames.get(i), useDH.get(i), numOfTeamsPerDivision, numberOfDivisions, teamMakeup,
                        cityGenerator, teamNameGenerator, newOrganization.getId(), userTeamName, userTeamCity, progressBar));
            } else {
                leagues.add(leagueGenerator.generateLeague(DEFAULT_LEAGUE_NAME + (i + 1), useDH.get(i), numOfTeamsPerDivision, numberOfDivisions,
                        teamMakeup, cityGenerator, teamNameGenerator, newOrganization.getId(), userTeamName, userTeamCity, progressBar));
            }

        }

        newOrganization.setLeagues(leagues);

        return newOrganization;
    }

    public interface buildProgressStatus{
        void onProgressUpdate(int progressStatus);
    }
}
