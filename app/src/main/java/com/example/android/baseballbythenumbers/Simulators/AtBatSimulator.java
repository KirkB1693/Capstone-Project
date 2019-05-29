package com.example.android.baseballbythenumbers.Simulators;

import com.example.android.baseballbythenumbers.Data.Player;

import java.util.ArrayList;

public class AtBatSimulator {

    ArrayList<Integer> count;

    int outs;

    int currentBatter;

    ArrayList<Player> lineup;

    ArrayList<Player> runners;

    ArrayList<Player> defense;

    public AtBatSimulator(ArrayList<Integer> count, int outs, int currentBatter, ArrayList<Player> lineup, ArrayList<Player> runners, ArrayList<Player> defense){
        this.count = count;
        this.outs = outs;
        this.currentBatter = currentBatter;
        this.lineup = lineup;
        this.runners = runners;
        this.defense = defense;
    }


}
