package com.example.android.baseballbythenumbers.ui.gameplay;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.R;

public class BoxScoreTabsPagerAdapter extends FragmentPagerAdapter {

    // tab titles
    private String[] tabTitles;

    private final Context mContext;
    private Game mGame;

    public BoxScoreTabsPagerAdapter(Context context, FragmentManager fm, Game game) {
        super(fm);
        mContext = context;
        mGame = game;
        tabTitles = new String[]{mContext.getResources().getString(R.string.home_tab_label), mContext.getResources().getString(R.string.visitor_tab_label)};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BoxScoreDetailFragment.newInstance(mGame, true);
            case 1:
                return BoxScoreDetailFragment.newInstance(mGame, false);
            default:
                return null;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
