package com.example.android.baseballbythenumbers.ui.gameplay;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.ResourceProvider;
import com.example.android.baseballbythenumbers.data.BattingLine;
import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Runner;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.databinding.ActivityGamePlayBinding;
import com.example.android.baseballbythenumbers.repository.Repository;
import com.example.android.baseballbythenumbers.ui.main.MainActivity;
import com.example.android.baseballbythenumbers.viewModels.GamePlayViewModel;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_PITCHER;

public class GamePlayActivity extends AppCompatActivity implements ManageGameFragment.ManageGameClickListener, View.OnClickListener {

    public static final int RUNNER_ON_THIRD_KEY = 3;
    public static final int RUNNER_ON_SECOND_KEY = 2;
    public static final int RUNNER_ON_FIRST_KEY = 1;
    public static final int BATTER_KEY = 0;

    public static final String CURRENT_GAME_STATE = "current_game_state";
    public static final int INITIAL_GAME_STATE = 0;
    public static final int USER_TEAM_PITCHING_GAME_STATE = 1;
    public static final int USER_TEAM_BATTING_GAME_STATE = 2;
    public static final int SIM_REST_OF_GAME_STATE = 3;
    public static final int GAME_OVER_STATE = 4;

    private static final String SAVED_HOME_ERRORS_AT_START = "saved_home_errors_at_start";
    private static final String SAVED_VISITOR_ERRORS_AT_START = "saved_visitor_errors_at_start";
    public static final String GAME_PLAY_FINISHED = "game_play_finished";

    private int currentGameState;

    private Game game;
    private Team homeTeam;
    private Team visitingTeam;
    private String usersTeamName;
    private Organization organization;
    private Repository repository;
    private ResourceProvider resourceProvider;
    private int homeErrorsAtGameStart;
    private int visitorErrorsAtGameStart;
    private boolean simRestOfGameInProgress;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private GamePlayViewModel gamePlayViewModel;
    private ActivityGamePlayBinding activityGamePlayBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        activityGamePlayBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_play);
        gamePlayViewModel = new ViewModelProvider(this).get(GamePlayViewModel.class);
        resourceProvider = ((BaseballByTheNumbersApp) getApplication()).getResourceProvider();

        if (savedInstanceState != null) {
            currentGameState = savedInstanceState.getInt(CURRENT_GAME_STATE);
            game = gamePlayViewModel.getGame();
            if (game != null) {
                organization = gamePlayViewModel.getOrganization();
                homeTeam = gamePlayViewModel.getHomeTeam();
                visitingTeam = gamePlayViewModel.getVisitingTeam();
                usersTeamName = gamePlayViewModel.getUsersTeamName();
                homeErrorsAtGameStart = savedInstanceState.getInt(SAVED_HOME_ERRORS_AT_START);
                visitorErrorsAtGameStart = savedInstanceState.getInt(SAVED_VISITOR_ERRORS_AT_START);
                GamePlayTabsPagerAdapter gamePlayTabsPagerAdapter = new GamePlayTabsPagerAdapter(this, getSupportFragmentManager(), game, currentGameState);
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(gamePlayTabsPagerAdapter);
                TabLayout tabs = findViewById(R.id.tabs);
                tabs.setupWithViewPager(viewPager);
                activityGamePlayBinding.throwPitchFab.setOnClickListener(this);
                repository = ((BaseballByTheNumbersApp) getApplication()).getRepository();
                continueGame();
            }
        } else {
            currentGameState = INITIAL_GAME_STATE;
            Intent intent = getIntent();
            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    game = extras.getParcelable(MainActivity.NEXT_GAME_EXTRA);
                    if (game != null) {
                        organization = extras.getParcelable(MainActivity.ORGANIZATION_EXTRA);
                        homeTeam = extras.getParcelable(MainActivity.HOME_TEAM_EXTRA);
                        visitingTeam = extras.getParcelable(MainActivity.VISITING_TEAM_EXTRA);
                        usersTeamName = extras.getString(MainActivity.USER_TEAM_NAME);
                        GamePlayTabsPagerAdapter gamePlayTabsPagerAdapter = new GamePlayTabsPagerAdapter(this, getSupportFragmentManager(), game, currentGameState);
                        ViewPager viewPager = findViewById(R.id.view_pager);
                        viewPager.setAdapter(gamePlayTabsPagerAdapter);
                        TabLayout tabs = findViewById(R.id.tabs);
                        tabs.setupWithViewPager(viewPager);
                        activityGamePlayBinding.throwPitchFab.setOnClickListener(this);
                        repository = ((BaseballByTheNumbersApp) getApplication()).getRepository();
                        gamePlayViewModel.setGame(game);
                        gamePlayViewModel.setHomeTeam(homeTeam);
                        gamePlayViewModel.setVisitingTeam(visitingTeam);
                        gamePlayViewModel.setUsersTeamName(usersTeamName);
                        gamePlayViewModel.setOrganization(organization);
                        gamePlayViewModel.setRepository(repository);
                        initializeScoreboard();
                        setupGame();

                    }
                }
            }
        }


    }


    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_GAME_STATE, currentGameState);
        outState.putInt(SAVED_HOME_ERRORS_AT_START, homeErrorsAtGameStart);
        outState.putInt(SAVED_VISITOR_ERRORS_AT_START, visitorErrorsAtGameStart);
        scheduledExecutorService.shutdownNow();
    }


    private void initializeScoreboard() {
        activityGamePlayBinding.scoreboardHomeNameTv.setText(game.getHomeTeamName());
        activityGamePlayBinding.scoreboardVisitorNameTv.setText(game.getVisitingTeamName());
        activityGamePlayBinding.scoreboardRunsVisitorTv.setText("0");
        activityGamePlayBinding.scoreboardRunsHomeTv.setText("0");
        activityGamePlayBinding.scoreboardErrorsVisitorTv.setText("0");
        activityGamePlayBinding.scoreboardErrorsHomeTv.setText("0");
        activityGamePlayBinding.scoreboardHitsVisitorTv.setText("0");
        activityGamePlayBinding.scoreboardHitsHomeTv.setText("0");
    }

    private void setupGame() {
        gamePlayViewModel.setupGameSimulator(resourceProvider);
        gamePlayViewModel.getGameSimulator().startGame();

        homeErrorsAtGameStart = getTotalErrorsForTeam(homeTeam);
        visitorErrorsAtGameStart = getTotalErrorsForTeam(visitingTeam);

        updateUI();
    }


    private void continueGame() {
        gamePlayViewModel.getGameSimulator().continueGame();
        if (currentGameState == SIM_REST_OF_GAME_STATE) {
            simRestOfGame();
        }
        updateUI();
    }

    private int getTotalErrorsForTeam(Team team) {
        int errors = 0;
        for (Player player : team.getPlayers()) {
            errors += player.getBattingStatsForYear(organization.getCurrentYear()).getErrors();
        }
        return errors;
    }


    @Override
    public void onBackPressed() {
        // do something on back.
        if (isGameOver()) {
            processEndOfGame();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GAME_PLAY_FINISHED, isGameOver());
            startActivity(intent);
            finish();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.game_play_exit_question);
            alertDialogBuilder.setPositiveButton(R.string.game_play_exit_lose_progress_option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton(R.string.game_play_exit_stay_here_option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing as user is staying here
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    @Override
    public void manageGameOnClickResponse(int buttonId) {
        switch (buttonId) {
            case R.id.pinch_hit_button:
                Toast.makeText(this, R.string.pinch_hit_button_toast_message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.sim_this_at_bat_button:
                if (!isGameOver()) {
                    simCurrentAtBat();
                }
                break;
            case R.id.pause_button:
                simRestOfGameInProgress = false;
                scheduledExecutorService.shutdownNow();
                activityGamePlayBinding.throwPitchFab.setVisibility(View.VISIBLE);
                if (isGameOver()) {
                    setEndGameButtonsOnManageGameFragment();
                } else {
                    setButtonsBasedOnHittingTeam();
                }
                break;
            case R.id.sub_pitcher_button:
                Toast.makeText(this, R.string.sub_pitcher_button_toast_message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.sim_rest_of_game_button:
                if (!isGameOver()) {
                    simRestOfGame();
                }
                break;
            case R.id.finalize_game_button:
                onBackPressed();
        }
    }

    private void setButtonsBasedOnHittingTeam() {
        if (!simRestOfGameInProgress) {
            String fragmentTag = makeFragmentName(R.id.view_pager, 0);
            Fragment manageGameFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if (manageGameFragment != null && manageGameFragment.isVisible()) {
                if (gamePlayViewModel.getGameSimulator().isVisitorHitting()) {
                    if (visitingTeam.getTeamName().equals(usersTeamName)) {
                        currentGameState = USER_TEAM_BATTING_GAME_STATE;
                    } else {
                        currentGameState = USER_TEAM_PITCHING_GAME_STATE;
                    }
                } else {
                    if (visitingTeam.getTeamName().equals(usersTeamName)) {
                        currentGameState = USER_TEAM_PITCHING_GAME_STATE;
                    } else {
                        currentGameState = USER_TEAM_BATTING_GAME_STATE;
                    }
                }
                ((ManageGameFragment) manageGameFragment).setButtonsToDisplay(currentGameState);
            }
        }
    }

    private String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    private void simRestOfGame() {
        simRestOfGameInProgress = true;
        currentGameState = SIM_REST_OF_GAME_STATE;
        activityGamePlayBinding.throwPitchFab.setVisibility(View.GONE);
        String fragmentTag = makeFragmentName(R.id.view_pager, 0);
        Fragment manageGameFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (manageGameFragment != null && manageGameFragment.isVisible()) {
            ((ManageGameFragment) manageGameFragment).setButtonsToDisplay(currentGameState);
        }
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                GamePlayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gamePlayViewModel.getGameSimulator().simAtBatWithHumanControl();
                        updateUI();
                    }
                });

            }
        }, 0L, 2500L, TimeUnit.MILLISECONDS);


    }

    private void simCurrentAtBat() {
        gamePlayViewModel.getGameSimulator().simAtBatWithHumanControl();
        updateUI();
    }

    private void updateUI() {
        Player currentPitcher = gamePlayViewModel.getGameSimulator().getDefense().get(SCOREKEEPING_PITCHER);
        if (currentPitcher != null) {
            displayCurrentPitcher(currentPitcher);
        }
        Player currentBatter = gamePlayViewModel.getGameSimulator().getLineup().get(gamePlayViewModel.getGameSimulator().getCurrentBatter());
        if (currentBatter != null) {
            displayCurrentBatter(currentBatter);
        }
        activityGamePlayBinding.scoreboardVisitorNameTv.setText(visitingTeam.getTeamName());
        activityGamePlayBinding.scoreboardHomeNameTv.setText(homeTeam.getTeamName());
        setInningScoresOnScoreboard();
        updateVisitorHits();
        activityGamePlayBinding.scoreboardErrorsVisitorTv.setText(String.format(Locale.getDefault(), "%d", (getTotalErrorsForTeam(visitingTeam) - visitorErrorsAtGameStart)));
        updateHomeHits();
        activityGamePlayBinding.scoreboardErrorsHomeTv.setText(String.format(Locale.getDefault(), "%d", (getTotalErrorsForTeam(homeTeam) - homeErrorsAtGameStart)));

        if (gamePlayViewModel.getGameSimulator().isVisitorHitting()) {
            activityGamePlayBinding.scoreboardVisitorIconIv.setVisibility(View.VISIBLE);
            activityGamePlayBinding.scoreboardHomeIconIv.setVisibility(View.INVISIBLE);


        } else {
            activityGamePlayBinding.scoreboardVisitorIconIv.setVisibility(View.INVISIBLE);
            activityGamePlayBinding.scoreboardHomeIconIv.setVisibility(View.VISIBLE);

        }
        activityGamePlayBinding.gamePlayAtBatResultTv.setText(gamePlayViewModel.getGameSimulator().getCurrentAtBatSummary());
        activityGamePlayBinding.scoreboardRunsVisitorTv.setText(String.format(Locale.getDefault(), "%d", gamePlayViewModel.getGameSimulator().getVisitorScore()));
        activityGamePlayBinding.scoreboardRunsHomeTv.setText(String.format(Locale.getDefault(), "%d", gamePlayViewModel.getGameSimulator().getHomeScore()));
        animateRunners();
        if (isGameOver()) {
            activityGamePlayBinding.gamePlayAtBatResultTv.setText(R.string.game_over_message);
            setEndGameButtonsOnManageGameFragment();
            scheduledExecutorService.shutdownNow();
            try {
                scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            setButtonsBasedOnHittingTeam();
        }
    }

    private void setInningScoresOnScoreboard() {
        int inning = 0;
        for (Integer runs : gamePlayViewModel.getGameSimulator().getRunsScoredByInningForVisitingTeam()) {
            switch (inning) {
                case 0:
                    activityGamePlayBinding.inning1VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 1:
                    activityGamePlayBinding.inning2VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 2:
                    activityGamePlayBinding.inning3VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 3:
                    activityGamePlayBinding.inning4VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 4:
                    activityGamePlayBinding.inning5VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 5:
                    activityGamePlayBinding.inning6VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 6:
                    activityGamePlayBinding.inning7VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 7:
                    activityGamePlayBinding.inning8VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 8:
                    activityGamePlayBinding.inning9VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 9:
                    if (!isGameOver()) {
                        activityGamePlayBinding.inning10LabelTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning10VisitorScoreTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning10HomeScoreTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning10VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    }
                    break;
                case 10:
                    if (!isGameOver()) {
                        activityGamePlayBinding.inning11LabelTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning11VisitorScoreTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning11HomeScoreTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning11VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    }
                    break;
                case 11:
                    if (!isGameOver()) {
                        activityGamePlayBinding.inning12LabelTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning12VisitorScoreTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning12HomeScoreTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning12VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    }
                    break;
                default:
                    if (!isGameOver()) {
                        activityGamePlayBinding.inning12LabelTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning12LabelTv.setText(String.format(Locale.getDefault(), "%d", (inning + 1)));
                        activityGamePlayBinding.inning12VisitorScoreTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning12HomeScoreTv.setVisibility(View.VISIBLE);
                        activityGamePlayBinding.inning12VisitorScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    }
                    break;
            }
            inning++;
        }
        inning = 0;
        for (Integer runs : gamePlayViewModel.getGameSimulator().getRunsScoredByInningForHomeTeam()) {
            switch (inning) {
                case 0:
                    activityGamePlayBinding.inning1HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 1:
                    activityGamePlayBinding.inning2HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 2:
                    activityGamePlayBinding.inning3HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 3:
                    activityGamePlayBinding.inning4HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 4:
                    activityGamePlayBinding.inning5HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 5:
                    activityGamePlayBinding.inning6HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 6:
                    activityGamePlayBinding.inning7HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 7:
                    activityGamePlayBinding.inning8HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 8:
                    if (homeTeamBattedInNinth()) {
                        activityGamePlayBinding.inning9HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    }
                    break;
                case 9:
                    activityGamePlayBinding.inning10HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                case 10:
                    activityGamePlayBinding.inning11HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
                default:
                    activityGamePlayBinding.inning12HomeScoreTv.setText(String.format(Locale.getDefault(), "%d", runs));
                    break;
            }
            inning++;

        }

    }

    private void setEndGameButtonsOnManageGameFragment() {
        currentGameState = GAME_OVER_STATE;
        String fragmentTag = makeFragmentName(R.id.view_pager, 0);
        Fragment manageGameFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (manageGameFragment != null && manageGameFragment.isVisible()) {
            ((ManageGameFragment) manageGameFragment).setButtonsToDisplay(currentGameState);
        }
    }

    private void processEndOfGame() {

        game.setHomeScore(gamePlayViewModel.getGameSimulator().getHomeScore());
        game.setVisitorScore(gamePlayViewModel.getGameSimulator().getVisitorScore());
        game.setPlayedGame(true);

        repository.updateGame(game);
        repository.updateBoxScore(game.getHomeBoxScore());
        repository.insertAllBattingLines(game.getHomeBoxScore().getBattingLines());
        repository.insertAllPitchingLines(game.getHomeBoxScore().getPitchingLines());
        repository.updateBoxScore(game.getVisitorBoxScore());
        repository.insertAllBattingLines(game.getVisitorBoxScore().getBattingLines());
        repository.insertAllPitchingLines(game.getVisitorBoxScore().getPitchingLines());
        repository.updateTeam(homeTeam);
        repository.updateTeam(visitingTeam);
        List<Player> playerList = new ArrayList<>();
        playerList.addAll(homeTeam.getPlayers());
        playerList.addAll(visitingTeam.getPlayers());
        for (Player player : playerList) {
            repository.updatePlayer(player);
            repository.updateBattingStats(player.getBattingStats().get(organization.getCurrentYear()));
            repository.updatePitchingStats(player.getPitchingStats().get(organization.getCurrentYear()));
        }
    }

    private boolean homeTeamBattedInNinth() {
        return gamePlayViewModel.getGameSimulator().didHomeTeamBatInNinth();
    }

    private boolean isGameOver() {
        return !(gamePlayViewModel.getGameSimulator().isGameNotOver());
    }

    private void animateRunners() {
        setRunnersOnUI(new Runner[]{null, null, null});
        TreeMap<Integer, Pair<Integer, Boolean>> animationData = gamePlayViewModel.getGameSimulator().getAnimationData();
        if (animationData != null) {

            Pair<Integer, Boolean> runnerOnThirdAnimationData = null;
            if (animationData.containsKey(RUNNER_ON_THIRD_KEY)) {
                runnerOnThirdAnimationData = animationData.get(RUNNER_ON_THIRD_KEY);
            }
            Pair<Integer, Boolean> runnerOnSecondAnimationData = null;
            if (animationData.containsKey(RUNNER_ON_SECOND_KEY)) {
                runnerOnSecondAnimationData = animationData.get(RUNNER_ON_SECOND_KEY);
            }
            Pair<Integer, Boolean> runnerOnFirstAnimationData = null;
            if (animationData.containsKey(RUNNER_ON_FIRST_KEY)) {
                runnerOnFirstAnimationData = animationData.get(RUNNER_ON_FIRST_KEY);
            }
            Pair<Integer, Boolean> batterAnimationData = null;
            if (animationData.containsKey(BATTER_KEY)) {
                batterAnimationData = animationData.get(BATTER_KEY);
            }
            if (runnerOnThirdAnimationData != null) {
                animateRunnerOnThird(runnerOnThirdAnimationData);
            }
            if (runnerOnSecondAnimationData != null) {
                animateRunnerOnSecond(runnerOnSecondAnimationData);
            }
            if (runnerOnFirstAnimationData != null) {
                animateRunnerOnFirst(runnerOnFirstAnimationData);
            }
            if (batterAnimationData != null) {
                animateBatter(batterAnimationData);
            }
        }
    }

    private void animateBatter(Pair<Integer, Boolean> batterAnimationData) {
        if (batterAnimationData.first != null && batterAnimationData.second != null) {
            switch (batterAnimationData.first) {
                case 0:
                    if (!batterAnimationData.second) {
                        AnimatedVectorDrawableCompat out = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_out_at_home);
                        if (out != null) {
                            activityGamePlayBinding.animateBatterIv.setImageDrawable(out);
                            out.start();
                            out.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                @Override
                                public void onAnimationEnd(Drawable drawable) {
                                    super.onAnimationEnd(drawable);
                                    setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                }
                            });
                        }
                    }
                    break;
                case 1:
                    AnimatedVectorDrawableCompat homeToFirst = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_home_to_first);
                    if (homeToFirst != null) {
                        activityGamePlayBinding.animateBatterIv.setImageDrawable(homeToFirst);
                        homeToFirst.start();
                        final boolean safeAtFirst = batterAnimationData.second;
                        homeToFirst.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                if (safeAtFirst) {
                                    AnimatedVectorDrawableCompat safe = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_first);
                                    if (safe != null) {
                                        activityGamePlayBinding.animateBatterIv.setImageDrawable(safe);
                                        safe.start();
                                        safe.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                            @Override
                                            public void onAnimationEnd(Drawable drawable) {
                                                super.onAnimationEnd(drawable);
                                                setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                            }
                                        });
                                    }
                                } else {
                                    AnimatedVectorDrawableCompat out = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_first);
                                    if (out != null) {
                                        activityGamePlayBinding.animateBatterIv.setImageDrawable(out);
                                        out.start();
                                        out.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                            @Override
                                            public void onAnimationEnd(Drawable drawable) {
                                                super.onAnimationEnd(drawable);
                                                setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                            }
                                        });
                                    }
                                }
                            }

                        });
                    }
                    break;
                case 2:
                    homeToFirst = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_home_to_first);
                    if (homeToFirst != null) {
                        activityGamePlayBinding.animateBatterIv.setImageDrawable(homeToFirst);
                        homeToFirst.start();
                        final boolean safeAtSecond = batterAnimationData.second;
                        homeToFirst.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                AnimatedVectorDrawableCompat firstToSecond = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_first_to_second);
                                if (firstToSecond != null) {
                                    activityGamePlayBinding.animateBatterIv.setImageDrawable(firstToSecond);
                                    firstToSecond.start();
                                    firstToSecond.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                        @Override
                                        public void onAnimationEnd(Drawable drawable) {
                                            super.onAnimationEnd(drawable);
                                            if (safeAtSecond) {
                                                AnimatedVectorDrawableCompat safe = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_second);
                                                if (safe != null) {
                                                    activityGamePlayBinding.animateBatterIv.setImageDrawable(safe);
                                                    safe.start();
                                                    safe.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                        @Override
                                                        public void onAnimationEnd(Drawable drawable) {
                                                            super.onAnimationEnd(drawable);
                                                            setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                                        }
                                                    });
                                                }
                                            } else {
                                                AnimatedVectorDrawableCompat out = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_second);
                                                if (out != null) {
                                                    activityGamePlayBinding.animateBatterIv.setImageDrawable(out);
                                                    out.start();
                                                    out.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                        @Override
                                                        public void onAnimationEnd(Drawable drawable) {
                                                            super.onAnimationEnd(drawable);
                                                            setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    break;
                case 3:
                    homeToFirst = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_home_to_first);
                    if (homeToFirst != null) {
                        activityGamePlayBinding.animateBatterIv.setImageDrawable(homeToFirst);
                        homeToFirst.start();
                        final boolean safeAtThird = batterAnimationData.second;
                        homeToFirst.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                AnimatedVectorDrawableCompat firstToSecond = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_first_to_second);
                                if (firstToSecond != null) {
                                    activityGamePlayBinding.animateBatterIv.setImageDrawable(firstToSecond);
                                    firstToSecond.start();
                                    firstToSecond.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                        @Override
                                        public void onAnimationEnd(Drawable drawable) {
                                            super.onAnimationEnd(drawable);
                                            AnimatedVectorDrawableCompat secondToThird = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_second_to_third);
                                            if (secondToThird != null) {
                                                activityGamePlayBinding.animateBatterIv.setImageDrawable(secondToThird);
                                                secondToThird.start();
                                                secondToThird.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                    @Override
                                                    public void onAnimationEnd(Drawable drawable) {
                                                        super.onAnimationEnd(drawable);
                                                        if (safeAtThird) {
                                                            AnimatedVectorDrawableCompat safe = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_third);
                                                            if (safe != null) {
                                                                activityGamePlayBinding.animateBatterIv.setImageDrawable(safe);
                                                                safe.start();
                                                                safe.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                                    @Override
                                                                    public void onAnimationEnd(Drawable drawable) {
                                                                        super.onAnimationEnd(drawable);
                                                                        setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                                                    }
                                                                });
                                                            }
                                                        } else {
                                                            AnimatedVectorDrawableCompat out = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_third);
                                                            if (out != null) {
                                                                activityGamePlayBinding.animateBatterIv.setImageDrawable(out);
                                                                out.start();
                                                                out.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                                    @Override
                                                                    public void onAnimationEnd(Drawable drawable) {
                                                                        super.onAnimationEnd(drawable);
                                                                        setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    break;
                case 4:
                    homeToFirst = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_home_to_first);
                    if (homeToFirst != null) {
                        activityGamePlayBinding.animateBatterIv.setImageDrawable(homeToFirst);
                        homeToFirst.start();
                        final boolean safeAtHome = batterAnimationData.second;
                        homeToFirst.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                AnimatedVectorDrawableCompat firstToSecond = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_first_to_second);
                                if (firstToSecond != null) {
                                    activityGamePlayBinding.animateBatterIv.setImageDrawable(firstToSecond);
                                    firstToSecond.start();
                                    firstToSecond.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                        @Override
                                        public void onAnimationEnd(Drawable drawable) {
                                            super.onAnimationEnd(drawable);
                                            AnimatedVectorDrawableCompat secondToThird = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_second_to_third);
                                            if (secondToThird != null) {
                                                activityGamePlayBinding.animateBatterIv.setImageDrawable(secondToThird);
                                                secondToThird.start();
                                                secondToThird.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                    @Override
                                                    public void onAnimationEnd(Drawable drawable) {
                                                        super.onAnimationEnd(drawable);
                                                        AnimatedVectorDrawableCompat thirdToHome = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_third_to_home);
                                                        if (thirdToHome != null) {
                                                            activityGamePlayBinding.animateBatterIv.setImageDrawable(thirdToHome);
                                                            thirdToHome.start();
                                                            thirdToHome.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                                @Override
                                                                public void onAnimationEnd(Drawable drawable) {
                                                                    super.onAnimationEnd(drawable);
                                                                    if (safeAtHome) {
                                                                        AnimatedVectorDrawableCompat safe = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_home);
                                                                        if (safe != null) {
                                                                            activityGamePlayBinding.animateBatterIv.setImageDrawable(safe);
                                                                            safe.start();
                                                                            safe.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                                                @Override
                                                                                public void onAnimationEnd(Drawable drawable) {
                                                                                    super.onAnimationEnd(drawable);
                                                                                    setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                                                                }
                                                                            });
                                                                        }
                                                                    } else {
                                                                        AnimatedVectorDrawableCompat out = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_home);
                                                                        if (out != null) {
                                                                            activityGamePlayBinding.animateBatterIv.setImageDrawable(out);
                                                                            out.start();
                                                                            out.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                                                @Override
                                                                                public void onAnimationEnd(Drawable drawable) {
                                                                                    super.onAnimationEnd(drawable);
                                                                                    setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    break;
                default:
            }
        }
    }

    private void animateRunnerOnFirst(Pair<Integer, Boolean> runnerOnFirstAnimationData) {
        if (runnerOnFirstAnimationData.first != null && runnerOnFirstAnimationData.second != null) {
            switch (runnerOnFirstAnimationData.first) {
                case 0:
                    setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                    break;
                case 1:
                    AnimatedVectorDrawableCompat firstToSecond = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_first_to_second);
                    if (firstToSecond != null) {
                        activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(firstToSecond);
                        firstToSecond.start();
                        final boolean safeAtSecond = runnerOnFirstAnimationData.second;
                        firstToSecond.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                if (safeAtSecond) {
                                    AnimatedVectorDrawableCompat safe = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_second);
                                    if (safe != null) {
                                        activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(safe);
                                        safe.start();
                                    }
                                } else {
                                    AnimatedVectorDrawableCompat out = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_second);
                                    if (out != null) {
                                        activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(out);
                                        out.start();
                                    }
                                }
                            }

                        });
                    }
                    break;
                case 2:
                    firstToSecond = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_first_to_second);
                    if (firstToSecond != null) {
                        activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(firstToSecond);
                        firstToSecond.start();
                        final boolean safeAtThird = runnerOnFirstAnimationData.second;
                        firstToSecond.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                AnimatedVectorDrawableCompat secondToThird = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_second_to_third);
                                if (secondToThird != null) {
                                    activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(secondToThird);
                                    secondToThird.start();
                                    secondToThird.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                        @Override
                                        public void onAnimationEnd(Drawable drawable) {
                                            super.onAnimationEnd(drawable);
                                            if (safeAtThird) {
                                                AnimatedVectorDrawableCompat safe = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_third);
                                                if (safe != null) {
                                                    activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(safe);
                                                    safe.start();
                                                }
                                            } else {
                                                AnimatedVectorDrawableCompat out = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_third);
                                                if (out != null) {
                                                    activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(out);
                                                    out.start();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    break;
                case 3:
                    firstToSecond = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_first_to_second);
                    if (firstToSecond != null) {
                        activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(firstToSecond);
                        firstToSecond.start();
                        final boolean safeAtHome = runnerOnFirstAnimationData.second;
                        firstToSecond.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                AnimatedVectorDrawableCompat secondToThird = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_second_to_third);
                                if (secondToThird != null) {
                                    activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(secondToThird);
                                    secondToThird.start();
                                    secondToThird.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                        @Override
                                        public void onAnimationEnd(Drawable drawable) {
                                            super.onAnimationEnd(drawable);
                                            AnimatedVectorDrawableCompat thirdToHome = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_third_to_home);
                                            if (thirdToHome != null) {
                                                activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(thirdToHome);
                                                thirdToHome.start();
                                                thirdToHome.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                    @Override
                                                    public void onAnimationEnd(Drawable drawable) {
                                                        super.onAnimationEnd(drawable);
                                                        if (safeAtHome) {
                                                            AnimatedVectorDrawableCompat safe = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_home);
                                                            if (safe != null) {
                                                                activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(safe);
                                                                safe.start();
                                                            }
                                                        } else {
                                                            AnimatedVectorDrawableCompat out = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_home);
                                                            if (out != null) {
                                                                activityGamePlayBinding.animateRunnerOnFirstIv.setImageDrawable(out);
                                                                out.start();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    break;
                default:
            }
        }
    }

    private void animateRunnerOnThird(Pair<Integer, Boolean> runnerOnThirdAnimationData) {
        if (runnerOnThirdAnimationData.first != null) {
            if (runnerOnThirdAnimationData.first == 1) {
                activityGamePlayBinding.animateRunnerOnThirdIv.setImageDrawable(AnimatedVectorDrawableCompat.create(this, R.drawable.animate_third_to_home));
                AnimatedVectorDrawableCompat thirdToHome = (AnimatedVectorDrawableCompat) activityGamePlayBinding.animateRunnerOnThirdIv.getDrawable();
                thirdToHome.start();
                if (runnerOnThirdAnimationData.second != null) {
                    final boolean safeAtHome = runnerOnThirdAnimationData.second;
                    thirdToHome.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            super.onAnimationEnd(drawable);
                            if (safeAtHome) {
                                AnimatedVectorDrawableCompat safeAtHome = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_home);
                                if (safeAtHome != null) {
                                    activityGamePlayBinding.animateRunnerOnThirdIv.setImageDrawable(safeAtHome);
                                    safeAtHome.start();
                                }
                            } else {
                                AnimatedVectorDrawableCompat outAtHome = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_home);
                                if (outAtHome != null) {
                                    activityGamePlayBinding.animateRunnerOnThirdIv.setImageDrawable(outAtHome);
                                    outAtHome.start();
                                }
                            }
                        }

                    });
                }
            } else {
                setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
            }
        }
    }

    private void animateRunnerOnSecond(Pair<Integer, Boolean> runnerOnSecondAnimationData) {
        if (runnerOnSecondAnimationData.first != null && runnerOnSecondAnimationData.second != null) {
            switch (runnerOnSecondAnimationData.first) {
                case 0:
                    setRunnersOnUI(gamePlayViewModel.getGameSimulator().getRunners());
                    break;
                case 1:
                    AnimatedVectorDrawableCompat secondToThird = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_second_to_third);
                    if (secondToThird != null) {
                        activityGamePlayBinding.animateRunnerOnSecondIv.setImageDrawable(secondToThird);
                        secondToThird.start();
                        final boolean safeAtThird = runnerOnSecondAnimationData.second;
                        secondToThird.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                if (safeAtThird) {
                                    AnimatedVectorDrawableCompat safeAtThird = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_third);
                                    if (safeAtThird != null) {
                                        activityGamePlayBinding.animateRunnerOnSecondIv.setImageDrawable(safeAtThird);
                                        safeAtThird.start();
                                    }
                                } else {
                                    AnimatedVectorDrawableCompat outAtThird = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_third);
                                    if (outAtThird != null) {
                                        activityGamePlayBinding.animateRunnerOnSecondIv.setImageDrawable(outAtThird);
                                        outAtThird.start();
                                    }
                                }
                            }

                        });
                    }
                    break;
                case 2:
                    secondToThird = AnimatedVectorDrawableCompat.create(this, R.drawable.animate_second_to_third);
                    if (secondToThird != null) {
                        activityGamePlayBinding.animateRunnerOnSecondIv.setImageDrawable(secondToThird);
                        secondToThird.start();
                        final boolean safeAtHome = runnerOnSecondAnimationData.second;
                        secondToThird.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                AnimatedVectorDrawableCompat thirdToHome = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_third_to_home);
                                if (thirdToHome != null) {
                                    activityGamePlayBinding.animateRunnerOnSecondIv.setImageDrawable(thirdToHome);
                                    thirdToHome.start();
                                    thirdToHome.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                        @Override
                                        public void onAnimationEnd(Drawable drawable) {
                                            super.onAnimationEnd(drawable);
                                            if (safeAtHome) {
                                                AnimatedVectorDrawableCompat safeAtHome = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_safe_at_home);
                                                if (safeAtHome != null) {
                                                    activityGamePlayBinding.animateRunnerOnSecondIv.setImageDrawable(safeAtHome);
                                                    safeAtHome.start();
                                                }
                                            } else {
                                                AnimatedVectorDrawableCompat outAtHome = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animate_out_at_home);
                                                if (outAtHome != null) {
                                                    activityGamePlayBinding.animateRunnerOnSecondIv.setImageDrawable(outAtHome);
                                                    outAtHome.start();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    break;
                default:
            }
        }
    }

    private void setRunnersOnUI(Runner[] runners) {
        int gameState = gamePlayViewModel.getGameSimulator().getGameState();
        if (gameState >= 17) {
            gameState -= 16;
        } else if (gameState >= 9) {
            gameState -= 8;
        }
        switch (gameState) {
            case 2:
                activityGamePlayBinding.baserunnersIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseball_game_state_2));
                activityGamePlayBinding.baserunnersIv.setVisibility(View.VISIBLE);
                break;
            case 3:
                activityGamePlayBinding.baserunnersIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseball_game_state_3));
                activityGamePlayBinding.baserunnersIv.setVisibility(View.VISIBLE);
                break;
            case 4:
                activityGamePlayBinding.baserunnersIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseball_game_state_4));
                activityGamePlayBinding.baserunnersIv.setVisibility(View.VISIBLE);
                break;
            case 5:
                activityGamePlayBinding.baserunnersIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseball_game_state_5));
                activityGamePlayBinding.baserunnersIv.setVisibility(View.VISIBLE);
                break;
            case 6:
                activityGamePlayBinding.baserunnersIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseball_game_state_6));
                activityGamePlayBinding.baserunnersIv.setVisibility(View.VISIBLE);
                break;
            case 7:
                activityGamePlayBinding.baserunnersIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseball_game_state_7));
                activityGamePlayBinding.baserunnersIv.setVisibility(View.VISIBLE);
                break;
            case 8:
                activityGamePlayBinding.baserunnersIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseball_game_state_8));
                activityGamePlayBinding.baserunnersIv.setVisibility(View.VISIBLE);
                break;
            default:
                activityGamePlayBinding.baserunnersIv.setVisibility(View.INVISIBLE);
                activityGamePlayBinding.runnerOnFirstTv.setVisibility(View.GONE);
                activityGamePlayBinding.runnerOnSecondTv.setVisibility(View.GONE);
                activityGamePlayBinding.runnerOnThirdTv.setVisibility(View.GONE);
        }
        if (runners != null && gameState != 1 && gameState != 9) {
            if (runners.length == 3) {
                if (runners[0] != null) {
                    activityGamePlayBinding.runnerOnFirstTv.setVisibility(View.VISIBLE);
                    activityGamePlayBinding.runnerOnFirstTv.setText(runners[0].getRunner().getLastName());
                } else {
                    activityGamePlayBinding.runnerOnFirstTv.setVisibility(View.GONE);
                }
                if (runners[1] != null) {
                    activityGamePlayBinding.runnerOnSecondTv.setVisibility(View.VISIBLE);
                    activityGamePlayBinding.runnerOnSecondTv.setText(runners[1].getRunner().getLastName());
                } else {
                    activityGamePlayBinding.runnerOnSecondTv.setVisibility(View.GONE);
                }
                if (runners[2] != null) {
                    activityGamePlayBinding.runnerOnThirdTv.setVisibility(View.VISIBLE);
                    activityGamePlayBinding.runnerOnThirdTv.setText(runners[2].getRunner().getLastName());
                } else {
                    activityGamePlayBinding.runnerOnThirdTv.setVisibility(View.GONE);
                }
            }
        }
    }


    private void updateHomeHits() {
        List<BattingLine> battingLineList = game.getHomeBoxScore().getBattingLines();
        int hits = 0;
        for (BattingLine battingLine : battingLineList) {
            hits += battingLine.getHits();
        }
        activityGamePlayBinding.scoreboardHitsHomeTv.setText(String.format(Locale.getDefault(), "%d", hits));
    }

    private void updateVisitorHits() {
        List<BattingLine> battingLineList = game.getVisitorBoxScore().getBattingLines();
        int hits = 0;
        for (BattingLine battingLine : battingLineList) {
            hits += battingLine.getHits();
        }
        activityGamePlayBinding.scoreboardHitsVisitorTv.setText(String.format(Locale.getDefault(), "%d", hits));
    }

    private void displayCurrentBatter(Player currentBatter) {
        DecimalFormat decimalFormat = new DecimalFormat(".000");
        String batterInfo = String.format(Locale.getDefault(), getString(R.string.display_current_batter_format), currentBatter.getName() , decimalFormat.format(currentBatter.getBattingStatsForYear(organization.getCurrentYear()).getAverage())
                , decimalFormat.format(currentBatter.getBattingStatsForYear(organization.getCurrentYear()).getOnBasePct()));
        boolean batterIsTired = gamePlayViewModel.getGameSimulator().getBatterStaminaAdjustment() > 0;
        if (gamePlayViewModel.getGameSimulator().isVisitorHitting()) {
            activityGamePlayBinding.gameScreenVisitorPlayerTv.setText(batterInfo);
            if (batterIsTired) {
                activityGamePlayBinding.gameScreenVisitorPlayerTv.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else {
                activityGamePlayBinding.gameScreenVisitorPlayerTv.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }
        } else {
            activityGamePlayBinding.gameScreenHomePlayerTv.setText(batterInfo);
            if (batterIsTired) {
                activityGamePlayBinding.gameScreenHomePlayerTv.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else {
                activityGamePlayBinding.gameScreenHomePlayerTv.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }
        }

    }

    private void displayCurrentPitcher(Player currentPitcher) {
        String pitcherInfo = String.format(Locale.getDefault(), getString(R.string.display_current_pitcher_format), currentPitcher.getName(), currentPitcher.getPitchingStatsForYear(organization.getCurrentYear()).getERA(), currentPitcher.getPitchingStatsForYear(organization.getCurrentYear()).getWHIP());
        boolean pitcherIsTired = gamePlayViewModel.getGameSimulator().getPitcherStaminaAdjustment() > 0;
        if (gamePlayViewModel.getGameSimulator().isVisitorHitting()) {
            activityGamePlayBinding.gameScreenHomePlayerTv.setText(pitcherInfo);
            if (pitcherIsTired) {
                activityGamePlayBinding.gameScreenVisitorPlayerTv.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else {
                activityGamePlayBinding.gameScreenVisitorPlayerTv.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }
        } else {
            activityGamePlayBinding.gameScreenVisitorPlayerTv.setText(pitcherInfo);
            if (pitcherIsTired) {
                activityGamePlayBinding.gameScreenHomePlayerTv.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else {
                activityGamePlayBinding.gameScreenHomePlayerTv.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.throw_pitch_fab) {
            Toast.makeText(this, R.string.throw_pitch_fab_toast_message, Toast.LENGTH_SHORT).show();
        }
    }
}