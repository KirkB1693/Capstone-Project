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
import static com.example.android.baseballbythenumbers.Data.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Data.Positions.RIGHT_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.SECOND_BASE;
import static com.example.android.baseballbythenumbers.Data.Positions.SHORTSTOP;
import static com.example.android.baseballbythenumbers.Data.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Data.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.THIRD_BASE;

public class DefenseGenerator {

    public ArrayList<Player> defenseFromLineup (ArrayList<Player> lineup){
        return lineup;
    }


    //Returns a TreeMap with one player from the team at each position (the best defensive player with that Primary position will be chosen, not necessarily the best overall player)
    public static TreeMap<Integer, Player> defenseFromTeam (Team team) {
        List<Player> allPlayers = team.getPlayers();
        Collections.sort(allPlayers,Player.ErrorPctComparator);
        TreeMap<Integer, Player> defense = new TreeMap<>();
        for (Player player: allPlayers) {
            int position = player.getPrimaryPosition();
            int scorebookPosition = 0;
            switch (position) {
                case STARTING_PITCHER:
                    scorebookPosition = 1;
                    break;
                case LONG_RELIEVER:
                    scorebookPosition = 1;
                    break;
                case SHORT_RELEIVER:
                    scorebookPosition = 1;
                    break;
                case CATCHER:
                    scorebookPosition = 2;
                    break;
                case FIRST_BASE:
                    scorebookPosition = 3;
                    break;
                case SECOND_BASE:
                    scorebookPosition = 4;
                    break;
                case THIRD_BASE:
                    scorebookPosition = 5;
                    break;
                case SHORTSTOP:
                    scorebookPosition = 6;
                    break;
                case LEFT_FIELD:
                    scorebookPosition = 7;
                    break;
                case CENTER_FIELD:
                    scorebookPosition = 8;
                    break;
                case RIGHT_FIELD:
                    scorebookPosition = 9;
                    break;
                case DESIGNATED_HITTER:
                    scorebookPosition = 0;
                    break;
                default:
                    scorebookPosition = 0;
            }
            if (defense.isEmpty() && (position < DESIGNATED_HITTER)) {
                defense.put(scorebookPosition, player);
            } else {
                if (!defense.containsKey(scorebookPosition) && (position < DESIGNATED_HITTER)) {
                    defense.put(scorebookPosition, player);
                }
            }
        }
        return defense;
    }
}
