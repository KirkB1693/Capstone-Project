package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.baseballbythenumbers.Data.BattingLine;
import com.example.android.baseballbythenumbers.Data.BattingStats;
import com.example.android.baseballbythenumbers.Data.BoxScore;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.PitchingLine;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;

import timber.log.Timber;

@android.arch.persistence.room.Database(entities = {Organization.class, League.class, Division.class, Team.class, Player.class, BattingStats.class, PitchingStats.class,
        Schedule.class, Game.class, BoxScore.class, BattingLine.class, PitchingLine.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database sInstance;

    public abstract OrganizationDao getOrganizationDao();
    public abstract LeagueDao getLeagueDao();
    public abstract DivisionDao getDivisionDao();
    public abstract TeamDao getTeamDao();
    public abstract PlayersDao getPlayersDao();
    public abstract BattingStatsDao getBattingStatsDao();
    public abstract PitchingStatsDao getPitchingStatsDao();
    public abstract ScheduleDao getScheduleDao();
    public abstract GameDao getGameDao();
    public abstract BoxScoreDao getBoxScoreDao();
    public abstract BattingLineDao getBattingLineDao();
    public abstract PitchingLineDao getPitchingLineDao();


    public static Database getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Database.class) {
                if (sInstance == null) {
                    Timber.d("Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "organizations")
                            .build();
                }
            }
        }
        Timber.d( "Getting the database instance");
        return sInstance;
    }
}
