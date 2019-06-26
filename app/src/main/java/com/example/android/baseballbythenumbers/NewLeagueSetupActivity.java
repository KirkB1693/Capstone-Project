package com.example.android.baseballbythenumbers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.baseballbythenumbers.databinding.ActivityNewLeagueSetupBinding;

public class NewLeagueSetupActivity extends AppCompatActivity implements PickCountriesFragment.OnPickCountriesFragmentInteractionListener, PickCityFragment.OnListFragmentInteractionListener {

    public static final String PICK_COUNTRIES_FRAGMENT_TAG = "pickCountriesFragmentTag";
    public static final String PICK_CITIES_FRAGMENT_TAG = "pickCitiesFragmentTag";
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
        PickCityFragment fragment = PickCityFragment.newInstance(1, countries);
        getSupportFragmentManager().beginTransaction().replace(newLeagueSetupBinding.fragmentPlaceholder.getId(), fragment, PICK_CITIES_FRAGMENT_TAG).commit();
    }

    @Override
    public void onListFragmentInteraction(String cityName) {
        this.cityName = cityName;
        Toast.makeText(this, "City Picked is " + this.cityName, Toast.LENGTH_SHORT).show();
    }
}
