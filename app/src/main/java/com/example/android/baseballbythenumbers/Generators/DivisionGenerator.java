package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;
import android.widget.ProgressBar;

import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.List;

public class DivisionGenerator {

    private Context context;
    private static final int[] DEFAULT_TEAM_MAKEUP = new int[]{5, 3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1};
    private static final int DEFAULT_BUDGET = 1000000;
    private CityGenerator cityGenerator;
    private boolean userTeamPlaced;

    public DivisionGenerator(Context context) {
        this.context = context;
    }

    public Division generateDivision(String divisionName, boolean doesDivisionUseDH, int divisionSize, int[] teamMakeup,
                                     CityGenerator cityGenerator, TeamNameGenerator teamNameGenerator, String leagueId, String userTeamName, String userTeamCity, ProgressBar progressBar) {
        Division newDivision = new Division();
        newDivision.setDivisionName(divisionName);
        int[] teamMakeupToUse = DEFAULT_TEAM_MAKEUP;

        this.cityGenerator = cityGenerator;

        TeamGenerator teamGenerator;

        if (teamMakeup != null) {
            if (teamMakeup.length == 15) {
                for (int i = 0; i < teamMakeup.length; i++) {
                    teamMakeupToUse[i] = teamMakeup [i];
                }
            }
        }

        teamGenerator = new TeamGenerator(context, teamMakeupToUse[0], teamMakeupToUse[1], teamMakeupToUse[2], teamMakeupToUse[3], teamMakeupToUse[4], teamMakeupToUse[5],
                teamMakeupToUse[6], teamMakeupToUse[7], teamMakeupToUse[8], teamMakeupToUse[9], teamMakeupToUse[10], teamMakeupToUse[11], teamMakeupToUse[12],
                teamMakeupToUse[13], teamMakeupToUse[14]);

        List<Team> newTeamList = new ArrayList<>();
        for (int i = 0; i < divisionSize; i++) {
            userTeamPlaced = !teamNameGenerator.checkIfUserTeamNameInList(userTeamName);
            String cityName = getUniqueCity(newTeamList, divisionName, userTeamCity);
            String teamName = "";
            if (cityName.equals(userTeamCity) && !userTeamPlaced) {
                teamName = userTeamName;
                teamNameGenerator.removeUserTeamNameFromList(userTeamName);  // Makes sure that another team doesn't have the same name as the user picked
                userTeamPlaced = true;
            } else {
                teamName = teamNameGenerator.generateTeamName();
            }
            newTeamList.add(teamGenerator.generateTeam(teamName, cityName, doesDivisionUseDH, DEFAULT_BUDGET, newDivision.getDivisionId(), progressBar));
        }
        newDivision.setTeams(newTeamList);
        newDivision.setLeagueId(leagueId);
        return newDivision;
    }


    private String getUniqueCity(List<Team> newTeamList, String divisionName, String userCity) {
        if (!userTeamPlaced) {
            if (cityGenerator.cityPossibleInDivision(divisionName, userCity)){
                return userCity;
            }
        }
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
