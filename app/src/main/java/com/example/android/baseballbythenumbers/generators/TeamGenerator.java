package com.example.android.baseballbythenumbers.generators;

import android.content.Context;
import android.widget.ProgressBar;

import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamGenerator {

    private PitcherGenerator pitcherGenerator;

    private HitterGenerator hitterGenerator;

    public TeamGenerator(Context context, int startingPitchers, int longReliefPitchers, int shortReliefPitchers,
                         int catchers, int firstBasemen, int secondBasemen, int thirdBasemen, int shortstops, int leftField,
                         int centerField, int rightField, int utilityInfield, int utilityOutfield, int utility, int designatedHitter){

        NameGenerator nameGenerator = new NameGenerator(context);
        pitcherGenerator = new PitcherGenerator(context, nameGenerator,startingPitchers, longReliefPitchers, shortReliefPitchers);
        hitterGenerator = new HitterGenerator(context, nameGenerator, catchers, firstBasemen, secondBasemen, thirdBasemen, shortstops, leftField, centerField, rightField, utilityInfield, utilityOutfield, utility, designatedHitter);
    }


    public Team generateTeam(String teamName, String teamCity, boolean useDH, int teamBudget, String divisionId, ProgressBar progressBar) {
        Team newTeam = new Team(teamName,teamCity,useDH,teamBudget,null,divisionId,0,0);

        List<Player> pitchers = generatePitchers(newTeam.getTeamId(), progressBar);

        List<Player> hitters = generateHitters(newTeam.getTeamId(), progressBar);

        List<Player> teamRoster = new ArrayList<>();
        teamRoster.addAll(pitchers);
        teamRoster.addAll(hitters);

        newTeam.setPlayers(teamRoster);

        return newTeam;
    }

    private List<Player> generateHitters(String teamId, ProgressBar progressBar) {
        return hitterGenerator.generateHitters(teamId, progressBar);
    }

    private List<Player> generatePitchers(String teamId, ProgressBar progressBar) {
        return pitcherGenerator.generatePitchers(teamId, progressBar);
    }
}
