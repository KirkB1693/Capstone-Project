package com.example.android.baseballbythenumbers.ui.gameplay;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Game;

import org.jetbrains.annotations.NotNull;

public class BoxScoreTabsPagerAdapter extends FragmentPagerAdapter {

    // tab titles
    private String[] tabTitles;

    private final Context mContext;
    private Game mGame;

    public BoxScoreTabsPagerAdapter(Context context, FragmentManager fm, Game game) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        mGame = game;
        tabTitles = new String[]{mContext.getResources().getString(R.string.home_tab_label), mContext.getResources().getString(R.string.visitor_tab_label)};
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BoxScoreDetailFragment.newInstance(mGame, true);
            case 1:
                return BoxScoreDetailFragment.newInstance(mGame, false);
            default:
                return new Fragment();
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
