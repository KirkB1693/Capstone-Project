package com.example.android.baseballbythenumbers.UI.GamePlayActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.databinding.FragmentBoxScoreBinding;

public class BoxScoreFragment extends Fragment {

    private static final String ARG_GAME = "game";
    private Game mGame;

    private FragmentBoxScoreBinding boxScoreFragmentBinding;

    public BoxScoreFragment(){
    }

    public static BoxScoreFragment newInstance(Game game) {
        BoxScoreFragment fragment = new BoxScoreFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GAME, game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGame = getArguments().getParcelable(ARG_GAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        boxScoreFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_box_score, container, false);
        BoxScoreTabsPagerAdapter boxScoreTabsPagerAdapter = new BoxScoreTabsPagerAdapter(getContext(), getChildFragmentManager(), mGame);
        ViewPager boxScoreViewPager = boxScoreFragmentBinding.boxScoreViewPager;
        boxScoreViewPager.setAdapter(boxScoreTabsPagerAdapter);
        TabLayout tabs = boxScoreFragmentBinding.boxScoreTabLayout;
        tabs.setupWithViewPager(boxScoreViewPager);
        return boxScoreFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
