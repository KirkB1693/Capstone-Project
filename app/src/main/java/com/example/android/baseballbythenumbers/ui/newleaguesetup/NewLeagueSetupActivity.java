package com.example.android.baseballbythenumbers.ui.newleaguesetup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.ui.main.MainActivity;
import com.example.android.baseballbythenumbers.databinding.ActivityNewLeagueSetupBinding;

public class NewLeagueSetupActivity extends AppCompatActivity implements PickCountriesFragment.OnPickCountriesFragmentInteractionListener,
        PickCityFragment.OnListFragmentInteractionListener, NewLeagueOptionsFragment.OnFragmentInteractionListener, SaveOrganizationFragment.OrgSaveEndSignal {

    public static final String PICK_COUNTRIES_FRAGMENT_TAG = "pickCountriesFragmentTag";
    public static final String PICK_CITIES_FRAGMENT_TAG = "pickCitiesFragmentTag";
    public static final String FINISH_LEAGUE_SETUP_FRAGMENT_TAG = "finishLeagueSetupTag";
    public static final String SAVE_ORGANIZATION_FRAGMENT_TAG = "saveOrganizationTag";
    public static final String ORGANIZATION_EXTRA = "organization";
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
        newLeagueSetupBinding.newLeagueSetupLabel.setText(R.string.pick_a_city_label);
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
        newLeagueSetupBinding.newLeagueSetupLabel.setText(R.string.finish_league_setup_label);
        NewLeagueOptionsFragment fragment = NewLeagueOptionsFragment.newInstance(countries, cityName);
        getSupportFragmentManager().beginTransaction().replace(newLeagueSetupBinding.fragmentPlaceholder.getId(), fragment, FINISH_LEAGUE_SETUP_FRAGMENT_TAG)
                .addToBackStack(PICK_CITIES_FRAGMENT_TAG).commit();
    }

    @Override
    public void onStartSeasonFragmentInteraction(Organization newOrganization) {
        newLeagueSetupBinding.newLeagueSetupLabel.setText(R.string.save_new_league_label);
        saveNewOrganization(newOrganization);
    }

    private void saveNewOrganization(Organization newOrganization) {
        SaveOrganizationFragment fragment = SaveOrganizationFragment.newInstance(getApplication(), newOrganization);
        getSupportFragmentManager().beginTransaction().replace(newLeagueSetupBinding.fragmentPlaceholder.getId(), fragment, SAVE_ORGANIZATION_FRAGMENT_TAG)
                .addToBackStack(FINISH_LEAGUE_SETUP_FRAGMENT_TAG).commit();
    }

    @Override
    public void onOrganizationSaveEndSignal(Organization newOrganization) {
        returnToMainActivity(newOrganization);
    }

    private void returnToMainActivity(Organization newOrganization) {
        newLeagueSetupBinding.newLeagueSetupLabel.setText(R.string.finished_league_setup_label);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ORGANIZATION_EXTRA, newOrganization);
        this.startActivity(intent);
        this.finish();
    }
}
