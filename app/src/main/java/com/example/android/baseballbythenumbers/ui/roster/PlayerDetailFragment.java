package com.example.android.baseballbythenumbers.ui.roster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.constants.Positions;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.databinding.FragmentPlayerDetailBinding;

import java.text.DecimalFormat;
import java.util.Locale;

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
            showPlayerDetails();
            playerDetailBinding.playerDetailPlayerNameTv.setText(mPlayer.getName());
            playerDetailBinding.playerDetailPosition.setText(Positions.getPositionNameFromPrimaryPosition(mPlayer.getPrimaryPosition()));

            DecimalFormat decimalFormat = new DecimalFormat(".000");
            String currentBattingStats = String.format(Locale.getDefault(), getString(R.string.player_detail_batting_stats_format),
                    decimalFormat.format(mPlayer.getBattingStats().get(0).getAverage()),
                    decimalFormat.format(mPlayer.getBattingStats().get(0).getOnBasePct()),
                    mPlayer.getBattingStats().get(0).getHomeRuns(),
                    mPlayer.getBattingStats().get(0).getStolenBases());
            playerDetailBinding.playerDetailBattingStatsThisSeason.setText(currentBattingStats);
            setProgressBarDrawable(mPlayer.getBattingContactRating(), playerDetailBinding.playerDetailBattingContactPb);
            playerDetailBinding.playerDetailBattingContactPb.setProgress(mPlayer.getBattingContactRating());
            setProgressBarDrawable(mPlayer.getBattingEyeRating(), playerDetailBinding.playerDetailBattingEyePb);
            playerDetailBinding.playerDetailBattingEyePb.setProgress(mPlayer.getBattingEyeRating());
            setProgressBarDrawable(mPlayer.getBattingPowerRating(), playerDetailBinding.playerDetailBattingPowerPb);
            playerDetailBinding.playerDetailBattingPowerPb.setProgress(mPlayer.getBattingPowerRating());
            setProgressBarDrawable(mPlayer.getBattingSpeedRating(), playerDetailBinding.playerDetailBattingSpeedPb);
            playerDetailBinding.playerDetailBattingSpeedPb.setProgress(mPlayer.getBattingSpeedRating());

            String currentPitchingStats = String.format(Locale.getDefault(), getString(R.string.player_detail_pitching_stats_format),
                    mPlayer.getPitchingStats().get(0).getERA(),
                    mPlayer.getPitchingStats().get(0).getWHIP(),
                    mPlayer.getPitchingStats().get(0).getWins(),
                    mPlayer.getPitchingStats().get(0).getLosses(),
                    mPlayer.getPitchingStats().get(0).getSaves());
            playerDetailBinding.playerDetailPitchingPlayerStats.setText(currentPitchingStats);
            setProgressBarDrawable(mPlayer.getPitchingStaminaRating(), playerDetailBinding.playerDetailPitchingStaminaPb);
            playerDetailBinding.playerDetailPitchingStaminaPb.setProgress(mPlayer.getPitchingStaminaRating());
            setProgressBarDrawable(mPlayer.getAccuracyRating(), playerDetailBinding.playerDetailPitchingAccuracyPb);
            playerDetailBinding.playerDetailPitchingAccuracyPb.setProgress(mPlayer.getAccuracyRating());
            setProgressBarDrawable(mPlayer.getDeceptionRating(), playerDetailBinding.playerDetailPitchingDeceptionPb);
            playerDetailBinding.playerDetailPitchingDeceptionPb.setProgress(mPlayer.getDeceptionRating());
            setProgressBarDrawable(mPlayer.getMovementRating(), playerDetailBinding.playerDetailPitchingMovementPb);
            playerDetailBinding.playerDetailPitchingMovementPb.setProgress(mPlayer.getMovementRating());
        } else {
            showEmptyPlayerDetails();
        }
    }

    private void showEmptyPlayerDetails() {
        playerDetailBinding.emptyPlayerDetailTv.setVisibility(View.VISIBLE);
        playerDetailBinding.playerDetailPitchingStatsLl.setVisibility(View.GONE);
        playerDetailBinding.playerDetailHittingBarsLl.setVisibility(View.GONE);
        playerDetailBinding.playerDetailHittingStatsLl.setVisibility(View.GONE);
        playerDetailBinding.playerDetailPitchingBarsLl.setVisibility(View.GONE);
        playerDetailBinding.playerDetailHittingBarLabelsLl.setVisibility(View.GONE);
        playerDetailBinding.playerDetailPitchingBarLabelsLl.setVisibility(View.GONE);
        playerDetailBinding.playerDetailBattingStatsLabel.setVisibility(View.GONE);
        playerDetailBinding.playerDetailPitchingStatsLabel.setVisibility(View.GONE);
    }

    private void showPlayerDetails() {
        playerDetailBinding.emptyPlayerDetailTv.setVisibility(View.GONE);
        playerDetailBinding.playerDetailPitchingStatsLl.setVisibility(View.VISIBLE);
        playerDetailBinding.playerDetailHittingBarsLl.setVisibility(View.VISIBLE);
        playerDetailBinding.playerDetailHittingStatsLl.setVisibility(View.VISIBLE);
        playerDetailBinding.playerDetailPitchingBarsLl.setVisibility(View.VISIBLE);
        playerDetailBinding.playerDetailHittingBarLabelsLl.setVisibility(View.VISIBLE);
        playerDetailBinding.playerDetailPitchingBarLabelsLl.setVisibility(View.VISIBLE);
        playerDetailBinding.playerDetailBattingStatsLabel.setVisibility(View.VISIBLE);
        playerDetailBinding.playerDetailPitchingStatsLabel.setVisibility(View.VISIBLE);
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