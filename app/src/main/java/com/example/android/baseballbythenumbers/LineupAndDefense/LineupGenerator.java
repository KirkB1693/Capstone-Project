package com.example.android.baseballbythenumbers.LineupAndDefense;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.Data.Positions.CATCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.CENTER_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.DESIGNATED_HITTER;
import static com.example.android.baseballbythenumbers.Data.Positions.FIRST_BASE;
import static com.example.android.baseballbythenumbers.Data.Positions.LEFT_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.RIGHT_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.SECOND_BASE;
import static com.example.android.baseballbythenumbers.Data.Positions.SHORTSTOP;
import static com.example.android.baseballbythenumbers.Data.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.THIRD_BASE;

public class LineupGenerator {

    static int[] positionsNoDH = {STARTING_PITCHER, CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE, SHORTSTOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD};
    static int[] positionsWithDH = {CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE, SHORTSTOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD, DESIGNATED_HITTER};

    public ArrayList<Player> lineupFromDefense(ArrayList<Player> lineup) {
        return lineup;
    }


    //Returns a TreeMap with one player from the team at each position in the order (the best defensive player with that Primary position will be chosen, not necessarily the best overall player)
    public static TreeMap<Integer, Player> lineupFromTeam(Team team) {
        List<Player> allPlayers = team.getPlayers();
        int positionInOrder = 1;
        Collections.sort(allPlayers, Player.BestPowerComparator);
        TreeMap<Integer, Player> lineup = new TreeMap<>();

        for (Player player : allPlayers) {
            final int position = player.getPrimaryPosition();
            if (lineup.isEmpty()) {
                lineup.put(positionInOrder, player);
                positionInOrder++;
            } else {
                if (!lineup.containsKey(positionInOrder) && positionIsInValidPositions(position, team.isUseDh()) && positionNotAlreadyInTreeMap(position, lineup)) {
                    lineup.put(positionInOrder, player);
                    positionInOrder++;
                }
            }
        }
        return lineup;
    }

    private static boolean positionNotAlreadyInTreeMap(int position, TreeMap<Integer, Player> lineup) {
        for (Map.Entry<Integer, Player> entry : lineup.entrySet()) {
            Player player = entry.getValue();
            if (player.getPrimaryPosition() == position) {
                return false;
            }
        }
        return true;
    }

    private static boolean positionIsInValidPositions(int position, boolean useDH) {
        if (useDH) {
            for (int i = 0; i < positionsWithDH.length; i++) {
                if (position == positionsWithDH[i]) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < positionsNoDH.length; i++) {
                if (position == positionsNoDH[i]) {
                    return true;
                }
            }
        }
        return false;
    }
}
