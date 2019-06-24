package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;

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

    public Organization generateOrganization(String organizationName, int currentYear, int numberOfLeagues, String[] leagueNames, boolean[] useDH, int divisionSize, int numberOfDivisions, int countriesToInclude, int[] teamMakeup) {
        List<League> leagues = new ArrayList<>();
        LeagueGenerator leagueGenerator = new LeagueGenerator(context);


        TeamNameGenerator teamNameGenerator = new TeamNameGenerator(context);
        CityGenerator cityGenerator = new CityGenerator(context, numberOfDivisions, countriesToInclude);

        if (numberOfLeagues < 1 || numberOfLeagues > 4) {
            numberOfLeagues = DEFAULT_LEAGUES;
        }

        if (useDH == null) {
            useDH = new boolean[numberOfLeagues];
        }
        if (useDH.length != numberOfLeagues) {
            useDH = new boolean[numberOfLeagues];
        }

        if (leagueNames == null) {
            leagueNames = new String[numberOfLeagues];
            for (int i = 0; i < numberOfLeagues; i++) {
                leagueNames[i] = DEFAULT_LEAGUE_NAME + (i + 1);
            }
        }

        if (divisionSize < 0) {
            divisionSize = DEFAULT_DIVISION_SIZE;
        }
        if (numberOfDivisions < 0) {
            numberOfDivisions = DEFAULT_NUMBER_OF_DIVISIONS;
        }

        Organization newOrganization = new Organization(organizationName, currentYear, null, null);

        for (int i = 0; i < numberOfLeagues; i++) {
            if (useDH[i]) {
                teamMakeup = DEFAULT_TEAM_MAKEUP_WITH_DH;
            } else {
                teamMakeup = DEFAULT_TEAM_MAKEUP_WITHOUT_DH;
            }
            if (i < leagueNames.length) {
                leagues.add(leagueGenerator.generateLeague(leagueNames[i], useDH[i], divisionSize, numberOfDivisions, countriesToInclude, teamMakeup, cityGenerator, teamNameGenerator, newOrganization.getId()));
            } else {
                leagues.add(leagueGenerator.generateLeague(DEFAULT_LEAGUE_NAME+(i+1), useDH[i], divisionSize, numberOfDivisions, countriesToInclude, teamMakeup, cityGenerator, teamNameGenerator, newOrganization.getId()));
            }

        }

        newOrganization.setLeagues(leagues);

        return newOrganization;
    }
}
