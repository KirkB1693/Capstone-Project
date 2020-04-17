package com.example.android.baseballbythenumbers.Repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

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
import com.example.android.baseballbythenumbers.Database.AppDatabase;
import com.example.android.baseballbythenumbers.Database.BattingLineDao;
import com.example.android.baseballbythenumbers.Database.BattingStatsDao;
import com.example.android.baseballbythenumbers.Database.BoxScoreDao;
import com.example.android.baseballbythenumbers.Database.DivisionDao;
import com.example.android.baseballbythenumbers.Database.GameDao;
import com.example.android.baseballbythenumbers.Database.LeagueDao;
import com.example.android.baseballbythenumbers.Database.OrganizationDao;
import com.example.android.baseballbythenumbers.Database.PitchingLineDao;
import com.example.android.baseballbythenumbers.Database.PitchingStatsDao;
import com.example.android.baseballbythenumbers.Database.PlayersDao;
import com.example.android.baseballbythenumbers.Database.ScheduleDao;
import com.example.android.baseballbythenumbers.Database.TeamDao;

import java.util.List;

public class Repository {

    private static Repository sInstance;
    private final AppDatabase mDatabase;

    private LiveData<List<BattingLine>> mBattingLines;
    private MutableLiveData<List<BattingLine>> mBattingLineSearchResults;

    private LiveData<List<BattingStats>> mBattingStats;

    private LiveData<List<BoxScore>> mBoxScore;

    private LiveData<List<Division>> mDivisions;

    private LiveData<List<Game>> mGames;

    private LiveData<List<League>> mLeagues;

    private LiveData<List<Organization>> mOrganizations;

    private LiveData<List<PitchingLine>> mPitchingLines;

    private LiveData<List<PitchingStats>> mPitchingStats;

    private LiveData<List<Player>> mPlayers;

    private LiveData<List<Schedule>> mSchedules;

    private LiveData<List<Team>> mTeams;

    private Repository(final AppDatabase database) {
        mDatabase = database;

        mBattingLines = mDatabase.getBattingLineDao().getAllBattingLines();

        mOrganizations = mDatabase.getOrganizationDao().getAllOrganizations();

        mSchedules = mDatabase.getScheduleDao().getAllSchedules();

        mGames = mDatabase.getGameDao().getAllGames();

        mBoxScore = mDatabase.getBoxScoreDao().getAllBoxScores();

        mPitchingLines = mDatabase.getPitchingLineDao().getAllPitchingLines();

        mLeagues = mDatabase.getLeagueDao().getAllLeagues();

        mDivisions = mDatabase.getDivisionDao().getAllDivisions();

        mTeams = mDatabase.getTeamDao().getAllTeams();

        mPlayers = mDatabase.getPlayersDao().getAllPlayers();

        mBattingStats = mDatabase.getBattingStatsDao().getAllBattingStats();

        mPitchingStats = mDatabase.getPitchingStatsDao().getAllPitchingStats();
    }

    public static Repository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<PitchingLine>> getLiveDataPitchingLinesForBoxScore(String boxScoreId){
        return mDatabase.getPitchingLineDao().findLiveDataPitchingLinesForBoxScore(boxScoreId);
    }

    public LiveData<List<BattingLine>> getLiveDataBattingLinesForBoxScore(String boxScoreId){
        return mDatabase.getBattingLineDao().findLiveDataBattingLinesForBoxScore(boxScoreId);
    }

    public LiveData<Game> getLiveDataForGame(final String gameId) {
        LiveData<Game> gameLiveData = mDatabase.getGameDao().findGameWithGameId(gameId);
        gameLiveData = Transformations.switchMap(gameLiveData, new Function<Game, LiveData<Game>>() {
            @Override
            public LiveData<Game> apply(final Game inputGame) {
                LiveData<List<BoxScore>> boxScoreListLiveData = mDatabase.getBoxScoreDao().findLiveDataBoxScoresForGame(inputGame.getGameId());
                LiveData<Game> outputGameLiveData = Transformations.map(boxScoreListLiveData, new Function<List<BoxScore>, Game>() {
                    @Override
                    public Game apply(List<BoxScore> inputBoxScoreList) {
                        for (BoxScore boxScore : inputBoxScoreList) {
                            if (boxScore.isBoxScoreForHomeTeam()) {
                                inputGame.setHomeBoxScore(boxScore);
                            } else {
                                inputGame.setVisitorBoxScore(boxScore);
                            }
                        }
                        return inputGame;
                    }
                });
                return outputGameLiveData;
            }

        });
        return gameLiveData;
    }

    public void deleteAll()  {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getOrganizationDao().deleteAll();
            }
        });
    }

    public Organization getOrganizationById (String id) {
        return mDatabase.getOrganizationDao().getOrganizationById(id);
    }

    public List<Schedule> getSchedulesForOrganization (String orgId){
        return mDatabase.getScheduleDao().getSchedulesForOrganization(orgId);
    }

    public Team getTeamWithTeamName (String teamName) {
        return mDatabase.getTeamDao().getTeamWithTeamName(teamName);
    }

    public LiveData<List<BattingLine>> getAllBattingLines () {
        return mBattingLines;
    }

    public List<BattingLine> getBattingLinesForBoxScore (String boxScoreId) {
        return mDatabase.getBattingLineDao().findBattingLinesForBoxScore(boxScoreId);
    }

    public void insertBattingLine (final BattingLine battingLine) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingLineDao().insert(battingLine);
            }
        });
    }

    public void insertAllBattingLines (final List<BattingLine> battingLines) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingLineDao().insertAll(battingLines);
            }
        });
    }


    public void updateBattingLine (final BattingLine battingLine) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingLineDao().update(battingLine);
            }
        });
    }

    public LiveData<List<Organization>> getAllOrganizations () {
        return mOrganizations;
    }

    public Organization[] getAnyOrganization() {
        return mDatabase.getOrganizationDao().getAnyOrganization();
    }

    public void insertOrganization (final Organization Organization) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getOrganizationDao().insert(Organization);
            }
        });

    }




    public void updateOrganization (final Organization Organization) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getOrganizationDao().update(Organization);
            }
        });
    }

    public LiveData<List<Schedule>> getAllSchedules () {
        return mSchedules;
    }

    public void insertSchedule (final Schedule schedule) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getScheduleDao().insert(schedule);
            }
        });
    }


    public void updateSchedule (final Schedule schedule) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getScheduleDao().update(schedule);
            }
        });
    }


    @SuppressWarnings("unchecked")
    public void insertAllSchedules (final List<Schedule> schedules) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getScheduleDao().insertAll(schedules);
            }
        });
    }


    public LiveData<List<Game>> getAllGames () {
        return mGames;
    }
    
    public List<Game> getGamesForSchedule(String scheduleId) {
        return mDatabase.getGameDao().findGamesForSchedule(scheduleId);
    }
    
    public List<Game> getGamesForDay (int day, String scheduleId) {
        return mDatabase.getGameDao().findGamesForDayInSchedule(day, scheduleId);
    }

    public List<Game> getGamesForTeamInSchedule(String teamName, String scheduleId){
        return mDatabase.getGameDao().findGamesForTeamNameInSchedule(teamName, scheduleId);
    }

    public void insertGame (final Game game) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getGameDao().insert(game);
            }
        });
    }


    public void updateGame (final Game game) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getGameDao().update(game);
            }
        });
    }


    @SuppressWarnings("unchecked")
    public void insertAllGames (final List<Game> games) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getGameDao().insertAll(games);
            }
        });
    }


    public LiveData<List<BoxScore>> getAllBoxScores () {
        return mBoxScore;
    }

    public List<BoxScore> getBoxScoresForGame(String gameId) {
        return mDatabase.getBoxScoreDao().findBoxScoresForGame(gameId);
    }

    public void insertBoxScore (final BoxScore boxScore) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBoxScoreDao().insert(boxScore);
            }
        });
    }
    
    public void updateBoxScore (final BoxScore boxScore) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBoxScoreDao().update(boxScore);
            }
        });
    }

    public LiveData<List<PitchingLine>> getAllPitchingLines () {
        return mPitchingLines;
    }

    public List<PitchingLine> getPitchingLinesForBoxScore (String boxScoreId) {
        return mDatabase.getPitchingLineDao().findPitchingLinesForBoxScore(boxScoreId);
    }

    public void insertPitchingLine (final PitchingLine pitchingLine) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingLineDao().insert(pitchingLine);
            }
        });
    }

    public void updatePitchingLine (final PitchingLine pitchingLine) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingLineDao().update(pitchingLine);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void insertAllPitchingLines (final List<PitchingLine> pitchingLines) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingLineDao().insertAll(pitchingLines);
            }
        });
    }

    public LiveData<List<League>> getAllLeagues () {
        return mLeagues;
    }

    public List<League> getLeaguesForOrganization (String orgId) {
        return mDatabase.getLeagueDao().findLeaguesForOrganization(orgId);
    }

    public void insertLeague (final League league) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getLeagueDao().insert(league);
            }
        });
    }


    public void updateLeague (final League league) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getLeagueDao().update(league);
            }
        });
    }


    @SuppressWarnings("unchecked")
    public void insertAllLeagues (final List<League> leagues) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getLeagueDao().insertAll(leagues);
            }
        });
    }


    public LiveData<List<Division>> getAllDivisions () {
        return mDivisions;
    }

    public List<Division> getDivisionsForLeague(String leagueId) {
        return mDatabase.getDivisionDao().findDivisionsForLeague(leagueId);
    }

    public void insertDivision (final Division division) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getDivisionDao().insert(division);
            }
        });
    }

    public void updateDivision (final Division division) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getDivisionDao().update(division);
            }
        });
    }

    public void insertAllDivisions (final List<Division> divisions) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getDivisionDao().insertAll(divisions);
            }
        });
    }


    public LiveData<List<Team>> getAllTeams () {
        return mTeams;
    }

    public List<Team> getTeamsForDivision(String divisionId) {
        return mDatabase.getTeamDao().findTeamsForDivision(divisionId);
    }

    public void insertTeam (final Team team) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getTeamDao().insert(team);
            }
        });
    }

    public void updateTeam (final Team team) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getTeamDao().update(team);
            }
        });
    }



    public void insertAllTeams (final List<Team> teams) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getTeamDao().insertAll(teams);
            }
        });
    }


    public LiveData<List<Player>> getAllPlayers () {
        return mPlayers;
    }

    public List<Player> getPlayersForTeam(String teamId) {
        return mDatabase.getPlayersDao().findPlayersForTeam(teamId);
    }

    public void insertPlayer (final Player player) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPlayersDao().insert(player);
            }
        });
    }


    public void updatePlayer (final Player player) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPlayersDao().update(player);
            }
        });
    }


    public void insertAllPlayers (final List<Player> players) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPlayersDao().insertAll(players);
            }
        });
    }


    public LiveData<List<BattingStats>> getAllBattingStats () {
        return mBattingStats;
    }

    public List<BattingStats> getBattingStatsForPlayer(String playerId) {
        return mDatabase.getBattingStatsDao().findBattingStatsForPlayer(playerId);
    }

    public void insertBattingStats (final BattingStats battingStats) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingStatsDao().insert(battingStats);
            }
        });
    }


    public void updateBattingStats (final BattingStats battingStats) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingStatsDao().update(battingStats);
            }
        });
    }


    public void insertAllBattingStats (final List<BattingStats> battingStats) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingStatsDao().insertAll(battingStats);
            }
        });
    }

    public LiveData<List<PitchingStats>> getAllPitchingStats () {
        return mPitchingStats;
    }

    public List<PitchingStats> getPitchingStatsForPlayer(String playerId) {
        return mDatabase.getPitchingStatsDao().findPitchingStatsForPlayer(playerId);
    }

    public void insertPitchingStats (final PitchingStats pitchingStats) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingStatsDao().insert(pitchingStats);
            }
        });
    }


    public void updatePitchingStats (final PitchingStats pitchingStats) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingStatsDao().update(pitchingStats);
            }
        });
    }


    public void insertAllPitchingStats (final List<PitchingStats> pitchingStatsList) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingStatsDao().insertAll(pitchingStatsList);
            }
        });
    }

}
