package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.League;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.CENTRAL;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.EAST;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.NORTH;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.NO_DIVISONS;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.SOUTH;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.WEST;

public class LeagueGenerator {

    Context context;

    private static final int DEFAULT_DIVISIONS = 2;

    public LeagueGenerator(Context context) {
        this.context = context;
    }

    public League generateLeague(String leagueName, boolean useDH, int divisionSize, int numberOfDivisions, int countriesToInclude, int[] teamMakeup, CityGenerator cityGenerator, TeamNameGenerator teamNameGenerator) {
        List<Division> divisions = new ArrayList<>();
        DivisionGenerator divisionGenerator = new DivisionGenerator(context);

        if (numberOfDivisions <0 || numberOfDivisions>5) {
            numberOfDivisions = DEFAULT_DIVISIONS;
        }

        switch (numberOfDivisions) {
            case 0:
                divisions.add(divisionGenerator.generateDivision(NO_DIVISONS, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                break;
            case 1:
                divisions.add(divisionGenerator.generateDivision(NO_DIVISONS, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                break;
            case 2:
                divisions.add(divisionGenerator.generateDivision(WEST, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                divisions.add(divisionGenerator.generateDivision(EAST, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                break;
            case 3:
                divisions.add(divisionGenerator.generateDivision(WEST, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                divisions.add(divisionGenerator.generateDivision(CENTRAL, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                divisions.add(divisionGenerator.generateDivision(EAST, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                break;
            case 4:
                divisions.add(divisionGenerator.generateDivision(WEST, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                divisions.add(divisionGenerator.generateDivision(NORTH, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                divisions.add(divisionGenerator.generateDivision(SOUTH, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                divisions.add(divisionGenerator.generateDivision(EAST, useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                break;
            default:
                for (int i = 0; i < divisionSize; i++) {
                    divisions.add(divisionGenerator.generateDivision(String.valueOf(i), useDH, divisionSize, teamMakeup, numberOfDivisions, countriesToInclude, cityGenerator, teamNameGenerator));
                }
                break;
        }
        return new League(leagueName, useDH, divisions);
    }

}
