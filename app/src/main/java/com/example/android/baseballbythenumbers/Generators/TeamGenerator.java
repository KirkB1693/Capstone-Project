package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;
import android.widget.ProgressBar;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamGenerator {

    private int startingPitchers;

    private int longReliefPitchers;

    private int shortReliefPitchers;

    private int catchers;

    private int firstBasemen;

    private int secondBasemen;

    private int thirdBasemen;

    private int shortstops;

    private int leftField;

    private int centerField;

    private int rightField;

    private int utilityInfield;

    private int utilityOutfield;

    private int utility;

    private int designatedHitter;

    private PitcherGenerator pitcherGenerator;

    private HitterGenerator hitterGenerator;

    private NameGenerator nameGenerator;

    public TeamGenerator(Context context, int startingPitchers, int longReliefPitchers, int shortReliefPitchers,
                         int catchers, int firstBasemen, int secondBasemen, int thirdBasemen, int shortstops, int leftField,
                         int centerField, int rightField, int utilityInfield, int utilityOutfield, int utility, int designatedHitter){
        this.startingPitchers = startingPitchers;
        this.longReliefPitchers = longReliefPitchers;
        this.shortReliefPitchers = shortReliefPitchers;
        this.catchers = catchers;
        this.firstBasemen = firstBasemen;
        this.secondBasemen = secondBasemen;
        this.thirdBasemen = thirdBasemen;
        this.shortstops = shortstops;
        this.leftField = leftField;
        this.centerField = centerField;
        this.rightField = rightField;
        this.utilityInfield = utilityInfield;
        this.utilityOutfield = utilityOutfield;
        this.utility = utility;
        this.designatedHitter = designatedHitter;

        nameGenerator = new NameGenerator(context);
        pitcherGenerator = new PitcherGenerator(context, nameGenerator ,startingPitchers, longReliefPitchers, shortReliefPitchers);
        hitterGenerator = new HitterGenerator(context, nameGenerator, catchers, firstBasemen, secondBasemen, thirdBasemen, shortstops, leftField, centerField, rightField, utilityInfield, utilityOutfield, utility, designatedHitter);
    }


    public Team generateTeam(String teamName, String teamCity, boolean useDH, int teamBudget, String divisionId, ProgressBar progressBar) {
        Team newTeam = new Team();

        newTeam.setTeamName(teamName);
        newTeam.setTeamCity(teamCity);
        newTeam.setUseDh(useDH);
        newTeam.setTeamBudget(teamBudget);
        newTeam.setDivisionId(divisionId);

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
