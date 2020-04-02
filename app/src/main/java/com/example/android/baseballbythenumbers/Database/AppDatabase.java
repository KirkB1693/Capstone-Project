package com.example.android.baseballbythenumbers.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.android.baseballbythenumbers.AppExecutors;
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

@Database(entities = {Organization.class, League.class, Division.class, Team.class, Player.class, BattingStats.class, PitchingStats.class,
        Schedule.class, Game.class, BoxScore.class, BattingLine.class, PitchingLine.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "organizations";

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

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    Timber.d("Creating new database instance");
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        Timber.d( "Getting the database instance");
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                // Generate the database
                                AppDatabase database = AppDatabase.getInstance(appContext, executors);
                                // notify that the database was created and it's ready to be used
                                database.setDatabaseCreated();
                            }
                        });
                    }
                })
                .build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
