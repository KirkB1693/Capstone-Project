package com.example.android.baseballbythenumbers.generators.lineupAndDefense;

import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.constants.Positions.CATCHER;
import static com.example.android.baseballbythenumbers.constants.Positions.CENTER_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.DESIGNATED_HITTER;
import static com.example.android.baseballbythenumbers.constants.Positions.FIRST_BASE;
import static com.example.android.baseballbythenumbers.constants.Positions.LEFT_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.constants.Positions.RIGHT_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.SECOND_BASE;
import static com.example.android.baseballbythenumbers.constants.Positions.SHORTSTOP;
import static com.example.android.baseballbythenumbers.constants.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.constants.Positions.THIRD_BASE;

public class LineupGenerator {

    private static final int LAST_BATTER = 9;
    private static int[] positionsNoDH = {STARTING_PITCHER, CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE, SHORTSTOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD};
    private static int[] positionsWithDH = {CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE, SHORTSTOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD, DESIGNATED_HITTER};
    private static int[] idealOrderToFillLineup = {1, 4, 2, 5, 3, 6, 7, 8, 9};

    public static TreeMap<Integer, Player> lineupFromDefense(TreeMap<Integer, Player> defense, Team team, boolean useDH) {


        List<Player> defensePlayers = new ArrayList<>();

        if (useDH) {
            for (Player player : team.getPlayers()) {
                if (player.getPrimaryPosition() == DESIGNATED_HITTER) {
                    defensePlayers.add(player);
                }
            }
            for (TreeMap.Entry<Integer, Player> entry : defense.entrySet()) {
                defensePlayers.add(entry.getValue());
            }
        } else {
            for (TreeMap.Entry<Integer, Player> entry : defense.entrySet()) {
                defensePlayers.add(entry.getValue());
            }
        }


        Collections.sort(defensePlayers, Player.BestPowerComparator);
        TreeMap<Integer, Player> bestPower = new TreeMap<>();
        int bestOrder = 1;
        for (Player player : defensePlayers) {
            bestPower.put(bestOrder, player);
            bestOrder++;
        }

        Collections.sort(defensePlayers, Player.BestOnBaseComparator);
        TreeMap<Integer, Player> bestOBP = new TreeMap<>();
        bestOrder = 1;
        for (Player player : defensePlayers) {
            bestOBP.put(bestOrder, player);
            bestOrder++;
        }

        Collections.sort(defensePlayers, Player.BestCombinedOnBaseAndPowerComparator);
        TreeMap<Integer, Player> bestCombined = new TreeMap<>();
        bestOrder = 1;
        for (Player player : defensePlayers) {
            bestCombined.put(bestOrder, player);
            bestOrder++;
        }


        TreeMap<Integer, Player> newLineup = new TreeMap<>();

        for (int lineupPosition : idealOrderToFillLineup) {
            if (lineupPosition <= 3) {
                for (TreeMap.Entry<Integer, Player> entry : bestOBP.entrySet()) {
                    Player player = entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (newLineup.isEmpty() && position != STARTING_PITCHER && positionIsInValidPositions(position, useDH)) {
                        newLineup.put(lineupPosition, player);
                        break;
                    } else {
                        if (position != STARTING_PITCHER && playerNotAlreadyInLineup(player, newLineup) && positionIsInValidPositions(position, useDH) && positionNotAlreadyInLineup(position, newLineup)) {
                            newLineup.put(lineupPosition, player);
                            break;
                        }
                    }
                }
            } else if (lineupPosition <= 6) {
                for (TreeMap.Entry<Integer, Player> entry : bestCombined.entrySet()) {
                    Player player = entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (newLineup.isEmpty() && position != STARTING_PITCHER && positionIsInValidPositions(position, useDH)) {
                        newLineup.put(lineupPosition, player);
                        break;
                    } else {
                        if (position != STARTING_PITCHER && playerNotAlreadyInLineup(player, newLineup) && positionIsInValidPositions(position, useDH) && positionNotAlreadyInLineup(position, newLineup)) {
                            newLineup.put(lineupPosition, player);
                            break;
                        }
                    }
                }
            } else {
                for (TreeMap.Entry<Integer, Player> entry : bestPower.entrySet()) {
                    Player player = entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (playerNotAlreadyInLineup(player, newLineup) && positionIsInValidPositions(position, useDH) && positionNotAlreadyInLineup(position, newLineup) && okToPlacePitcher(position, lineupPosition)) {
                        newLineup.put(lineupPosition, player);
                        break;
                    }

                }
            }
        }

        return newLineup;
    }


    //Returns a TreeMap with one player from the team at each position in the order (the best defensive player with that Primary position will be chosen, not necessarily the best overall player)
    public static TreeMap<Integer, Player> lineupFromTeam(Team team, boolean useDh) {
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

        if (useDh && teamHasNoDesignatedHitters(allPlayers)) {
            convertBestHittingSubToDH(bestCombined);
        }

        TreeMap<Integer, Player> lineup = new TreeMap<>();

        for (int lineupPosition : idealOrderToFillLineup) {
            if (lineupPosition <= 3) {
                for (TreeMap.Entry<Integer, Player> entry : bestOBP.entrySet()) {
                    Player player = entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (lineup.isEmpty() && position != STARTING_PITCHER && positionIsInValidPositions(position, useDh)) {
                        lineup.put(lineupPosition, player);
                        break;
                    } else {
                        if (position != STARTING_PITCHER && playerNotAlreadyInLineup(player, lineup) && positionIsInValidPositions(position, useDh) && positionNotAlreadyInLineup(position, lineup)) {
                            lineup.put(lineupPosition, player);
                            break;
                        }
                    }
                }
            } else if (lineupPosition <= 6) {
                for (TreeMap.Entry<Integer, Player> entry : bestCombined.entrySet()) {
                    Player player = entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (lineup.isEmpty() && position != STARTING_PITCHER && positionIsInValidPositions(position, useDh)) {
                        lineup.put(lineupPosition, player);
                        break;
                    } else {
                        if (position != STARTING_PITCHER && playerNotAlreadyInLineup(player, lineup) && positionIsInValidPositions(position, useDh) && positionNotAlreadyInLineup(position, lineup)) {
                            lineup.put(lineupPosition, player);
                            break;
                        }
                    }
                }
            } else {
                for (TreeMap.Entry<Integer, Player> entry : bestPower.entrySet()) {
                    Player player = entry.getValue();
                    final int position = player.getPrimaryPosition();
                    if (playerNotAlreadyInLineup(player, lineup) && positionIsInValidPositions(position, useDh) && positionNotAlreadyInLineup(position, lineup) && okToPlacePitcher(position, lineupPosition)) {
                        if (player.getPrimaryPosition() == STARTING_PITCHER) {
                            lineup.put(lineupPosition, PitchingRotationGenerator.getBestStarterAvailable(team));
                        } else {
                            lineup.put(lineupPosition, player);
                        }
                        break;
                    }

                }
            }
        }

        return lineup;
    }

    private static void convertBestHittingSubToDH(TreeMap<Integer, Player> bestCombined) {
        List<Integer> positions = new ArrayList<>();
        int currentPlayerPosition;
        for (TreeMap.Entry<Integer, Player> entry : bestCombined.entrySet()) {
            Player currentPlayer = entry.getValue();
            currentPlayerPosition = currentPlayer.getPrimaryPosition();
            if (positions.isEmpty()) {
                positions.add(currentPlayerPosition);
            } else {
                if (positions.contains(currentPlayerPosition)) {
                    changePositionToDH(currentPlayer);
                    return;
                } else {
                    positions.add(currentPlayerPosition);
                }

            }
        }
    }



    private static void changePositionToDH(Player currentPlayer) {
        int currentPosition = currentPlayer.getPrimaryPosition();
        currentPlayer.setPrimaryPosition(DESIGNATED_HITTER);
        if (currentPlayer.getAlternatePositions() >= DESIGNATED_HITTER && currentPlayer.getAlternatePositions() < LONG_RELIEVER) {
            currentPlayer.setAlternatePositions(currentPlayer.getAlternatePositions() - DESIGNATED_HITTER + currentPosition);
        } else {
            currentPlayer.setAlternatePositions(currentPlayer.getAlternatePositions() + currentPosition);
        }
    }

    private static boolean teamHasNoDesignatedHitters(List<Player> allPlayers) {
        for (Player player : allPlayers) {
            if (player.getPrimaryPosition() == DESIGNATED_HITTER) {
                return false;
            }
        }
        return true;
    }

    private static boolean okToPlacePitcher(int position, int lineupPosition) {
        if (position != STARTING_PITCHER) {
            return true;
        }
        return lineupPosition == LAST_BATTER;
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
            for (int positionFromListOfValidPositions : positionsWithDH) {
                if (position == positionFromListOfValidPositions) {
                    return true;
                }
            }
        } else {
            for (int positionFromListOfValidPositions : positionsNoDH) {
                if (position == positionFromListOfValidPositions) {
                    return true;
                }
            }
        }
        return false;
    }
}
