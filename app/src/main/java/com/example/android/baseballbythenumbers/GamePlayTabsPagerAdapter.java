package com.example.android.baseballbythenumbers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class GamePlayTabsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public GamePlayTabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BoxScoreFragment boxscore = BoxScoreFragment.newInstance();
                return boxscore;
            case 1:
                PlayByPlayFragment playByPlay = PlayByPlayFragment.newInstance();
                return playByPlay;
            case 2:
                ManageGameFragment manageGame = ManageGameFragment.newInstance();
                return manageGame;
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.tab_text_1);
            case 1:
                return mContext.getResources().getString(R.string.tab_text_2);
            case 2:
                return mContext.getResources().getString(R.string.tab_text_3);
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}