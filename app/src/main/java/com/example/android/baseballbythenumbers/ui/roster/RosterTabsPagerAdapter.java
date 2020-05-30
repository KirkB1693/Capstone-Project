package com.example.android.baseballbythenumbers.ui.roster;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.R;

import org.jetbrains.annotations.NotNull;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class RosterTabsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private Team mTeam;
    private Player mCurrentPlayer;
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public RosterTabsPagerAdapter(Context context, FragmentManager fm, Team team, Player currentPlayer) {
        super(fm);
        mContext = context;
        mTeam = team;
        mCurrentPlayer = currentPlayer;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LineupFragment.newInstance(mTeam);
            case 1:
                return PitchingRotationFragment.newInstance(mTeam);
            case 2:
                return PlayerDetailFragment.newInstance(mCurrentPlayer);
        }
        return null;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.roster_tab_lineup);
            case 1:
                return mContext.getResources().getString(R.string.roster_tab_pitching_rotation);
            case 2:
                return mContext.getResources().getString(R.string.roster_tab_player_detail);

        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }


}