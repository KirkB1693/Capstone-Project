package com.example.android.baseballbythenumbers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.databinding.ActivityNewLeagueSetupBinding;

import java.util.List;

public class NewLeagueSetupActivity extends AppCompatActivity implements PickCountriesFragment.OnPickCountriesFragmentInteractionListener,
        PickCityFragment.OnListFragmentInteractionListener, NewLeagueOptionsFragment.OnFragmentInteractionListener, CreateOrganizationFragment.OrgCreationEndSignal {

    public static final String PICK_COUNTRIES_FRAGMENT_TAG = "pickCountriesFragmentTag";
    public static final String PICK_CITIES_FRAGMENT_TAG = "pickCitiesFragmentTag";
    public static final String FINISH_LEAGUE_SETUP_FRAGMENT_TAG = "finishLeagueSetupTag";
    public static final String CREATE_ORGANIZATION_TAG = "createOrganizationTag";
    private ActivityNewLeagueSetupBinding newLeagueSetupBinding;
    private int countries;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_league_setup);
        newLeagueSetupBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_league_setup);

        PickCountriesFragment fragment;
        if (savedInstanceState != null) {
            fragment = (PickCountriesFragment) getSupportFragmentManager().findFragmentByTag(PICK_COUNTRIES_FRAGMENT_TAG);
        } else {
            fragment = new PickCountriesFragment();
            getSupportFragmentManager().beginTransaction().add(newLeagueSetupBinding.fragmentPlaceholder.getId(), fragment, PICK_COUNTRIES_FRAGMENT_TAG).commit();
        }

    }

    @Override
    public void onPickCountriesFragmentInteraction(int countriesToInclude) {
        countries = countriesToInclude;
        changeFragmentsToPickCity();
    }

    private void changeFragmentsToPickCity() {
        newLeagueSetupBinding.newLeagueSetupLabel.setText("Please pick a city for your team");
        PickCityFragment fragment = PickCityFragment.newInstance(1, countries);
        getSupportFragmentManager().beginTransaction().replace(newLeagueSetupBinding.fragmentPlaceholder.getId(), fragment, PICK_CITIES_FRAGMENT_TAG)
                .addToBackStack(PICK_COUNTRIES_FRAGMENT_TAG).commit();
    }

    @Override
    public void onListFragmentInteraction(String cityName) {
        this.cityName = cityName;
        changeFragmentsToNewLeagueOptions();
    }

    private void changeFragmentsToNewLeagueOptions() {
        newLeagueSetupBinding.newLeagueSetupLabel.setText("Finish League Setup");
        getSupportFragmentManager().beginTransaction().replace(newLeagueSetupBinding.fragmentPlaceholder.getId(), new NewLeagueOptionsFragment(), FINISH_LEAGUE_SETUP_FRAGMENT_TAG)
                .addToBackStack(PICK_CITIES_FRAGMENT_TAG).commit();
    }

    @Override
    public void onStartSeasonFragmentInteraction() {
        newLeagueSetupBinding.newLeagueSetupLabel.setText("Creating New League");
    }

    private void changeFragmentToOrganizationCreation(String userName, String teamName, int numOfLeagues, List<String> leagueNames, List<Boolean> useDhList,
                                                      int numOfDivisions, int numOfTeamsPerDivision, int numOfGamesPerSeries, boolean interleaguePlay) {

        CreateOrganizationFragment fragment = CreateOrganizationFragment.newInstance(countries, cityName, userName, teamName, numOfLeagues, leagueNames, useDhList,
                numOfDivisions, numOfTeamsPerDivision, numOfGamesPerSeries, interleaguePlay);
        getSupportFragmentManager().beginTransaction().replace(newLeagueSetupBinding.fragmentPlaceholder.getId(), fragment, CREATE_ORGANIZATION_TAG)
                .addToBackStack(FINISH_LEAGUE_SETUP_FRAGMENT_TAG).commit();
    }

    @Override
    public void onOrgCreationEndSignal(Organization newOrganization) {
        returnToMainActivity();
    }

    private void returnToMainActivity() {
    }
}
