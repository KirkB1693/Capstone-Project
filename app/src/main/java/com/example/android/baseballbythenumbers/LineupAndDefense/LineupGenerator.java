package com.example.android.baseballbythenumbers.LineupAndDefense;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    static int[] idealOrderToFillLineup = {1, 4, 2, 5, 3, 6, 7, 8, 9};

    public ArrayList<Player> lineupFromDefense(ArrayList<Player> lineup) {
        return lineup;
    }


    //Returns a TreeMap with one player from the team at each position in the order (the best defensive player with that Primary position will be chosen, not necessarily the best overall player)
    public static TreeMap<Integer, Player> lineupFromTeam(Team team) {
        List<Player> allPlayers = team.getPlayers();

        Collections.sort(allPlayers, Player.BestPowerComparator);
        TreeMap<Integer, Player> bestPower = new TreeMap<>();
        int bestOrder = 1;
        for (Player player : allPlayers) {
            bestPower.put(bestOrder, player);
            bestOrder++;
        }

        Collections.sort(allPlayers, Player.BestOnBaseComparator);
        TreeMap<Integer, Player> bestOBP = new TreeMap<>();
        bestOrder = 1;
        for (Player player : allPlayers) {
            bestOBP.put(bestOrder, player);
            bestOrder++;
        }

        Collections.sort(allPlayers, Player.BestCombinedOnBaseAndPowerComparator);
        TreeMap<Integer, Player> bestCombined = new TreeMap<>();
        bestOrder = 1;
        for (Player player : allPlayers) {
            bestCombined.put(bestOrder, player);
            bestOrder++;
        }


        TreeMap<Integer, Player> lineup = new TreeMap<>();

        for (int lineupPosition : idealOrderToFillLineup) {
            if (lineupPosition <= 3) {
                for (TreeMap.Entry entry : bestOBP.entrySet()) {
                    Player player = (Player) entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (lineup.isEmpty() && position != STARTING_PITCHER && positionIsInValidPositions(position, team.isUseDh())) {
                        lineup.put(lineupPosition, player);
                        break;
                    } else {
                        if (position != STARTING_PITCHER && playerNotAlreadyInLineup(player, lineup) && positionIsInValidPositions(position, team.isUseDh()) && positionNotAlreadyInLineup(position, lineup)) {
                            lineup.put(lineupPosition, player);
                            break;
                        }
                    }
                }
            } else if (lineupPosition <= 6) {
                for (TreeMap.Entry entry : bestCombined.entrySet()) {
                    Player player = (Player) entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (lineup.isEmpty() && position != STARTING_PITCHER && positionIsInValidPositions(position, team.isUseDh())) {
                        lineup.put(lineupPosition, player);
                        break;
                    } else {
                        if (position != STARTING_PITCHER && playerNotAlreadyInLineup(player, lineup) && positionIsInValidPositions(position, team.isUseDh()) && positionNotAlreadyInLineup(position, lineup)) {
                            lineup.put(lineupPosition, player);
                            break;
                        }
                    }
                }
            } else {
                for (TreeMap.Entry entry : bestPower.entrySet()) {
                    Player player = (Player) entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (playerNotAlreadyInLineup(player, lineup) && positionIsInValidPositions(position, team.isUseDh()) && positionNotAlreadyInLineup(position, lineup) && okToPlacePitcher(position, lineupPosition)) {
                            lineup.put(lineupPosition, player);
                            break;
                        }

                }
            }
        }

        return lineup;
    }

    private static boolean okToPlacePitcher(int position, int lineupPosition) {
        if (position != STARTING_PITCHER) {
            return true;
        }
        if (lineupPosition == 9) {
            return true;
        }
        return false;
    }

    private static boolean playerNotAlreadyInLineup(Player playerToCheck, TreeMap<Integer, Player> lineup) {
        for (TreeMap.Entry<Integer, Player> entry : lineup.entrySet()) {
            Player lineupPlayer = entry.getValue();
            if (playerToCheck == lineupPlayer) {
                return false;
            }
        }
        return true;
    }

    private static boolean positionNotAlreadyInLineup(int position, TreeMap<Integer, Player> lineup) {
        for (TreeMap.Entry<Integer, Player> entry : lineup.entrySet()) {
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
