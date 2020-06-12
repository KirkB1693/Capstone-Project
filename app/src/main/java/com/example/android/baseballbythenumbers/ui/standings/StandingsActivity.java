package com.example.android.baseballbythenumbers.ui.standings;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.baseballbythenumbers.AppExecutors;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Division;
import com.example.android.baseballbythenumbers.data.League;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.databinding.ActivityStandingsBinding;
import com.example.android.baseballbythenumbers.ui.main.MainActivity;
import com.example.android.baseballbythenumbers.viewModels.StandingsViewModel;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StandingsActivity extends AppCompatActivity {

private ActivityStandingsBinding activityStandingsBinding;
private Organization organization;
private StandingsViewModel standingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_standings);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        activityStandingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_standings);
        standingsViewModel = new ViewModelProvider(this).get(StandingsViewModel.class);
        setSupportActionBar(activityStandingsBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Standings");

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                organization = extras.getParcelable(MainActivity.ORGANIZATION_EXTRA);
                updateUI();
            }
        }
    }


    private void updateUI() {
        int leagueNumber = 0;
        for (League league: organization.getLeagues()) {
            leagueNumber++;
            switch (leagueNumber) {
                case 1:
                    activityStandingsBinding.include.league1TV.setText(league.getLeagueName());
                    setDivisionNames(leagueNumber, league);
                    break;
                case 2:
                    activityStandingsBinding.include.league2TV.setVisibility(View.VISIBLE);
                    activityStandingsBinding.include.league2TV.setText(league.getLeagueName());
                    setDivisionNames(leagueNumber, league);
                    break;
                case 3:
                    activityStandingsBinding.include.league3TV.setVisibility(View.VISIBLE);
                    activityStandingsBinding.include.league3TV.setText(league.getLeagueName());
                    setDivisionNames(leagueNumber, league);
                    break;
                default:
                    break;
            }
        }
    }

    private void setDivisionNames(final int leagueNumber, League league) {
        int divisionNumber = 0;
        final int totalLeagues = organization.getLeagues().size();
        final int totalDivisions = league.getDivisions().size();
        for (final Division division: league.getDivisions()) {
            divisionNumber++;
            switch (leagueNumber) {
                case 1:
                    switch (divisionNumber) {
                        case 1:
                            activityStandingsBinding.include.league1division1TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league1division1TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league1division1Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league1division1Table);
                                            activityStandingsBinding.include.standingsProgressBar.setVisibility(View.GONE);
                                            if (totalLeagues == leagueNumber && totalDivisions == 1) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 2:
                            activityStandingsBinding.include.league1division2TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league1division2TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league1division2Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league1division2Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 2) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 3:
                            activityStandingsBinding.include.league1division3TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league1division3TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league1division3Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league1division3Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 3) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 4:
                            activityStandingsBinding.include.league1division4TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league1division4TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league1division4Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league1division4Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 4) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        default:
                    }
                    break;
                case 2:
                    switch (divisionNumber) {
                        case 1:
                            activityStandingsBinding.include.league2division1TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league2division1TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league2division1Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league2division1Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 1) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 2:
                            activityStandingsBinding.include.league2division2TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league2division2TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league2division2Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league2division2Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 2) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 3:
                            activityStandingsBinding.include.league2division3TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league2division3TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league2division3Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league2division3Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 3) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 4:
                            activityStandingsBinding.include.league2division4TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league2division4TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league2division4Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league2division4Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 4) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        default:
                    }
                    break;
                case 3:
                    switch (divisionNumber) {
                        case 1:
                            activityStandingsBinding.include.league3division1TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league3division1TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league3division1Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league3division1Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 1) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 2:
                            activityStandingsBinding.include.league3division2TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league3division2TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league3division2Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league3division2Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 2) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 3:
                            activityStandingsBinding.include.league3division3TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league3division3TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league3division3Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league3division3Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 3) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 4:
                            activityStandingsBinding.include.league3division4TV.setVisibility(View.VISIBLE);
                            activityStandingsBinding.include.league3division4TV.setText(division.getDivisionName());
                            activityStandingsBinding.include.league3division4Table.setVisibility(View.VISIBLE);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Team> teams = standingsViewModel.getTeamsFromDivision(division.getDivisionId());
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTeamsInTable(teams, activityStandingsBinding.include.league3division4Table);
                                            if (totalLeagues == leagueNumber && totalDivisions == 5) {
                                                setupWindowAnimations();
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        default:
                    }
                    break;
                default:
            }
        }

    }

    private void setTeamsInTable(List<Team> teams, TableLayout tableLayout) {
        if (teams != null) {
            Collections.sort(teams, Team.WonLossRecordComparator);
            tableLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                TableLayout labelsRow = (TableLayout) inflater.inflate(R.layout.standings_division_table_labels, null);
                tableLayout.addView(labelsRow);
                boolean changeBackground = true;
                for (Team team : teams) {
                    changeBackground = !changeBackground;
                    TableLayout teamRow = (TableLayout) inflater.inflate(R.layout.standings_table_empty_row, null);
                    if (changeBackground) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            teamRow.setBackgroundColor(getColor(R.color.light_background));
                        }
                    }
                    TextView teamNameTV = teamRow.findViewById(R.id.standings_empty_team_name_tv);
                    teamNameTV.setText(String.format("%s %s", team.getTeamCity(), team.getTeamName()));
                    TextView teamRecordTV = teamRow.findViewById(R.id.standings_empty_record_tv);
                    String teamRecord = String.format(Locale.getDefault(), "%d - %d", team.getWins(), team.getLosses());
                    teamRecordTV.setText(teamRecord);
                    TextView teamWLPctTV = teamRow.findViewById(R.id.standings_empty_win_pct_tv);
                    DecimalFormat decimalFormat = new DecimalFormat(".000");
                    double winPct;
                    if (team.getWins() == 0) {
                        winPct = 0.0;
                    } else {
                        winPct = (team.getWins() * 1.0) / (team.getWins() + team.getLosses());
                    }
                    String winPctString = decimalFormat.format(winPct);
                    teamWLPctTV.setText(winPctString);
                    tableLayout.addView(teamRow);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tableLayout.setTransitionGroup(true);
                }
                tableLayout.setStretchAllColumns(true);
            }
        }
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_up);
            getWindow().setEnterTransition(transition);
            getWindow().setExitTransition(new Fade(Fade.OUT));
            startPostponedEnterTransition();
        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            Intent standingsIntent = new Intent(StandingsActivity.this, MainActivity.class);
            startActivity(standingsIntent, options.toBundle());
            finishAfterTransition();
        } else {
            Intent standingsIntent = new Intent(this, MainActivity.class);
            this.startActivity(standingsIntent);
            finish();
        }
    }
}
