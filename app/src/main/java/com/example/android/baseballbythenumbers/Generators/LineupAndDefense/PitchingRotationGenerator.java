package com.example.android.baseballbythenumbers.Generators.LineupAndDefense;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.SHORT_RELIEVER_STAMINA_MIN;
import static com.example.android.baseballbythenumbers.Constants.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Player.BestPitcherComparator;

public class PitchingRotationGenerator {

    private static int STARTER_DAYS_TO_RECOVER_FULLY = 5;

    public static Player getBestStarterAvailable(Team team) {
        List<Player> possibleStarters = new ArrayList<>();
        for (Player player : team.getPlayers()) {
            if (player.getPrimaryPosition() == STARTING_PITCHER && pitcherNotTired(player, STARTING_PITCHER)) {
                possibleStarters.add(player);
            }
        }

        if (possibleStarters.isEmpty()) {
            possibleStarters.add(getStarterWithMostStaminaLeft(team));
        }
        Collections.sort(possibleStarters, BestPitcherComparator);
        return possibleStarters.get(0);
    }

    private static Player getStarterWithMostStaminaLeft(Team team) {
        Player mostRestedStarter = null;
        int mostStaminaLeft = 0;
        for (Player player : team.getPlayers()) {
            if (player.getPrimaryPosition() == STARTING_PITCHER || player.getPrimaryPosition() == LONG_RELIEVER) {
                int currentPitcherStaminaLeft = player.getPitchingPercentages().getPitchingStamina() - player.getPitchingPercentages().getPitchingStaminaUsed();
                if ( currentPitcherStaminaLeft > mostStaminaLeft) {
                    mostRestedStarter = player;
                    mostStaminaLeft = currentPitcherStaminaLeft;
                }
            }
        }
        return mostRestedStarter;
    }

    public static Player getBestStarterAvailableForNextGame(Team team) {
        List<Player> possibleStarters = new ArrayList<>();
        for (Player player : team.getPlayers()) {
            if (player.getPrimaryPosition() == STARTING_PITCHER && pitcherNotTiredForNextGame(player, STARTING_PITCHER)) {
                possibleStarters.add(player);
            }
        }
        if (possibleStarters.isEmpty()) {
            possibleStarters.add(getStarterWithMostStaminaLeft(team));
        }
        Collections.sort(possibleStarters, BestPitcherComparator);
        return possibleStarters.get(0);
    }


    public static Player getWorstStarterAvailable(Team team) {
        List<Player> possibleStarters = new ArrayList<>();
        for (Player player : team.getPlayers()) {
            if (player.getPrimaryPosition() == STARTING_PITCHER && pitcherNotTired(player, STARTING_PITCHER)) {
                possibleStarters.add(player);
            }
        }
        if (possibleStarters.isEmpty()) {
            possibleStarters.add(getStarterWithMostStaminaLeft(team));
        }
        Collections.sort(possibleStarters, BestPitcherComparator);
        return possibleStarters.get(possibleStarters.size()-1);
    }

    public static Player getBestCloserAvailable(Team team) {
        List<Player> possibleClosers = new ArrayList<>();
        for (Player player : team.getPlayers()) {
            if (player.getPrimaryPosition() == LONG_RELIEVER || player.getPrimaryPosition() == SHORT_RELEIVER) {
                if (pitcherNotTired(player, SHORT_RELEIVER))
                    possibleClosers.add(player);
            }
        }

        Collections.sort(possibleClosers, BestPitcherComparator);
        return possibleClosers.get(0);
    }

    public static TreeMap<Integer, Player> getAvailableRelievers(Team team) {
        Player closer = getBestCloserAvailable(team);
        List<Player> possibleRelievers = new ArrayList<>();
        for (Player player : team.getPlayers()) {
            if (player.getPrimaryPosition() == LONG_RELIEVER || player.getPrimaryPosition() == SHORT_RELEIVER) {
                if (pitcherNotTired(player, SHORT_RELEIVER) && player != closer)
                    possibleRelievers.add(player);
            }
        }

        Collections.sort(possibleRelievers, BestPitcherComparator);
        TreeMap<Integer, Player> relieversAvailable = new TreeMap<>();
        for (int i = 0; i < possibleRelievers.size(); i++) {
            relieversAvailable.put(i + 1, possibleRelievers.get(i));
        }
        return relieversAvailable;
    }

    private static boolean pitcherNotTired(Player pitcher, int primaryPosition) {
        int pitcherStaminaLeft = pitcher.getPitchingPercentages().getPitchingStamina() - pitcher.getPitchingPercentages().getPitchingStaminaUsed();
        switch (primaryPosition) {
            case STARTING_PITCHER:
                return (pitcher.getPitchingPercentages().getPitchingStaminaUsed() < ((pitcher.getPitchingPercentages().getPitchingStamina() / STARTER_DAYS_TO_RECOVER_FULLY)));
            case LONG_RELIEVER:
            case SHORT_RELEIVER:
                return (pitcherStaminaLeft >= SHORT_RELIEVER_STAMINA_MIN);
        }
        return false;
    }

    private static boolean pitcherNotTiredForNextGame(Player pitcher, int primaryPosition) {
        int pitcherStaminaLeft = pitcher.getPitchingPercentages().getPitchingStamina() - pitcher.getPitchingPercentages().getPitchingStaminaUsed();
        switch (primaryPosition) {
            case STARTING_PITCHER:
                return (pitcher.getPitchingPercentages().getPitchingStaminaUsed() < 2*((pitcher.getPitchingPercentages().getPitchingStamina() / STARTER_DAYS_TO_RECOVER_FULLY)));
            case LONG_RELIEVER:
            case SHORT_RELEIVER:
                return (pitcherStaminaLeft >= SHORT_RELIEVER_STAMINA_MIN);
        }
        return false;
    }
}
