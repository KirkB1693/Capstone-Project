package com.example.android.baseballbythenumbers.utilities;

import com.example.android.baseballbythenumbers.data.Division;
import com.example.android.baseballbythenumbers.data.Team;

import java.util.List;

public interface SyncServiceSupport {
    Division getDivision(String divisionId);
    List<Team> getTeamsForDivision(String divisionId);
}