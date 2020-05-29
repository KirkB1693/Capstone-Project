package com.example.android.baseballbythenumbers.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Repository.Repository;

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
