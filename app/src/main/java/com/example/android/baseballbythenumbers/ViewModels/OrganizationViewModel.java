package com.example.android.baseballbythenumbers.ViewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Repository.Repository;

import java.util.List;

public class OrganizationViewModel {

    private final Repository mRepository;

    private final LiveData<List<Organization>> mOrganizations;

    public OrganizationViewModel(Application application){
        mRepository = new Repository(application);
        mOrganizations = mRepository.getAllOrganizations();

    }

    public LiveData<List<Organization>> getAllOrganizations() {
        return mOrganizations;
    }

    public void deleteAllData() {mRepository.deleteAll();}
}
