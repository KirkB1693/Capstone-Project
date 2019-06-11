package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.List;

public class DivisionGenerator {

    private Context context;
    private static final int[] DEFAULT_TEAM_MAKEUP = new int[]{5, 3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1};
    private static final int DEFAULT_BUDGET = 1000000;
    private CityGenerator cityGenerator;
    private TeamNameGenerator teamNameGenerator;

    public DivisionGenerator(Context context) {
        this.context = context;
    }

    public Division generateDivision(String divisionName, boolean doesDivisionUseDH, int divisionSize, int[] teamMakeup, int numberOfDivisions, int countriesToInclude) {
        Division newDivision = new Division();
        newDivision.setDivisionName(divisionName);
        int[] teamMakeupToUse = DEFAULT_TEAM_MAKEUP;
        teamNameGenerator = new TeamNameGenerator(context);
        cityGenerator = new CityGenerator(context, numberOfDivisions, countriesToInclude);

        TeamGenerator teamGenerator;

        if (teamMakeup != null) {
            if (teamMakeup.length == 15) {
                for (int i = 0; i < teamMakeup.length; i++) {
                    teamMakeupToUse[i] = teamMakeup [i];
                }
            }
        }

        teamGenerator = new TeamGenerator(context, teamMakeupToUse[0], teamMakeupToUse[1], teamMakeupToUse[2], teamMakeupToUse[3], teamMakeupToUse[4], teamMakeupToUse[5],
                teamMakeupToUse[6], teamMakeupToUse[7], teamMakeupToUse[8], teamMakeupToUse[9], teamMakeupToUse[10], teamMakeupToUse[11], teamMakeupToUse[12], teamMakeupToUse[13], teamMakeupToUse[14]);

        List<Team> newTeamList = new ArrayList<>();
        for (int i = 0; i < divisionSize; i++) {
            String teamName = getUniqueTeamName(newTeamList);
            String cityName = getUniqueCity(newTeamList, divisionName);

            newTeamList.add(teamGenerator.generateTeam(teamName, cityName, doesDivisionUseDH, DEFAULT_BUDGET));
        }
        newDivision.setTeams(newTeamList);
        return newDivision;
    }


    private String getUniqueTeamName(List<Team> newTeamList) {
        if (newTeamList.isEmpty()) {
            return teamNameGenerator.generateTeamName();
        }
        List<String> teamNames = new ArrayList<>();
        for (Team team: newTeamList) {
            teamNames.add(team.getTeamName());
        }
        String teamName = teamNameGenerator.generateTeamName();
        while (teamNames.contains(teamName)) {
            teamName = teamNameGenerator.generateTeamName();
        }
        return teamName;
    }

    private String getUniqueCity(List<Team> newTeamList, String divisionName) {
        if (newTeamList.isEmpty()) {
            return cityGenerator.generateCity(divisionName);
        }
        List<String> teamCities = new ArrayList<>();
        for (Team team: newTeamList) {
            teamCities.add(team.getTeamCity());
        }
        String cityName = cityGenerator.generateCity(divisionName);
        while (teamCities.contains(cityName)) {
            cityName = cityGenerator.generateCity(divisionName);
        }
        return cityName;
    }


}
