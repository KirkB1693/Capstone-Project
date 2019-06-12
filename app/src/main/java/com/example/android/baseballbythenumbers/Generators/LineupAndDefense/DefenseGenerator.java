package com.example.android.baseballbythenumbers.Generators.LineupAndDefense;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.Data.Positions.DESIGNATED_HITTER;
import static com.example.android.baseballbythenumbers.Data.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.getScorkeeperPositionFromPrimaryPosition;

public class DefenseGenerator {

    public static TreeMap<Integer, Player> defenseFromLineup (TreeMap<Integer, Player> lineup, Team team){

        List<Player> lineupPlayers = new ArrayList<>();

        if (team.isUseDh()){
            lineupPlayers.add(PitchingRotationGenerator.getBestStarterAvailable(team));
            for (TreeMap.Entry entry : lineup.entrySet() ) {
                lineupPlayers.add((Player) entry.getValue());
            }
        } else {
            for (TreeMap.Entry entry : lineup.entrySet() ) {
                lineupPlayers.add((Player) entry.getValue());
            }
        }

        Collections.sort(lineupPlayers, Player.ErrorPctComparator);
        TreeMap <Integer, Player> newDefense = new TreeMap<>();
        for (Player player: lineupPlayers) {
            if (player.getPrimaryPosition() != DESIGNATED_HITTER) {
                if (newDefense.isEmpty()) {
                    newDefense.put(getScorkeeperPositionFromPrimaryPosition(player.getPrimaryPosition()), player);
                } else if (!newDefense.containsKey(getScorkeeperPositionFromPrimaryPosition(player.getPrimaryPosition()))){
                    newDefense.put(getScorkeeperPositionFromPrimaryPosition(player.getPrimaryPosition()), player);
                }
            }
        }
        return newDefense;
    }


    //Returns a TreeMap with one player from the team at each position (the best defensive player with that Primary position will be chosen, not necessarily the best overall player)
    public static TreeMap<Integer, Player> defenseFromTeam (Team team) {
        List<Player> allPlayers = team.getPlayers();
        Collections.sort(allPlayers,Player.ErrorPctComparator);
        TreeMap<Integer, Player> defense = new TreeMap<>();
        for (Player player: allPlayers) {
            int position = player.getPrimaryPosition();
            if (position < DESIGNATED_HITTER) {
                if (defense.isEmpty()) {
                    if (position == STARTING_PITCHER) {
                        defense.put(getScorkeeperPositionFromPrimaryPosition(position), PitchingRotationGenerator.getBestStarterAvailable(team));
                    } else {
                        defense.put(getScorkeeperPositionFromPrimaryPosition(position), player);
                    }

                } else {
                    if (!defense.containsKey(getScorkeeperPositionFromPrimaryPosition(position))) {
                        if (position == STARTING_PITCHER) {
                            defense.put(getScorkeeperPositionFromPrimaryPosition(position), PitchingRotationGenerator.getBestStarterAvailable(team));
                        } else {
                            defense.put(getScorkeeperPositionFromPrimaryPosition(position), player);
                        }
                    }
                }
            }

        }
        return defense;
    }
}
