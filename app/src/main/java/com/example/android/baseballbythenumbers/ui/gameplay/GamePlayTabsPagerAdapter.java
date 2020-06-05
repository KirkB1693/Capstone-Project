package com.example.android.baseballbythenumbers.ui.gameplay;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Game;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class GamePlayTabsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private Game mGame;
    private Integer mCurrentGameState;

    public GamePlayTabsPagerAdapter(Context context, FragmentManager fm, Game game, Integer currentGameState) {
        super(fm);
        mContext = context;
        mGame = game;
        mCurrentGameState = currentGameState;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ManageGameFragment.newInstance(mCurrentGameState);
            case 1:
                return PlayByPlayFragment.newInstance(mGame);
            case 2:
                return BoxScoreFragment.newInstance(mGame);
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.gameplay_tab_manage_game);
            case 1:
                return mContext.getResources().getString(R.string.gameplay_tab_play_by_play);
            case 2:
                return mContext.getResources().getString(R.string.gameplay_tab_box_score);
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }


}