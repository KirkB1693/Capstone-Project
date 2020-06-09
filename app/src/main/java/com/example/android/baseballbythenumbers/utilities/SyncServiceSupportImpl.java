package com.example.android.baseballbythenumbers.utilities;

import android.content.Context;

import com.example.android.baseballbythenumbers.data.Division;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.database.AppDatabase;
import com.example.android.baseballbythenumbers.database.DivisionDao;
import com.example.android.baseballbythenumbers.database.TeamDao;

import java.util.List;

public class SyncServiceSupportImpl implements SyncServiceSupport {
    private TeamDao teamDao;
    private DivisionDao divisionDao;

    public SyncServiceSupportImpl(Context context) {

        divisionDao = AppDatabase.getInstance(context).getDivisionDao();
        teamDao = AppDatabase.getInstance(context).getTeamDao();
        // getDatabase() from where we get DB instance.
    }

    @Override
    public Division getDivision(String divisionId) {
        return divisionDao.findDivisionById(divisionId);
    }

    @Override
    public List<Team> getTeamsForDivision(String divisionId) {
        return teamDao.findTeamsForDivision(divisionId);
    }
}
