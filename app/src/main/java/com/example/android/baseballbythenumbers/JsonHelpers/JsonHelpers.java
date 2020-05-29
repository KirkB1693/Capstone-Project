package com.example.android.baseballbythenumbers.JsonHelpers;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.TreeMap;

public class JsonHelpers {


    public TreeMap<Integer, Player> getTreeMapDataFromJson(JSONObject homeDefenseJson, Team homeTeam, Team visitingTeam) {
        TreeMap<Integer, Player> playerTreeMap = new TreeMap<>();
        for (Iterator<String> iterator = homeDefenseJson.keys(); iterator.hasNext();) {    // iterate over stored keys
            try {
                String key = iterator.next();
                Integer integerKey = Integer.valueOf(key);
                JSONObject playerFromJsonObject = homeDefenseJson.getJSONObject(key);
                String playerTeamId = String.valueOf(playerFromJsonObject.get("teamId"));
                if (playerTeamId.equals(homeTeam.getTeamId())) {
                    for (Player player : homeTeam.getPlayers()) {
                        if (player.getPlayerId().equals(playerFromJsonObject.get("playerId"))) {
                            playerTreeMap.put(integerKey,player);
                            break;
                        }
                    }
                } else {
                    for (Player player : visitingTeam.getPlayers()) {
                        if (player.getPlayerId().equals(playerFromJsonObject.get("playerId"))) {
                            playerTreeMap.put(integerKey,player);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return playerTreeMap;
    }

    public Player getPlayerFromJson(JSONObject playerJsonObject) {
        Gson gson = new Gson();
        Player player = null;
        if (playerJsonObject != null) {
            player = gson.fromJson(String.valueOf(playerJsonObject), Player.class);
        }
        return player;
    }


}
