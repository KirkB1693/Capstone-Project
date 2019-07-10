package com.example.android.baseballbythenumbers.UI.NewLeagueSetupActivity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.Data.BattingStats;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.Repository.Repository;
import com.example.android.baseballbythenumbers.databinding.FragmentSaveOrganizationBinding;

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

    private static Organization organization;

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
     * @param newOrganization
     * @return A new instance of fragment SaveOrganizationFragment.
     */
    
    public static SaveOrganizationFragment newInstance(Organization newOrganization) {
        SaveOrganizationFragment fragment = new SaveOrganizationFragment();
        organization = newOrganization;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                repository = new Repository(getActivity().getApplication());
                repository.insertOrganization(organization);
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText("Saving Leagues...");
                    }
                });
                List<League> leagueList = organization.getLeagues();
                repository.insertAllLeagues(organization.getLeagues());
                saveOrganizationBinding.generateOrgProgressbar.incrementProgressBy(10);

                saveOrganizationBinding.generateOrgProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        saveOrganizationBinding.orgSaveTv.setText("Saving Divisions...");
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
                        saveOrganizationBinding.orgSaveTv.setText("Saving Teams...");
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
                        saveOrganizationBinding.orgSaveTv.setText("Saving Players...");
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
                        saveOrganizationBinding.orgSaveTv.setText("Saving Player Stats...");
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
                        saveOrganizationBinding.orgSaveTv.setText("Saving Schedule...");
                    }
                });
                List<Schedule> scheduleList = organization.getSchedules();
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
                        saveOrganizationBinding.orgSaveTv.setText("Finished Saving!");
                        onOrganizationCreationEnd(organization);
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
