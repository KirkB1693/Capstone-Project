package com.example.android.baseballbythenumbers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.UI.MainActivity.MainActivity;
import com.example.android.baseballbythenumbers.databinding.ActivityGamePlayBinding;

public class GamePlayActivity extends AppCompatActivity {

    private Game game;
    private ActivityGamePlayBinding activityGamePlayBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        activityGamePlayBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_play);

        Intent intent = getIntent();                        // Check intent for an orgId
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                game = extras.getParcelable(MainActivity.NEXT_GAME_EXTRA);
                if (game != null) {
                    activityGamePlayBinding.scoreboardHomeNameTv.setText(game.getHomeTeamName());
                    activityGamePlayBinding.scoreboardVisitorNameTv.setText(game.getVisitingTeamName());
                    GamePlayTabsPagerAdapter gamePlayTabsPagerAdapter = new GamePlayTabsPagerAdapter(this, getSupportFragmentManager(), game);
                    ViewPager viewPager = findViewById(R.id.view_pager);
                    viewPager.setAdapter(gamePlayTabsPagerAdapter);
                    TabLayout tabs = findViewById(R.id.tabs);
                    tabs.setupWithViewPager(viewPager);
                }
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
}