package com.example.android.baseballbythenumbers.UI.GamePlayActivity;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.databinding.FragmentManageGameBinding;

public class ManageGameFragment extends Fragment implements View.OnClickListener {

    private FragmentManageGameBinding manageGameBinding;

    private ManageGameClickListener manageGameClickListener;

    public static final String ARG_GAME_STATE = "game_state";
    private int mGameState;


    public ManageGameFragment(){
    }


    public static ManageGameFragment newInstance(int currentGameState) {
        ManageGameFragment fragment = new ManageGameFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GAME_STATE, currentGameState);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameState = getArguments().getInt(ARG_GAME_STATE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        manageGameBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_game, container, false);
        return manageGameBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mGameState = savedInstanceState.getInt(ARG_GAME_STATE);
            updateUI();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manageGameBinding.pinchHitButton.setOnClickListener(this);
        manageGameBinding.subPitcherButton.setOnClickListener(this);
        manageGameBinding.pauseButton.setOnClickListener(this);
        manageGameBinding.simThisAtBatButton.setOnClickListener(this);
        manageGameBinding.simRestOfGameButton.setOnClickListener(this);
        manageGameBinding.finalizeGameButton.setOnClickListener(this);
        if (getArguments() != null) {
            mGameState = getArguments().getInt(ARG_GAME_STATE);
        }
        updateUI();
    }

    public void setButtonsToDisplay(int gameState) {
        mGameState = gameState;
        Bundle args = new Bundle();
        args.putInt(ARG_GAME_STATE, gameState);
        this.setArguments(args);
        updateUI();
    }

    private void updateUI() {
        switch (mGameState) {
            case GamePlayActivity.INITIAL_GAME_STATE:
                setInitialGameVisibility();
                break;
            case GamePlayActivity.USER_TEAM_PITCHING_GAME_STATE:
                setTeamPitchingVisibility();
                break;
            case GamePlayActivity.USER_TEAM_BATTING_GAME_STATE:
                setTeamBattingVisibility();
                break;
            case GamePlayActivity.SIM_REST_OF_GAME_STATE:
                setSimGameVisibility();
                break;
            case GamePlayActivity.GAME_OVER_STATE:
                setEndOfGameVisibility();
                break;
            default:
                setInitialGameVisibility();
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ManageGameClickListener) {
            manageGameClickListener = (ManageGameClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ManageGameClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        manageGameClickListener = null;
    }

    @Override
    public void onClick(View view) {
        manageGameClickListener.manageGameOnClickResponse(view.getId());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_GAME_STATE, mGameState);
    }


    private void setInitialGameVisibility(){
        manageGameBinding.pinchHitButton.setVisibility(View.GONE);
        manageGameBinding.subPitcherButton.setVisibility(View.GONE);
        manageGameBinding.pauseButton.setVisibility(View.GONE);
        manageGameBinding.finalizeGameButton.setVisibility(View.GONE);
        manageGameBinding.simRestOfGameButton.setVisibility(View.VISIBLE);
        manageGameBinding.simThisAtBatButton.setVisibility(View.VISIBLE);
    }

    private void setTeamBattingVisibility() {
        manageGameBinding.pinchHitButton.setVisibility(View.VISIBLE);
        manageGameBinding.subPitcherButton.setVisibility(View.GONE);
        manageGameBinding.pauseButton.setVisibility(View.GONE);
        manageGameBinding.finalizeGameButton.setVisibility(View.GONE);
        manageGameBinding.simRestOfGameButton.setVisibility(View.VISIBLE);
        manageGameBinding.simThisAtBatButton.setVisibility(View.VISIBLE);
    }

    private void setTeamPitchingVisibility() {
        manageGameBinding.pinchHitButton.setVisibility(View.GONE);
        manageGameBinding.subPitcherButton.setVisibility(View.VISIBLE);
        manageGameBinding.pauseButton.setVisibility(View.GONE);
        manageGameBinding.finalizeGameButton.setVisibility(View.GONE);
        manageGameBinding.simRestOfGameButton.setVisibility(View.VISIBLE);
        manageGameBinding.simThisAtBatButton.setVisibility(View.VISIBLE);
    }

    private void setSimGameVisibility() {
        manageGameBinding.pinchHitButton.setVisibility(View.GONE);
        manageGameBinding.subPitcherButton.setVisibility(View.GONE);
        manageGameBinding.pauseButton.setVisibility(View.VISIBLE);
        manageGameBinding.finalizeGameButton.setVisibility(View.GONE);
        manageGameBinding.simRestOfGameButton.setVisibility(View.GONE);
        manageGameBinding.simThisAtBatButton.setVisibility(View.GONE);
    }

    private void setEndOfGameVisibility(){
        manageGameBinding.finalizeGameButton.setVisibility(View.VISIBLE);
        manageGameBinding.pinchHitButton.setVisibility(View.GONE);
        manageGameBinding.simRestOfGameButton.setVisibility(View.GONE);
        manageGameBinding.simThisAtBatButton.setVisibility(View.GONE);
        manageGameBinding.pauseButton.setVisibility(View.GONE);
        manageGameBinding.subPitcherButton.setVisibility(View.GONE);
    }

    public interface ManageGameClickListener{
        void manageGameOnClickResponse(int buttonId);
    }

}
