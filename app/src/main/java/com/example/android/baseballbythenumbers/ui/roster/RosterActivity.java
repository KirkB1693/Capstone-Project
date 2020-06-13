package com.example.android.baseballbythenumbers.ui.roster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.databinding.ActivityRosterBinding;
import com.example.android.baseballbythenumbers.ui.main.MainActivity;

import org.jetbrains.annotations.NotNull;

public class RosterActivity extends AppCompatActivity implements LineupFragment.OnLineupFragmentInteractionListener, PitchingRotationFragment.OnPitchingRotationFragmentInteractionListener, View.OnClickListener {

    private static final String CURRENT_PLAYER_SELECTED = "current_player_selected";
    private ActivityRosterBinding activityRosterBinding;
    private Fragment currentPagerFragment;
    private Team team;
    private Player current_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        activityRosterBinding = DataBindingUtil.setContentView(this, R.layout.activity_roster);
        activityRosterBinding.rosterToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(activityRosterBinding.rosterToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState != null) {
            current_player = savedInstanceState.getParcelable(CURRENT_PLAYER_SELECTED);
        } else {
            current_player = null;
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                team = extras.getParcelable(MainActivity.USER_TEAM_EXTRA);
                final RosterTabsPagerAdapter rosterTabsPagerAdapter = new RosterTabsPagerAdapter(this, getSupportFragmentManager(), team, current_player);
                activityRosterBinding.rosterViewPager.setAdapter(rosterTabsPagerAdapter);
                activityRosterBinding.rosterTabs.setupWithViewPager(activityRosterBinding.rosterViewPager);

                activityRosterBinding.rosterViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        // current Fragment displayed by pager
                        currentPagerFragment = rosterTabsPagerAdapter.getRegisteredFragment(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                activityRosterBinding.saveFab.setOnClickListener(this);
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
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_PLAYER_SELECTED, current_player);
    }

    @Override
    public void onLineupFragmentInteraction(Player player) {
        activityRosterBinding.rosterViewPager.setCurrentItem(3);
        updatePlayerDetailUI(player);
    }

    @Override
    public void onPitchingRotationFragmentInteraction(Player player) {
        activityRosterBinding.rosterViewPager.setCurrentItem(3);
        updatePlayerDetailUI(player);
    }

    private void updatePlayerDetailUI(Player player) {
        current_player = player;
        if (currentPagerFragment != null && currentPagerFragment instanceof PlayerDetailFragment) {
            ((PlayerDetailFragment) currentPagerFragment).setPlayerToDisplayDetail(player);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_fab) {
            Toast.makeText(this, "In the future this will save the changes you made for use in the next game", Toast.LENGTH_SHORT).show();
        }
    }

}