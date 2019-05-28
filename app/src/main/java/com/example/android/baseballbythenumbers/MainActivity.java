package com.example.android.baseballbythenumbers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.TeamGenerator.TeamGenerator;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    TeamGenerator teamGenerator;
    List<Team> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
        JodaTimeAndroid.init(this);

        teams = new ArrayList<Team>();
         teamGenerator = new TeamGenerator(this, 5, 3, 4, 2,
                 1, 1, 1, 1, 1, 1, 1,
                 2, 1, 1, 0);
        newName(null);

    }

    public void newName(View view) {
        teams.add(teamGenerator.generateTeam("Padres", "San Diego", false, 100000000));
        TextView textView = findViewById(R.id.helloWorld);
        textView.setText(teams.toString());
    }
}
