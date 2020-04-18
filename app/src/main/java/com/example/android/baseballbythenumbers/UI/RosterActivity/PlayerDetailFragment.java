package com.example.android.baseballbythenumbers.UI.RosterActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.databinding.FragmentPlayerDetailBinding;

public class PlayerDetailFragment extends Fragment {

    private static final String ARG_PLAYER = "player";
    private Player mPlayer;
    private FragmentPlayerDetailBinding playerDetailBinding;

    public PlayerDetailFragment(){
    }

    public static PlayerDetailFragment newInstance(Player player) {
        PlayerDetailFragment fragment = new PlayerDetailFragment();
        if (player != null) {
            Bundle args = new Bundle();
            args.putParcelable(ARG_PLAYER, player);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayer = getArguments().getParcelable(ARG_PLAYER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        playerDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player_detail, container, false);
        return playerDetailBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_PLAYER, mPlayer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mPlayer = savedInstanceState.getParcelable(ARG_PLAYER);
            updateUI();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();
    }

    public void setPlayerToDisplayDetail (Player player) {
        mPlayer = player;
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLAYER, player);
        this.setArguments(args);
        updateUI();
    }

    private void updateUI() {
        if (mPlayer != null) {
            playerDetailBinding.playerDetailPlayerNameTv.setText(mPlayer.getName());
        }
    }

}