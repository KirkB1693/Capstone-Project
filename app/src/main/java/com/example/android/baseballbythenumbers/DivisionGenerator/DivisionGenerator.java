package com.example.android.baseballbythenumbers.DivisionGenerator;

import android.content.Context;

import com.example.android.baseballbythenumbers.CityGenerator.CityGenerator;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.TeamGenerator.TeamGenerator;
import com.example.android.baseballbythenumbers.TeamNameGenerator.TeamNameGenerator;

import java.util.ArrayList;
import java.util.List;

public class DivisionGenerator {

    Context context;
    private static final int[] DEFAULT_TEAM_MAKEUP = new int[] {5, 3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1};
    private static final int DEFAULT_BUDGET = 1000000;

    public DivisionGenerator (Context context){
        this.context = context;
    }

    public Division generateDivision (String divisionName, boolean doesDivisionUseDH, int divisionSize, int[] teamMakeup) {
        Division newDivision = new Division();
        newDivision.setDivisionName(divisionName);
        int[] teamMakeupToUse = DEFAULT_TEAM_MAKEUP;
        TeamNameGenerator teamNameGenerator = new TeamNameGenerator(context);
        CityGenerator cityGenerator = new CityGenerator(context);

        TeamGenerator teamGenerator;

        if (teamMakeup != null) {
            if (teamMakeup.length == 15) {
                System.arraycopy(teamMakeup, 0, teamMakeupToUse, 0, teamMakeup.length);
            }
        }

        teamGenerator = new TeamGenerator(context, teamMakeupToUse[0],teamMakeupToUse[1],teamMakeupToUse[2],teamMakeupToUse[3],teamMakeupToUse[4],teamMakeupToUse[5],
                teamMakeupToUse[6],teamMakeupToUse[7],teamMakeupToUse[8],teamMakeupToUse[9], teamMakeupToUse[10],teamMakeupToUse[11],teamMakeupToUse[12],teamMakeupToUse[13],teamMakeupToUse[14]);

        List<Team> newTeamList = new ArrayList<>();
        for (int i = 0; i < divisionSize; i++) {
            String teamName = teamNameGenerator.generateTeamName();
            String cityName = cityGenerator.generateCity();
            if (!newTeamList.isEmpty()) {
                for (Team team : newTeamList) {
                    if (cityName.equals(team.getTeamCity())) {
                        cityName = cityGenerator.generateCity();
                    }
                    if (teamName.equals(team.getTeamName())) {
                        teamName = teamNameGenerator.generateTeamName();
                    }
                }
            }
            newTeamList.add(teamGenerator.generateTeam(teamName,cityName, doesDivisionUseDH, DEFAULT_BUDGET));
        }
        newDivision.setTeams(newTeamList);
        return newDivision;
    }


}
