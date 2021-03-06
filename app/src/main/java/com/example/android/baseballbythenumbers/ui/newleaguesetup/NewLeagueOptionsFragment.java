package com.example.android.baseballbythenumbers.ui.newleaguesetup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.Schedule;
import com.example.android.baseballbythenumbers.databinding.FragmentNewLeagueOptionsBinding;
import com.example.android.baseballbythenumbers.generators.OrganizationGenerator;
import com.example.android.baseballbythenumbers.generators.ScheduleGenerator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewLeagueOptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NewLeagueOptionsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    public static final int NUMBER_OF_PLAYERS_ON_ROSTER = 26;
    private static final String ARG_COUNTRIES = "countries";
    private static final String ARG_USER_CITY = "user_city";
    private static final String ARG_USER_NAME = "user_name";

    private int countries;
    private String userCity;
    private String mUserName;
    private String mTeamName;
    private int mNumOfLeagues;
    private int mNumOfDivisions;
    private int mNumOfTeamsInDivision;
    private int mNumOfGamesInSeries;
    private List<String> leagueNames;
    private List<Boolean> leagueUsesDh;
    private boolean interleaguePlay;
    private Organization newOrganization;

    private Spinner leagueSpinner;
    private Spinner divisionSpinner;
    private Spinner teamsSpinner;
    private Spinner gamesInSeriesSpinner;

    private FragmentNewLeagueOptionsBinding newLeagueOptionsBinding;

    private OnFragmentInteractionListener mListener;

    private String userName;

    public NewLeagueOptionsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param countries a number representing the countries to pick cities from
     * @param userCity the name of the users city
     * @return A new instance of fragment SaveOrganizationFragment.
     */

    public static NewLeagueOptionsFragment newInstance(int countries, String userCity, String userName) {
        NewLeagueOptionsFragment fragment = new NewLeagueOptionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNTRIES, countries);
        args.putString(ARG_USER_CITY, userCity);
        args.putString(ARG_USER_NAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            countries = getArguments().getInt(ARG_COUNTRIES);
            userCity = getArguments().getString(ARG_USER_CITY);
            userName = getArguments().getString(ARG_USER_NAME);
        }
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newLeagueOptionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_league_options, container, false);
        return newLeagueOptionsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newLeagueOptionsBinding.startSeasonButton.setOnClickListener(this);
        leagueSpinner = newLeagueOptionsBinding.numberOfLeaguesSpinner;
        leagueSpinner.setOnItemSelectedListener(this);
        divisionSpinner = newLeagueOptionsBinding.numberOfDivisionsSpinner;
        teamsSpinner = newLeagueOptionsBinding.teamsPerDivisionSpinner;
        gamesInSeriesSpinner = newLeagueOptionsBinding.gamesInSeriesSpinner;
        leagueNames = new ArrayList<>();
        leagueUsesDh = new ArrayList<>();
        if (!userName.isEmpty()) {
            newLeagueOptionsBinding.userNameEt.setText(userName);
        }
    }

    public void onStartSeasonButtonPressed(View view) {
        if (mListener != null && inputsAreValid()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int numOfItemsToBeGenerated = mNumOfLeagues * mNumOfDivisions * mNumOfTeamsInDivision * NUMBER_OF_PLAYERS_ON_ROSTER;
                        newLeagueOptionsBinding.newLeagueProgressbar.setProgress(0);int gamesToGenerate;
                        if (interleaguePlay) {
                            gamesToGenerate = ((mNumOfLeagues * mNumOfDivisions * mNumOfTeamsInDivision) - 1) * 2 * mNumOfGamesInSeries *
                                    ((mNumOfLeagues * mNumOfDivisions * mNumOfTeamsInDivision) / 2);  // Play every other team (n-1)  twice (home and away)
                        } else {
                            gamesToGenerate = ((mNumOfDivisions * mNumOfTeamsInDivision) - 1) * 2 * mNumOfGamesInSeries * mNumOfLeagues * ((mNumOfDivisions * mNumOfTeamsInDivision) / 2);
                        }
                        newLeagueOptionsBinding.newLeagueProgressbar.setProgress(0);
                        newLeagueOptionsBinding.newLeagueProgressbar.setMax(numOfItemsToBeGenerated + gamesToGenerate);
                        newOrganization = createTheNewOrganization();
                        newLeagueOptionsBinding.newLeagueProgressbar.post(new Runnable() {
                            @Override
                            public void run() {
                                newLeagueOptionsBinding.newLeagueTv.setText(R.string.creating_schedule_label);
                            }
                        });
                        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(newOrganization);
                        List<Schedule> scheduleList = new ArrayList<>();
                        scheduleList.add(scheduleGenerator.generateSchedule(newLeagueOptionsBinding.newLeagueProgressbar));
                        newOrganization.setSchedules(scheduleList);
                        newLeagueOptionsBinding.newLeagueProgressbar.post(new Runnable() {
                            @Override
                            public void run() {
                                newLeagueOptionsBinding.newLeagueTv.setText(R.string.finished_creating_schedule_label);
                                mListener.onStartSeasonFragmentInteraction(newOrganization);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private Organization createTheNewOrganization() {
        OrganizationGenerator organizationGenerator = new OrganizationGenerator(getContext());
        return organizationGenerator.generateOrganization(mUserName, 0, interleaguePlay, mNumOfGamesInSeries, mNumOfLeagues, leagueNames, leagueUsesDh, mNumOfTeamsInDivision,
                mNumOfDivisions, countries, null, mTeamName, userCity, newLeagueOptionsBinding.newLeagueProgressbar);
    }

    private void showProgressBar() {
        newLeagueOptionsBinding.interleaguePlayCheckBox.setVisibility(View.GONE);
        newLeagueOptionsBinding.useDhCheckbox3.setVisibility(View.GONE);
        newLeagueOptionsBinding.useDhCheckbox.setVisibility(View.GONE);
        newLeagueOptionsBinding.useDhCheckbox2.setVisibility(View.GONE);
        newLeagueOptionsBinding.league3NameEt.setVisibility(View.GONE);
        newLeagueOptionsBinding.league2NameEt.setVisibility(View.GONE);
        newLeagueOptionsBinding.league1NameEt.setVisibility(View.GONE);
        newLeagueOptionsBinding.teamNameEt.setVisibility(View.GONE);
        newLeagueOptionsBinding.userNameEt.setVisibility(View.GONE);
        newLeagueOptionsBinding.gamesInSeriesSpinner.setVisibility(View.GONE);
        newLeagueOptionsBinding.numberOfDivisionsSpinner.setVisibility(View.GONE);
        newLeagueOptionsBinding.numberOfLeaguesSpinner.setVisibility(View.GONE);
        newLeagueOptionsBinding.teamsPerDivisionSpinner.setVisibility(View.GONE);
        newLeagueOptionsBinding.startSeasonButton.setVisibility(View.GONE);

        newLeagueOptionsBinding.newLeagueTv.setVisibility(View.VISIBLE);
        newLeagueOptionsBinding.newLeagueProgressbar.setVisibility(View.VISIBLE);
    }

    private boolean inputsAreValid() {
        return (isUserNameValid() && isTeamNameValid() && areLeaguesSelectedValid() && isDivisionSelectionValid() && isTeamSelectionValid() && isGamesInSeriesSelectionValid());
    }

    private boolean isGamesInSeriesSelectionValid() {
        if (gamesInSeriesSpinner.getSelectedItemPosition() > 0) {
            mNumOfGamesInSeries = Integer.parseInt((String) gamesInSeriesSpinner.getSelectedItem());
            return true;
        }
        Toast.makeText(getContext(), R.string.new_league_setup_error_toast_select_games_in_series, Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean isTeamSelectionValid() {
        if (teamsSpinner.getSelectedItemPosition() > 0) {
            mNumOfTeamsInDivision = Integer.parseInt((String) teamsSpinner.getSelectedItem());
            return true;
        }
        Toast.makeText(getContext(), R.string.new_league_setup_error_toast_select_number_of_teams_per_division, Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean isDivisionSelectionValid() {
        if (divisionSpinner.getSelectedItemPosition() > 0) {
            mNumOfDivisions = Integer.parseInt((String) divisionSpinner.getSelectedItem());
            return true;
        }
        Toast.makeText(getContext(), R.string.new_league_setup_error_toast_select_number_of_divisions, Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean areLeaguesSelectedValid() {
        int position = leagueSpinner.getSelectedItemPosition();
        switch (position) {
            case 0:
                Toast.makeText(getContext(), R.string.new_league_setup_select_number_of_leagues_error_toast, Toast.LENGTH_SHORT).show();
                return false;
            case 1:
                if (newLeagueOptionsBinding.league1NameEt.getText().length() > 0) {
                    leagueNames.clear();
                    leagueUsesDh.clear();
                    leagueNames.add(String.valueOf(newLeagueOptionsBinding.league1NameEt.getText()));
                    leagueUsesDh.add(newLeagueOptionsBinding.useDhCheckbox.isChecked());
                    interleaguePlay = false;
                    return true;
                }
                Toast.makeText(getContext(), R.string.new_league_setup_enter_league_name_error_toast, Toast.LENGTH_SHORT).show();
                return false;
            case 2:
                if (newLeagueOptionsBinding.league1NameEt.getText().length() > 0 && newLeagueOptionsBinding.league2NameEt.getText().length() > 0) {
                    leagueNames.clear();
                    leagueUsesDh.clear();
                    leagueNames.add(String.valueOf(newLeagueOptionsBinding.league1NameEt.getText()));
                    leagueNames.add(String.valueOf(newLeagueOptionsBinding.league2NameEt.getText()));
                    leagueUsesDh.add(newLeagueOptionsBinding.useDhCheckbox.isChecked());
                    leagueUsesDh.add(newLeagueOptionsBinding.useDhCheckbox2.isChecked());
                    interleaguePlay = newLeagueOptionsBinding.interleaguePlayCheckBox.isChecked();
                    return true;
                }
                Toast.makeText(getContext(), R.string.new_league_setup_enter_all_league_names_error_toast, Toast.LENGTH_SHORT).show();
                return false;
            case 3:
                if (newLeagueOptionsBinding.league1NameEt.getText().length() > 0 && newLeagueOptionsBinding.league2NameEt.getText().length() > 0 &&
                        newLeagueOptionsBinding.league3NameEt.getText().length() > 0) {
                    leagueNames.clear();
                    leagueUsesDh.clear();
                    leagueNames.add(String.valueOf(newLeagueOptionsBinding.league1NameEt.getText()));
                    leagueNames.add(String.valueOf(newLeagueOptionsBinding.league2NameEt.getText()));
                    leagueNames.add(String.valueOf(newLeagueOptionsBinding.league3NameEt.getText()));
                    leagueUsesDh.add(newLeagueOptionsBinding.useDhCheckbox.isChecked());
                    leagueUsesDh.add(newLeagueOptionsBinding.useDhCheckbox2.isChecked());
                    leagueUsesDh.add(newLeagueOptionsBinding.useDhCheckbox3.isChecked());
                    interleaguePlay = newLeagueOptionsBinding.interleaguePlayCheckBox.isChecked();
                    return true;
                }
                Toast.makeText(getContext(),  R.string.new_league_setup_enter_all_league_names_error_toast, Toast.LENGTH_SHORT).show();
                return false;
            default:
                return false;
        }
    }

    private boolean isUserNameValid() {
        mUserName = String.valueOf(newLeagueOptionsBinding.userNameEt.getText());
        boolean validUserName = (mUserName.length() > 0);
        if (!validUserName) {
            Toast.makeText(getContext(), R.string.new_league_setup_error_toast_enter_user_name, Toast.LENGTH_SHORT).show();
        }
        return validUserName;
    }

    private boolean isTeamNameValid() {
        mTeamName = String.valueOf(newLeagueOptionsBinding.teamNameEt.getText());
        boolean validTeamName = (mTeamName.length() > 0);
        if (!validTeamName) {
            Toast.makeText(getContext(), R.string.new_league_setup_error_toast_enter_team_name, Toast.LENGTH_SHORT).show();
        }
        return validTeamName;
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_season_button) {
            showProgressBar();
            onStartSeasonButtonPressed(view);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        setVisibilityOfLeagueNames(position);
        mNumOfLeagues = position;
    }

    private void setVisibilityOfLeagueNames(int position) {
        switch (position) {
            case 0:
                newLeagueOptionsBinding.league1NameEt.setVisibility(View.GONE);
                newLeagueOptionsBinding.useDhCheckbox.setVisibility(View.GONE);
                newLeagueOptionsBinding.league2NameEt.setVisibility(View.GONE);
                newLeagueOptionsBinding.useDhCheckbox2.setVisibility(View.GONE);
                newLeagueOptionsBinding.league3NameEt.setVisibility(View.GONE);
                newLeagueOptionsBinding.useDhCheckbox3.setVisibility(View.GONE);
                newLeagueOptionsBinding.interleaguePlayCheckBox.setVisibility(View.GONE);
                break;
            case 1:
                newLeagueOptionsBinding.league1NameEt.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.useDhCheckbox.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.league2NameEt.setVisibility(View.GONE);
                newLeagueOptionsBinding.useDhCheckbox2.setVisibility(View.GONE);
                newLeagueOptionsBinding.league3NameEt.setVisibility(View.GONE);
                newLeagueOptionsBinding.useDhCheckbox3.setVisibility(View.GONE);
                newLeagueOptionsBinding.interleaguePlayCheckBox.setVisibility(View.GONE);
                break;
            case 2:
                newLeagueOptionsBinding.league1NameEt.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.useDhCheckbox.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.league2NameEt.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.useDhCheckbox2.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.league3NameEt.setVisibility(View.GONE);
                newLeagueOptionsBinding.useDhCheckbox3.setVisibility(View.GONE);
                newLeagueOptionsBinding.interleaguePlayCheckBox.setVisibility(View.VISIBLE);
                break;
            case 3:
                newLeagueOptionsBinding.league1NameEt.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.useDhCheckbox.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.league2NameEt.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.useDhCheckbox2.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.league3NameEt.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.useDhCheckbox3.setVisibility(View.VISIBLE);
                newLeagueOptionsBinding.interleaguePlayCheckBox.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onStartSeasonFragmentInteraction(Organization organization);
    }
}
