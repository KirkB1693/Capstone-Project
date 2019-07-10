package com.example.android.baseballbythenumbers;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.BattingLine;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.PitchingLine;
import com.example.android.baseballbythenumbers.databinding.FragmentBoxScoreDetailBinding;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BoxScoreDetailFragment extends Fragment {

    private static final String ARG_GAME = "game";
    private static final String ARG_IS_HOME_TEAM = "is_this_home_team";
    private BoxScoreDetailViewModel mViewModel;
    private Game mGame;
    private boolean isHomeTeam;

    private FragmentBoxScoreDetailBinding boxScoreDetailBinding;

    private Observer<Game> gameObserver;
    private Observer<List<BattingLine>> battingLineObserver;
    private Observer<List<PitchingLine>> pitchingLineObserver;

    public BoxScoreDetailFragment() {
        // Required empty public constructor
    }

    public static BoxScoreDetailFragment newInstance(Game game, boolean isThisHomeTeam) {
        BoxScoreDetailFragment fragment = new BoxScoreDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GAME, game);
        args.putBoolean(ARG_IS_HOME_TEAM, isThisHomeTeam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGame = getArguments().getParcelable(ARG_GAME);
            isHomeTeam = getArguments().getBoolean(ARG_IS_HOME_TEAM);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        boxScoreDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_box_score_detail, container, false);
        return boxScoreDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(BoxScoreDetailViewModel.class);
        mViewModel.setGame(mGame.getGameId());

        gameObserver = new Observer<Game>() {
            @Override
            public void onChanged(@Nullable final Game game) {
                //Update the UI
                mGame = game;
                updateFragmentUI(mGame);
            }
        };

        mViewModel.getGame().observe(this, gameObserver);
    }

    private void updateFragmentUIBattingLines(List<BattingLine> battingLines) {
        Collections.sort(battingLines);
        TableLayout tableLayout = boxScoreDetailBinding.boxScoreDetailHittersTable;
        tableLayout.removeAllViews();


        LayoutInflater inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        TableLayout labelsRow = (TableLayout) inflater.inflate(R.layout.box_score_detail_table_batting_row_labels, null);

        tableLayout.addView(labelsRow);

        if (battingLines != null) {
            if (!battingLines.isEmpty()) {
                for (BattingLine battingLine : battingLines) {
                    TableLayout dataRow = (TableLayout) inflater.inflate(R.layout.box_score_detail_table_empty_batting_row, null);
                    TextView batterTextView = dataRow.findViewById(R.id.box_score_details_empty_batter_tv);
                    StringBuilder batterName = new StringBuilder();
                    if (battingLine.isSubstitute()) {
                        batterName.append("      ");
                    }
                    batterName.append(battingLine.getBatterName());
                    batterTextView.setText(batterName);
                    TextView abTextView = dataRow.findViewById(R.id.box_score_details_empty_AB_tv);
                    abTextView.setText(String.format(Locale.getDefault(),"%d",battingLine.getAtBats()));
                    TextView rTextView = dataRow.findViewById(R.id.box_score_details_empty_R_tv);
                    rTextView.setText(String.format(Locale.getDefault(),"%d",battingLine.getRuns()));
                    TextView hTextView = dataRow.findViewById(R.id.box_score_details_empty_H_tv);
                    hTextView.setText(String.format(Locale.getDefault(),"%d",battingLine.getHits()));
                    TextView rbiTextView = dataRow.findViewById(R.id.box_score_details_empty_RBI_tv);
                    rbiTextView.setText(String.format(Locale.getDefault(),"%d",battingLine.getRbis()));
                    TextView hrTextView = dataRow.findViewById(R.id.box_score_details_empty_HR_tv);
                    hrTextView.setText(String.format(Locale.getDefault(),"%d",battingLine.getHomeRuns()));
                    TextView bbTextView = dataRow.findViewById(R.id.box_score_details_empty_BB_tv);
                    bbTextView.setText(String.format(Locale.getDefault(),"%d",battingLine.getWalks()));
                    TextView kTextView = dataRow.findViewById(R.id.box_score_details_empty_K_tv);
                    kTextView.setText(String.format(Locale.getDefault(),"%d",battingLine.getStrikeOuts()));
                    TextView lobTextView = dataRow.findViewById(R.id.box_score_details_empty_LOB_tv);
                    lobTextView.setText(String.format(Locale.getDefault(),"%d",battingLine.getLeftOnBase()));
                    TextView avgTextView = dataRow.findViewById(R.id.box_score_details_empty_AVG_tv);
                    DecimalFormat decimalFormat = new DecimalFormat(".000");
                    String average = decimalFormat.format(battingLine.getAverage());
                    avgTextView.setText(average);
                    TextView obpTextView = dataRow.findViewById(R.id.box_score_details_empty_OBP_tv);
                    String obp = decimalFormat.format(battingLine.getOnBasePct());
                    obpTextView.setText(obp);

                    tableLayout.addView(dataRow);
                }
            }
        }

        tableLayout.setStretchAllColumns(true);
    }

    private void updateFragmentUI(Game game) {

        if (isHomeTeam) {
            boxScoreDetailBinding.boxScoreDetailTeamNameTv.setText(game.getHomeTeamName());
        } else {
            boxScoreDetailBinding.boxScoreDetailTeamNameTv.setText(game.getVisitingTeamName());
        }

        if (game.getHomeBoxScore() != null && game.getVisitorBoxScore() != null) {

            if (isHomeTeam) {
                mViewModel.setBattingLines(game.getHomeBoxScore().getBoxScoreId());
                mViewModel.setPitchingLines(game.getHomeBoxScore().getBoxScoreId());
            } else {
                mViewModel.setBattingLines(game.getVisitorBoxScore().getBoxScoreId());
                mViewModel.setPitchingLines(game.getVisitorBoxScore().getBoxScoreId());
            }

            battingLineObserver = new Observer<List<BattingLine>>() {
                @Override
                public void onChanged(@Nullable List<BattingLine> battingLines) {
                    updateFragmentUIBattingLines(battingLines);
                }
            };

            mViewModel.getBattingLines().observe(this, battingLineObserver);

            pitchingLineObserver = new Observer<List<PitchingLine>>() {
                @Override
                public void onChanged(@Nullable List<PitchingLine> pitchingLines) {
                    updateFragmentUIPitchingLines(pitchingLines);
                }
            };

            mViewModel.getPitchingLines().observe(this, pitchingLineObserver);
        }
    }

    private void updateFragmentUIPitchingLines(List<PitchingLine> pitchingLines) {
        Collections.sort(pitchingLines);
        TableLayout tableLayout = boxScoreDetailBinding.boxScoreDetailPitchersTable;
        tableLayout.removeAllViews();


        LayoutInflater inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        TableLayout labelsRow = (TableLayout) inflater.inflate(R.layout.box_score_detail_table_pitching_row_labels, null);

        tableLayout.addView(labelsRow);

        if (pitchingLines != null) {
            if (!pitchingLines.isEmpty()) {
                for (PitchingLine pitchingLine : pitchingLines) {
                    TableLayout dataRow = (TableLayout) inflater.inflate(R.layout.box_score_detail_table_empty_pitching_row, null);
                    TextView pitcherTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_tv);
                    pitcherTextView.setText(pitchingLine.getPitcherName());
                    TextView ipTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_IP_tv);
                    DecimalFormat ipDecimalFormat = new DecimalFormat("#.#");
                    ipTextView.setText(ipDecimalFormat.format(pitchingLine.getInningsPitched()));
                    TextView hTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_H_tv);
                    hTextView.setText(String.format(Locale.getDefault(),"%d",pitchingLine.getHitsAllowed()));

                    TextView rTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_R_tv);
                    rTextView.setText(String.format(Locale.getDefault(),"%d",pitchingLine.getRunsAllowed()));

                    TextView erTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_ER_tv);
                    erTextView.setText(String.format(Locale.getDefault(),"%d",pitchingLine.getEarnedRuns()));

                    TextView bbTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_BB_tv);
                    bbTextView.setText(String.format(Locale.getDefault(),"%d",pitchingLine.getWalksAllowed()));

                    TextView soTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_SO_tv);
                    soTextView.setText(String.format(Locale.getDefault(),"%d",pitchingLine.getStrikeOutsMade()));

                    TextView hrTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_HR_tv);
                    hrTextView.setText(String.format(Locale.getDefault(),"%d",pitchingLine.getHomeRunsAllowed()));

                    TextView eraTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_ERA_tv);
                    eraTextView.setText(pitchingLine.getEra());

                    TextView whipTextView = dataRow.findViewById(R.id.box_score_details_empty_pitcher_WHIP_tv);
                    whipTextView.setText(pitchingLine.getWhip());

                    tableLayout.addView(dataRow);
                }
            }
        }

        tableLayout.setStretchAllColumns(true);
    }

}
