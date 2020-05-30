package com.example.android.baseballbythenumbers.ui.roster;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.baseballbythenumbers.constants.Positions;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.databinding.FragmentPlayerDetailBinding;

import java.text.DecimalFormat;

public class PlayerDetailFragment extends Fragment {

    private static final String ARG_PLAYER = "player";
    private Player mPlayer;
    private FragmentPlayerDetailBinding playerDetailBinding;

    public PlayerDetailFragment() {
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
        if (getArguments() != null) {
            mPlayer = getArguments().getParcelable(ARG_PLAYER);
        }
        updateUI();
    }

    public void setPlayerToDisplayDetail(Player player) {
        mPlayer = player;
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLAYER, player);
        this.setArguments(args);
        updateUI();
    }

    private void updateUI() {
        if (mPlayer != null) {
            playerDetailBinding.playerDetailPlayerNameTv.setText(mPlayer.getName());
            playerDetailBinding.playerDetailPosition.setText(Positions.getPositionNameFromPrimaryPosition(mPlayer.getPrimaryPosition()));

            DecimalFormat decimalFormat = new DecimalFormat(".000");
            String currentBattingStats = "AVG " + decimalFormat.format(mPlayer.getBattingStats().get(0).getAverage()) + ", OBP " + decimalFormat.format(mPlayer.getBattingStats().get(0).getOnBasePct()) +
                    ", HR " + mPlayer.getBattingStats().get(0).getHomeRuns() + ", SB " + mPlayer.getBattingStats().get(0).getStolenBases();
            playerDetailBinding.playerDetailBattingStatsThisSeason.setText(currentBattingStats);
            setProgressBarDrawable(mPlayer.getBattingContactRating(), playerDetailBinding.playerDetailBattingContactPb);
            playerDetailBinding.playerDetailBattingContactPb.setProgress(mPlayer.getBattingContactRating());
            setProgressBarDrawable(mPlayer.getBattingEyeRating(), playerDetailBinding.playerDetailBattingEyePb);
            playerDetailBinding.playerDetailBattingEyePb.setProgress(mPlayer.getBattingEyeRating());
            setProgressBarDrawable(mPlayer.getBattingPowerRating(), playerDetailBinding.playerDetailBattingPowerPb);
            playerDetailBinding.playerDetailBattingPowerPb.setProgress(mPlayer.getBattingPowerRating());
            setProgressBarDrawable(mPlayer.getBattingSpeedRating(), playerDetailBinding.playerDetailBattingSpeedPb);
            playerDetailBinding.playerDetailBattingSpeedPb.setProgress(mPlayer.getBattingSpeedRating());

            String currentPitchingStats = "ERA " + mPlayer.getPitchingStats().get(0).getERA() + ", WHIP " + mPlayer.getPitchingStats().get(0).getWHIP() +
                    ", W " + mPlayer.getPitchingStats().get(0).getWins() + ", L " + mPlayer.getPitchingStats().get(0).getLosses() + ", S " + mPlayer.getPitchingStats().get(0).getSaves();
            playerDetailBinding.playerDetailPitchingPlayerStats.setText(currentPitchingStats);
            setProgressBarDrawable(mPlayer.getPitchingStaminaRating(), playerDetailBinding.playerDetailPitchingStaminaPb);
            playerDetailBinding.playerDetailPitchingStaminaPb.setProgress(mPlayer.getPitchingStaminaRating());
            setProgressBarDrawable(mPlayer.getAccuracyRating(), playerDetailBinding.playerDetailPitchingAccuracyPb);
            playerDetailBinding.playerDetailPitchingAccuracyPb.setProgress(mPlayer.getAccuracyRating());
            setProgressBarDrawable(mPlayer.getDeceptionRating(), playerDetailBinding.playerDetailPitchingDeceptionPb);
            playerDetailBinding.playerDetailPitchingDeceptionPb.setProgress(mPlayer.getDeceptionRating());
            setProgressBarDrawable(mPlayer.getMovementRating(), playerDetailBinding.playerDetailPitchingMovementPb);
            playerDetailBinding.playerDetailPitchingMovementPb.setProgress(mPlayer.getMovementRating());
        }
    }

    private void setProgressBarDrawable(int progress, ProgressBar progressBar) {
        if (getContext() != null) {
            if (progress < 40) {
                progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.player_rating_bar_red));
            } else if (progress < 60) {
                progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.player_rating_bar_yellow));
            } else {
                progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.player_rating_bar_green));
            }
        }
    }
}