package com.example.android.baseballbythenumbers.LineupAndDefense;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.Data.Positions.DESIGNATED_HITTER;

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
            if (defense.isEmpty()) {
                defense.put(position, player);
            } else {
                if (!defense.containsKey(position) && position < DESIGNATED_HITTER) {
                    defense.put(position, player);
                }
            }
        }
        return defense;
    }
}
