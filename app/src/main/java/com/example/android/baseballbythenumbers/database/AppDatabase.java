package com.example.android.baseballbythenumbers.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.android.baseballbythenumbers.AppExecutors;
import com.example.android.baseballbythenumbers.data.BattingLine;
import com.example.android.baseballbythenumbers.data.BattingStats;
import com.example.android.baseballbythenumbers.data.BoxScore;
import com.example.android.baseballbythenumbers.data.Division;
import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.data.League;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.PitchingLine;
import com.example.android.baseballbythenumbers.data.PitchingStats;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Schedule;
import com.example.android.baseballbythenumbers.data.Team;

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

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    Timber.d("Creating new database instance");
                    sInstance = buildDatabase(context.getApplicationContext());
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
    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                // Generate the database
                                AppDatabase database = AppDatabase.getInstance(appContext);
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
