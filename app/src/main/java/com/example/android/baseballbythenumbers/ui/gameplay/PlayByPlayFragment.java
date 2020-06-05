package com.example.android.baseballbythenumbers.ui.gameplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.databinding.FragmentPlayByPlayBinding;
import com.example.android.baseballbythenumbers.viewModels.PlayByPlayViewModel;

public class PlayByPlayFragment extends Fragment {

    private static final String ARG_GAME = "game_for_pbp";
    private PlayByPlayViewModel mViewModel;

    private Game mGame;

    private Observer<Game> gameObserver;

    private FragmentPlayByPlayBinding fragmentPlayByPlayBinding;

    public PlayByPlayFragment(){
        // Required empty public constructor
    }

    public static PlayByPlayFragment newInstance(Game game) {
        PlayByPlayFragment fragment = new PlayByPlayFragment();
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
        fragmentPlayByPlayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_by_play, container, false);
        return fragmentPlayByPlayBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlayByPlayViewModel.class);
        mViewModel.setGame(mGame.getGameId());

        gameObserver = new Observer<Game>() {
            @Override
            public void onChanged(@Nullable Game game) {
                updatePlayByPlayUI(game);
            }
        };

        mViewModel.getGame().observe(getViewLifecycleOwner(), gameObserver);


    }

    private void updatePlayByPlayUI(Game game) {
        fragmentPlayByPlayBinding.gamePlayByPlayTv.setText(game.getGameLog());
        fragmentPlayByPlayBinding.playByPlayScrollview.post(new Runnable() {
            @Override
            public void run() {
                fragmentPlayByPlayBinding.playByPlayScrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

}
