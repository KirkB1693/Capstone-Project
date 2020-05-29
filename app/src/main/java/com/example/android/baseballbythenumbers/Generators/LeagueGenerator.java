package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;
import android.widget.ProgressBar;

import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.League;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.baseballbythenumbers.Constants.Constants.DivisionNames.CENTRAL;
import static com.example.android.baseballbythenumbers.Constants.Constants.DivisionNames.EAST;
import static com.example.android.baseballbythenumbers.Constants.Constants.DivisionNames.NORTH;
import static com.example.android.baseballbythenumbers.Constants.Constants.DivisionNames.NO_DIVISIONS;
import static com.example.android.baseballbythenumbers.Constants.Constants.DivisionNames.SOUTH;
import static com.example.android.baseballbythenumbers.Constants.Constants.DivisionNames.WEST;

public class LeagueGenerator {

    Context context;

    private static final int DEFAULT_DIVISIONS = 2;

    public LeagueGenerator(Context context) {
        this.context = context;
    }

    public League generateLeague(String leagueName, boolean useDH, int divisionSize, int numberOfDivisions,
                                 int[] teamMakeup, CityGenerator cityGenerator, TeamNameGenerator teamNameGenerator, String orgId, String userTeamName, String userTeamCity, ProgressBar progressBar) {
        List<Division> divisions = new ArrayList<>();
        DivisionGenerator divisionGenerator = new DivisionGenerator(context);

        if (numberOfDivisions <0 || numberOfDivisions>5) {
            numberOfDivisions = DEFAULT_DIVISIONS;
        }

        League newLeague = new League(leagueName, useDH, null, orgId);

        switch (numberOfDivisions) {
            case 0:
                divisions.add(divisionGenerator.generateDivision(NO_DIVISIONS, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                break;
            case 1:
                divisions.add(divisionGenerator.generateDivision(NO_DIVISIONS, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                break;
            case 2:
                divisions.add(divisionGenerator.generateDivision(WEST, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                divisions.add(divisionGenerator.generateDivision(EAST, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                break;
            case 3:
                divisions.add(divisionGenerator.generateDivision(WEST, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                divisions.add(divisionGenerator.generateDivision(CENTRAL, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                divisions.add(divisionGenerator.generateDivision(EAST, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                break;
            case 4:
                divisions.add(divisionGenerator.generateDivision(WEST, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                divisions.add(divisionGenerator.generateDivision(NORTH, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                divisions.add(divisionGenerator.generateDivision(SOUTH, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                divisions.add(divisionGenerator.generateDivision(EAST, useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                break;
            default:
                for (int i = 0; i < divisionSize; i++) {
                    divisions.add(divisionGenerator.generateDivision(String.valueOf(i), useDH, divisionSize, teamMakeup, cityGenerator, teamNameGenerator, newLeague.getLeagueId(), userTeamName, userTeamCity, progressBar));
                }
                break;
        }
        newLeague.setDivisions(divisions);
        return newLeague;
    }

}
