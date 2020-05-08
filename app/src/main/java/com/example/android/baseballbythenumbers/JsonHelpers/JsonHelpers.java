package com.example.android.baseballbythenumbers.JsonHelpers;

import android.support.v4.util.Pair;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Runner;
import com.example.android.baseballbythenumbers.Data.Team;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.TreeMap;

public class JsonHelpers {

    public TreeMap<Integer, Pair<Integer, Boolean>> getAnimationDataFromJson(JSONObject animationDataJson) {
        TreeMap<Integer, Pair<Integer, Boolean>> animationData = new TreeMap<>();
        try {
            JSONObject batterJson = animationDataJson.getJSONObject("0");

            if (batterJson != null) {
                Pair<Integer, Boolean> tempPair = new Pair<>(batterJson.getInt("first"),batterJson.getBoolean("second"));
                animationData.put(0, tempPair);
            }
            JSONObject firstJson = animationDataJson.getJSONObject("1");
            if (firstJson != null) {
                Pair<Integer, Boolean> tempPair = new Pair<>(batterJson.getInt("first"),batterJson.getBoolean("second"));
                animationData.put(1, tempPair);
            }
            JSONObject secondJson = animationDataJson.getJSONObject("2");
            if (secondJson != null) {
                Pair<Integer, Boolean> tempPair = new Pair<>(batterJson.getInt("first"),batterJson.getBoolean("second"));
                animationData.put(2, tempPair);
            }
            JSONObject thirdJson = animationDataJson.getJSONObject("3");
            if (thirdJson != null) {
                Pair<Integer, Boolean> tempPair = new Pair<>(batterJson.getInt("first"),batterJson.getBoolean("second"));
                animationData.put(0, tempPair);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return animationData;
    }

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
        if (playerJsonObject.toString() != null) {
            player = gson.fromJson(String.valueOf(playerJsonObject), Player.class);
        }
        return player;
    }

    public Runner[] getRunnersDataFromJson(JSONArray runnersJsonArray, Team homeTeam, Team visitingTeam) {
        Runner[] runners = new Runner[]{null, null, null};
        Gson gson = new Gson();
        for (int i = 0; i < runnersJsonArray.length(); i++) {
            try {
                if (runnersJsonArray.get(i) != null) {
                    JSONObject runnerJson = runnersJsonArray.getJSONObject(i);
                    if (runnerJson != null) {
                        Runner runner;
                        runner = gson.fromJson(String.valueOf(runnerJson), Runner.class);
                        runners[i] = runner;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return runners;
    }
}
