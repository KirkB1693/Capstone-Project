package com.example.android.baseballbythenumbers;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Generators.OrganizationGenerator;
import com.example.android.baseballbythenumbers.Generators.ScheduleGenerator;
import com.example.android.baseballbythenumbers.databinding.FragmentCreateOrganizationBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateOrganizationFragment.OrgCreationEndSignal} interface
 * to handle interaction events.
 * Use the {@link CreateOrganizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOrganizationFragment extends Fragment {
    private static final String ARG_COUNTRIES = "countries";
    private static final String ARG_USER_CITY = "user_city";
    private static final String ARG_USER_NAME = "user_name";
    private static final String ARG_TEAM_NAME = "team_name";
    private static final String ARG_NUM_OF_LEAGUES = "num_of_leagues";
    private static final String ARG_LEAGUE_NAMES = "league_names";
    private static final String ARG_USE_DH_LIST = "use_dh_list";
    private static final String ARG_DIVISIONS = "divisions";
    private static final String ARG_TEAMS = "teams";
    private static final String ARG_GAMES_PER_SERIES = "games_per_series";
    private static final String ARG_INTERLEAGUE_PLAY = "interleague_play";

    private int countries;
    private String userCity;
    private String userName;
    private String teamName;
    private int numOfLeagues;
    private List<String> leagueNamesList = new ArrayList<>();
    private List<Boolean> useDHList = new ArrayList<>();
    private int numOfDivisions;
    private int numOfTeamsPerDivision;
    private int numOfGamesPerSeries;
    private boolean interleaguePlay;

    private OrgCreationEndSignal mListener;

    private FragmentCreateOrganizationBinding createOrganizationBinding;

    public CreateOrganizationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param countries
     * @param userCity
     * @param userName
     * @param teamName
     * @param numOfLeagues 
     * @param leagueNames 
     * @param useDhList 
     * @param numOfDivisions 
     * @param numOfTeamsPerDivision 
     * @param numOfGamesPerSeries
     * @param interleaguePlay
     * @return A new instance of fragment CreateOrganizationFragment.
     */
    
    public static CreateOrganizationFragment newInstance(int countries, String userCity, String userName, String teamName, int numOfLeagues, List<String> leagueNames, List<Boolean> useDhList,
                                                         int numOfDivisions, int numOfTeamsPerDivision, int numOfGamesPerSeries, boolean interleaguePlay) {
        CreateOrganizationFragment fragment = new CreateOrganizationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNTRIES, countries);
        args.putString(ARG_USER_CITY, userCity);
        args.putString(ARG_USER_NAME, userName);
        args.putString(ARG_TEAM_NAME, teamName);
        args.putInt(ARG_NUM_OF_LEAGUES, numOfLeagues);
        args.putStringArrayList(ARG_LEAGUE_NAMES, (ArrayList<String>) leagueNames);
        boolean[] array = new boolean[useDhList.size()];
        for (int i = 0; i < useDhList.size(); i++) {
            array[i] = useDhList.get(i);
        }
        args.putBooleanArray(ARG_USE_DH_LIST, array);
        args.putInt(ARG_DIVISIONS, numOfDivisions);
        args.putInt(ARG_TEAMS, numOfTeamsPerDivision);
        args.putInt(ARG_GAMES_PER_SERIES, numOfGamesPerSeries);
        args.putBoolean(ARG_INTERLEAGUE_PLAY, interleaguePlay);
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
            teamName = getArguments().getString(ARG_TEAM_NAME);
            numOfLeagues = getArguments().getInt(ARG_NUM_OF_LEAGUES);
            leagueNamesList = getArguments().getStringArrayList(ARG_LEAGUE_NAMES);
            boolean[] array = getArguments().getBooleanArray(ARG_USE_DH_LIST);
            useDHList.clear();
            for (boolean item : array) {
                useDHList.add(item);
            }
            numOfDivisions = getArguments().getInt(ARG_DIVISIONS);
            numOfTeamsPerDivision = getArguments().getInt(ARG_TEAMS);
            numOfGamesPerSeries = getArguments().getInt(ARG_GAMES_PER_SERIES);
            interleaguePlay = getArguments().getBoolean(ARG_INTERLEAGUE_PLAY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        createOrganizationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_organization, container, false);
        return createOrganizationBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Organization newOrganization = createTheNewOrganization();
        createOrganizationBinding.orgGeneratorTv.setText("Creating Schedule...");
        createOrganizationBinding.generateOrgProgressbar.setProgress(0);
        int gamesToGenerate;
        if (interleaguePlay) {
            gamesToGenerate = ((numOfLeagues * numOfDivisions * numOfTeamsPerDivision) - 1) * 2 * numOfGamesPerSeries;  // Play every other team (n-1)  twice (home and away)
        } else {
            gamesToGenerate = ((numOfDivisions * numOfTeamsPerDivision) -1) * 2 * numOfGamesPerSeries * numOfLeagues;
        }
        createOrganizationBinding.generateOrgProgressbar.setMax(gamesToGenerate);
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(newOrganization);
        scheduleGenerator.generateSchedule(numOfGamesPerSeries, interleaguePlay, createOrganizationBinding.generateOrgProgressbar);
        onOrganizationCreationEnd(newOrganization);
    }

    private Organization createTheNewOrganization() {
        OrganizationGenerator organizationGenerator = new OrganizationGenerator(getContext());
        return organizationGenerator.generateOrganization(userName, 0, numOfLeagues, leagueNamesList, useDHList, numOfTeamsPerDivision,
                numOfDivisions, countries, null, teamName, userCity, createOrganizationBinding.generateOrgProgressbar);
    }

    public void onOrganizationCreationEnd(Organization newOrganization) {
        if (mListener != null) {
            mListener.onOrgCreationEndSignal(newOrganization);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrgCreationEndSignal) {
            mListener = (OrgCreationEndSignal) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OrgCreationEndSignal {
        void onOrgCreationEndSignal(Organization newOrganization);
    }
}
