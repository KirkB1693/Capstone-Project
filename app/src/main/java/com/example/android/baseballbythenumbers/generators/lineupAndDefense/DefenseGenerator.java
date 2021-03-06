package com.example.android.baseballbythenumbers.generators.lineupAndDefense;

import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.constants.Positions.DESIGNATED_HITTER;
import static com.example.android.baseballbythenumbers.constants.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.constants.Positions.getScorekeeperPositionFromPrimaryPosition;

public class DefenseGenerator {

    public static TreeMap<Integer, Player> defenseFromLineup (TreeMap<Integer, Player> lineup, Team team, boolean useDH){

        List<Player> lineupPlayers = new ArrayList<>();

        if (useDH){
            lineupPlayers.add(PitchingRotationGenerator.getBestStarterAvailable(team));
            for (TreeMap.Entry<Integer, Player> entry : lineup.entrySet() ) {
                lineupPlayers.add(entry.getValue());
            }
        } else {
            for (TreeMap.Entry<Integer, Player> entry : lineup.entrySet() ) {
                lineupPlayers.add(entry.getValue());
            }
        }

        Collections.sort(lineupPlayers, Player.ErrorPctComparator);
        TreeMap <Integer, Player> newDefense = new TreeMap<>();
        for (Player player: lineupPlayers) {
            if (player.getPrimaryPosition() != DESIGNATED_HITTER) {
                if (newDefense.isEmpty()) {
                    newDefense.put(getScorekeeperPositionFromPrimaryPosition(player.getPrimaryPosition()), player);
                } else if (!newDefense.containsKey(getScorekeeperPositionFromPrimaryPosition(player.getPrimaryPosition()))){
                    newDefense.put(getScorekeeperPositionFromPrimaryPosition(player.getPrimaryPosition()), player);
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
                        defense.put(getScorekeeperPositionFromPrimaryPosition(position), PitchingRotationGenerator.getBestStarterAvailable(team));
                    } else {
                        defense.put(getScorekeeperPositionFromPrimaryPosition(position), player);
                    }

                } else {
                    if (!defense.containsKey(getScorekeeperPositionFromPrimaryPosition(position))) {
                        if (position == STARTING_PITCHER) {
                            defense.put(getScorekeeperPositionFromPrimaryPosition(position), PitchingRotationGenerator.getBestStarterAvailable(team));
                        } else {
                            defense.put(getScorekeeperPositionFromPrimaryPosition(position), player);
                        }
                    }
                }
            }

        }
        return defense;
    }
}
