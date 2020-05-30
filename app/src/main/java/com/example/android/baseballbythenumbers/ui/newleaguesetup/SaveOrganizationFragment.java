package com.example.android.baseballbythenumbers.ui.newleaguesetup;

import android.app.Application;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.data.BattingStats;
import com.example.android.baseballbythenumbers.data.Division;
import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.data.League;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.PitchingStats;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Schedule;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.repository.Repository;
import com.example.android.baseballbythenumbers.databinding.FragmentSaveOrganizationBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrgSaveEndSignal} interface
 * to handle interaction events.
 * Use the {@link SaveOrganizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveOrganizationFragment extends Fragment {

    private static Organization mOrganization;

    private static Application mApplication;

    private Repository repository;

    private OrgSaveEndSignal mListener;

    private FragmentSaveOrganizationBinding saveOrganizationBinding;

    public SaveOrganizationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param newOrganization the organization to be saved in room
     * @return A new instance of fragment SaveOrganizationFragment.
     */
    
    public static SaveOrganizationFragment newInstance(Application application, Organization newOrganization) {
        SaveOrganizationFragment fragment = new SaveOrganizationFragment();
        mOrganization = newOrganization;
        mApplication = application;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        saveOrganizationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_save_organization, container, false);
        return saveOrganizationBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                repository = ((BaseballByTheNumbersApp) mApplication).getRepository();
                repository.insertOrganization(mOrganization);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText(R.string.organization_saving_leagues_label);
                    }
                });
                List<League> leagueList = mOrganization.getLeagues();
                repository.insertAllLeagues(mOrganization.getLeagues());
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText(R.string.organization_saving_divisions_label);
                    }
                });
                List<Division> divisionList = new ArrayList<>();
                for (League league: leagueList) {
                    divisionList.addAll(league.getDivisions());
                }
                repository.insertAllDivisions(divisionList);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText(R.string.organizations_saving_teams_label);
                    }
                });
                List<Team> teamList = new ArrayList<>();
                for (Division division: divisionList) {
                    teamList.addAll(division.getTeams());
                }
                repository.insertAllTeams(teamList);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText(R.string.organization_saving_players_label);
                    }
                });
                List<Player> playerList = new ArrayList<>();
                for (Team team : teamList) {
                    playerList.addAll(team.getPlayers());
                }
                repository.insertAllPlayers(playerList);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText(R.string.organization_saving_player_stats_label);
                    }
                });
                List<BattingStats> battingStatsList = new ArrayList<>();
                List<PitchingStats> pitchingStatsList = new ArrayList<>();
                for (Player player : playerList) {
                    battingStatsList.addAll(player.getBattingStats());
                    pitchingStatsList.addAll(player.getPitchingStats());
                }
                repository.insertAllBattingStats(battingStatsList);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                repository.insertAllPitchingStats(pitchingStatsList);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText(R.string.organization_saving_schedule_label);
                    }
                });
                List<Schedule> scheduleList = mOrganization.getSchedules();
                repository.insertAllSchedules(scheduleList);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                List<Game> gameList = new ArrayList<>();
                for(Schedule schedule : scheduleList) {
                    gameList.addAll(schedule.getGameList());
                }
                repository.insertAllGames(gameList);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(20);
                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText(R.string.organization_finished_saving_label);
                        onOrganizationCreationEnd(mOrganization);
                    }
                });
            }
        }).start();



    }


    public void onOrganizationCreationEnd(Organization newOrganization) {
        if (mListener != null) {
            mListener.onOrganizationSaveEndSignal(newOrganization);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrgSaveEndSignal) {
            mListener = (OrgSaveEndSignal) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OrgSaveEndSignal");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OrgSaveEndSignal {
        void onOrganizationSaveEndSignal(Organization newOrganization);
    }
}
