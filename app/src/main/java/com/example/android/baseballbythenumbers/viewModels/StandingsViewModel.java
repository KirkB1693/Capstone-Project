package com.example.android.baseballbythenumbers.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.repository.Repository;

import java.util.List;

public class StandingsViewModel extends AndroidViewModel {

    private Organization organization;
    private final Repository mRepository;

    public StandingsViewModel(Application application){
        super(application);
        mRepository = ((BaseballByTheNumbersApp) application).getRepository();
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public List<Team> getTeamsFromDivision(String divisionId){
        return mRepository.getTeamsForDivision(divisionId);
    }
}
