package com.example.android.baseballbythenumbers.JsonHelpers;

import android.support.v4.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

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
}
