package com.example.android.baseballbythenumbers.UI.StandingsActivity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.AppExecutors;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.UI.MainActivity.MainActivity;
import com.example.android.baseballbythenumbers.ViewModels.StandingsViewModel;
import com.example.android.baseballbythenumbers.databinding.ActivityStandingsBinding;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class StandingsActivity extends AppCompatActivity {

private ActivityStandingsBinding activityStandingsBinding;
private Organization organization;
private StandingsViewModel standingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);
        activityStandingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_standings);
        standingsViewModel = ViewModelProviders.of(this).get(StandingsViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    private void setDivisionNames(int leagueNumber, League league) {
        int divisionNumber = 0;
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league1division1Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league1division2Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league1division3Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league1division4Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league2division1Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league2division2Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league2division3Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league2division4Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league3division1Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league3division2Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league3division3Table);
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
                                            setTeamsInTable(teams, (TableLayout) activityStandingsBinding.include.league3division4Table);
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
            TableLayout labelsRow = (TableLayout) inflater.inflate(R.layout.standings_division_table_labels, null);
            tableLayout.addView(labelsRow);
            for (Team team: teams) {
                TableLayout teamRow = (TableLayout) inflater.inflate(R.layout.standings_table_empty_row, null);
                TextView teamNameTV = teamRow.findViewById(R.id.standings_empty_team_name_tv);
                teamNameTV.setText(team.getTeamCity() + " " +team.getTeamName());
                TextView teamRecordTV = teamRow.findViewById(R.id.standings_empty_record_tv);
                String teamRecord = Integer.toString(team.getWins()) + " - " + Integer.toString(team.getLosses());
                teamRecordTV.setText(teamRecord);
                TextView teamWLPctTV = teamRow.findViewById(R.id.standings_empty_win_pct_tv);
                DecimalFormat decimalFormat = new DecimalFormat(".000");
                double winPct;
                if (team.getWins() == 0) {
                    winPct = 0.0;
                } else {
                    winPct = (team.getWins() * 1.0)/(team.getWins() + team.getLosses());
                }
                String winPctString = decimalFormat.format(winPct);
                teamWLPctTV.setText(winPctString);
                tableLayout.addView(teamRow);
            }
            tableLayout.setStretchAllColumns(true);
        }
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}