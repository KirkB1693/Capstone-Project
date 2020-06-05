package com.example.android.baseballbythenumbers.ui.gameplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.databinding.FragmentBoxScoreBinding;
import com.google.android.material.tabs.TabLayout;

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
