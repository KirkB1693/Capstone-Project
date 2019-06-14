package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.RoomDatabase;

import com.example.android.baseballbythenumbers.Data.BattingStats;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;

@android.arch.persistence.room.Database(entities = {Organization.class, League.class, Division.class, Team.class, Player.class, BattingStats.class, PitchingStats.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract OrganizationDao getOrganizationDao();
    public abstract LeagueDao getLeagueDao();
    public abstract DivisionDao getDivisionDao();
    public abstract TeamDao getTeamDao();
    public abstract PlayersDao getPlayersDao();
    public abstract BattingStatsDao getBattingStatsDao();
    public abstract PitchingStatsDao getPitchingStatsDao();
}
