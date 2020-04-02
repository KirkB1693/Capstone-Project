package com.example.android.baseballbythenumbers.UI.RosterActivity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class RosterTabsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private Team mTeam;

    public RosterTabsPagerAdapter(Context context, FragmentManager fm, Team team) {
        super(fm);
        mContext = context;
        mTeam = team;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LineupFragment lineupFragment = LineupFragment.newInstance(mTeam);
                return lineupFragment;
            case 1:
                PitchingRotationFragment pitchingRotationFragment = PitchingRotationFragment.newInstance(mTeam);
                return pitchingRotationFragment;
            case 2:
                PlayerDetailFragment playerDetailFragment = PlayerDetailFragment.newInstance(mTeam);
                return playerDetailFragment;
        }
        return null;
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