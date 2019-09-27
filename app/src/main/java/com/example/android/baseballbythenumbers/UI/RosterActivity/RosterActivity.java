package com.example.android.baseballbythenumbers.UI.RosterActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.UI.MainActivity.MainActivity;
import com.example.android.baseballbythenumbers.databinding.ActivityRosterBinding;

public class RosterActivity extends AppCompatActivity implements LineupFragment.OnLineupFragmentInteractionListener, PitchingRotationFragment.OnPitchingRotationFragmentInteractionListener {

    private ActivityRosterBinding activityRosterBinding;
    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        activityRosterBinding = DataBindingUtil.setContentView(this, R.layout.activity_roster);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                team = extras.getParcelable(MainActivity.USER_TEAM_EXTRA);
                RosterTabsPagerAdapter rosterTabsPagerAdapter = new RosterTabsPagerAdapter(this, getSupportFragmentManager(), team);
                activityRosterBinding.rosterViewPager.setAdapter(rosterTabsPagerAdapter);
                activityRosterBinding.rosterTabs.setupWithViewPager(activityRosterBinding.rosterViewPager);
                FloatingActionButton fab = findViewById(R.id.fab);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onLineupFragmentInteraction(Player player) {

    }


    @Override
    public void onPitchingRotationFragmentInteraction(Player player) {

    }
}