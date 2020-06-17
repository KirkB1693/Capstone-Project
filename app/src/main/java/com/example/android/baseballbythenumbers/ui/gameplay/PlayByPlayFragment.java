package com.example.android.baseballbythenumbers.ui.gameplay;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Typeface.BOLD;
import static androidx.core.content.ContextCompat.getColor;

public class PlayByPlayFragment extends Fragment {

    private static final String ARG_GAME = "game_for_pbp";

    private Game mGame;

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
        PlayByPlayViewModel mViewModel = new ViewModelProvider(this).get(PlayByPlayViewModel.class);
        mViewModel.setGame(mGame.getGameId());

        Observer<Game> gameObserver = new Observer<Game>() {
            @Override
            public void onChanged(@Nullable Game game) {
                if (game != null) {
                    updatePlayByPlayUI(game);
                }
            }
        };

        mViewModel.getGame().observe(getViewLifecycleOwner(), gameObserver);


    }

    private void updatePlayByPlayUI(Game game) {
        String gameLog = game.getGameLog();
        SpannableStringBuilder formattedGameLog = formatPlayByPlay(gameLog);
        fragmentPlayByPlayBinding.gamePlayByPlayTv.setText(formattedGameLog, TextView.BufferType.SPANNABLE);
        fragmentPlayByPlayBinding.playByPlayScrollview.post(new Runnable() {
            @Override
            public void run() {
                fragmentPlayByPlayBinding.playByPlayScrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private SpannableStringBuilder formatPlayByPlay(String gameLog) {
        SpannableStringBuilder formattedGameLog = new SpannableStringBuilder();
        List<String> result = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new StringReader(gameLog));
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                result.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Context context = getContext();
        for (String line : result) {
            int spanStart = formattedGameLog.length();
            int spanEnd = spanStart + line.length();
            formattedGameLog.append(line);
            if (line.contains("---")) {  // This is an inning separator or similar - make it bold and change text color to black
                formattedGameLog.setSpan(new StyleSpan(BOLD),spanStart,spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
                    formattedGameLog.setSpan(new ForegroundColorSpan(getColor(context, R.color.black)), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            if (line.toLowerCase().contains("out")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
                    formattedGameLog.setSpan(new ForegroundColorSpan(getColor(context, R.color.out)), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (line.toLowerCase().contains("walked.") || line.toLowerCase().contains("single") || line.toLowerCase().contains("double") || line.toLowerCase().contains("triple")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
                    formattedGameLog.setSpan(new ForegroundColorSpan(getColor(context, R.color.safe)), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (line.toLowerCase().contains("home run")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
                    formattedGameLog.setSpan(new ForegroundColorSpan(getColor(context, R.color.primaryColor)), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            if (line.toLowerCase().contains("score")) {
                formattedGameLog.setSpan(new StyleSpan(BOLD),spanStart,spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            formattedGameLog.append("\n");
        }
        return formattedGameLog;
    }

}
