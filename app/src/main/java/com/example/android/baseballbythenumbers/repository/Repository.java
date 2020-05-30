package com.example.android.baseballbythenumbers.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

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
import com.example.android.baseballbythenumbers.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

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

    private List<Future<?>> futureList;

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

        futureList = new ArrayList<>();
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
                return Transformations.map(boxScoreListLiveData, new Function<List<BoxScore>, Game>() {
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
            }

        });
        return gameLiveData;
    }

    public void deleteAll()  {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getOrganizationDao().deleteAll();
            }
        });
        futureList.add(future);
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
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingLineDao().insert(battingLine);
            }
        });
        futureList.add(future);
    }

    public void insertAllBattingLines (final List<BattingLine> battingLines) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingLineDao().insertAll(battingLines);
            }
        });
        futureList.add(future);
    }


    public void updateBattingLine (final BattingLine battingLine) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingLineDao().update(battingLine);
            }
        });
        futureList.add(future);
    }

    public LiveData<List<Organization>> getAllOrganizations () {
        return mOrganizations;
    }

    public Organization[] getAnyOrganization() {
        return mDatabase.getOrganizationDao().getAnyOrganization();
    }

    public void insertOrganization (final Organization Organization) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getOrganizationDao().insert(Organization);
            }
        });
        futureList.add(future);
    }




    public void updateOrganization (final Organization Organization) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getOrganizationDao().update(Organization);
            }
        });
        futureList.add(future);
    }

    public LiveData<List<Schedule>> getAllSchedules () {
        return mSchedules;
    }

    public void insertSchedule (final Schedule schedule) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getScheduleDao().insert(schedule);
            }
        });
        futureList.add(future);
    }


    public void updateSchedule (final Schedule schedule) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getScheduleDao().update(schedule);
            }
        });
        futureList.add(future);
    }


    public void insertAllSchedules (final List<Schedule> schedules) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getScheduleDao().insertAll(schedules);
            }
        });
        futureList.add(future);
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
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getGameDao().insert(game);
            }
        });
        futureList.add(future);
    }


    public void updateGame (final Game game) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getGameDao().update(game);
            }
        });
        futureList.add(future);
    }

    public void updateAllGames (final List<Game> gameList) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getGameDao().updateAll(gameList);
            }
        });
        futureList.add(future);
    }


    public void insertAllGames (final List<Game> games) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getGameDao().insertAll(games);
            }
        });
        futureList.add(future);
    }


    public LiveData<List<BoxScore>> getAllBoxScores () {
        return mBoxScore;
    }

    public List<BoxScore> getBoxScoresForGame(String gameId) {
        return mDatabase.getBoxScoreDao().findBoxScoresForGame(gameId);
    }

    public void insertBoxScore (final BoxScore boxScore) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBoxScoreDao().insert(boxScore);
            }
        });
        futureList.add(future);
    }
    
    public void updateBoxScore (final BoxScore boxScore) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBoxScoreDao().update(boxScore);
            }
        });
        futureList.add(future);
    }

    public void updateAllBoxScores (final List<BoxScore> boxScoreList) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBoxScoreDao().updateAll(boxScoreList);
            }
        });
        futureList.add(future);
    }

    public LiveData<List<PitchingLine>> getAllPitchingLines () {
        return mPitchingLines;
    }

    public List<PitchingLine> getPitchingLinesForBoxScore (String boxScoreId) {
        return mDatabase.getPitchingLineDao().findPitchingLinesForBoxScore(boxScoreId);
    }

    public void insertPitchingLine (final PitchingLine pitchingLine) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingLineDao().insert(pitchingLine);
            }
        });
        futureList.add(future);
    }

    public void updatePitchingLine (final PitchingLine pitchingLine) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingLineDao().update(pitchingLine);
            }
        });
        futureList.add(future);
    }

    public void insertAllPitchingLines (final List<PitchingLine> pitchingLines) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingLineDao().insertAll(pitchingLines);
            }
        });
        futureList.add(future);
    }

    public LiveData<List<League>> getAllLeagues () {
        return mLeagues;
    }

    public List<League> getLeaguesForOrganization (String orgId) {
        return mDatabase.getLeagueDao().findLeaguesForOrganization(orgId);
    }

    public void insertLeague (final League league) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getLeagueDao().insert(league);
            }
        });
        futureList.add(future);
    }


    public void updateLeague (final League league) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getLeagueDao().update(league);
            }
        });
        futureList.add(future);
    }


    public void insertAllLeagues (final List<League> leagues) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getLeagueDao().insertAll(leagues);
            }
        });
        futureList.add(future);
    }


    public LiveData<List<Division>> getAllDivisions () {
        return mDivisions;
    }

    public List<Division> getDivisionsForLeague(String leagueId) {
        return mDatabase.getDivisionDao().findDivisionsForLeague(leagueId);
    }

    public void insertDivision (final Division division) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getDivisionDao().insert(division);
            }
        });
        futureList.add(future);
    }

    public void updateDivision (final Division division) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getDivisionDao().update(division);
            }
        });
        futureList.add(future);
    }

    public void insertAllDivisions (final List<Division> divisions) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getDivisionDao().insertAll(divisions);
            }
        });
        futureList.add(future);
    }


    public LiveData<List<Team>> getAllTeams () {
        return mTeams;
    }

    public List<Team> getTeamsForDivision(String divisionId) {
        return mDatabase.getTeamDao().findTeamsForDivision(divisionId);
    }

    public void insertTeam (final Team team) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getTeamDao().insert(team);
            }
        });
        futureList.add(future);
    }

    public void updateTeam (final Team team) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getTeamDao().update(team);
            }
        });
        futureList.add(future);
    }

    public void updateAllTeams (final List<Team> teamList) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getTeamDao().updateAll(teamList);
            }
        });
        futureList.add(future);
    }

    public void insertAllTeams (final List<Team> teams) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getTeamDao().insertAll(teams);
            }
        });
        futureList.add(future);
    }


    public LiveData<List<Player>> getAllPlayers () {
        return mPlayers;
    }

    public List<Player> getPlayersForTeam(String teamId) {
        return mDatabase.getPlayersDao().findPlayersForTeam(teamId);
    }

    public void insertPlayer (final Player player) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPlayersDao().insert(player);
            }
        });
        futureList.add(future);
    }


    public void updatePlayer (final Player player) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPlayersDao().update(player);
            }
        });
        futureList.add(future);
    }

    public void updateAllPlayers (final List<Player> playerList) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPlayersDao().updateAll(playerList);
            }
        });
        futureList.add(future);
    }


    public void insertAllPlayers (final List<Player> players) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPlayersDao().insertAll(players);
            }
        });
        futureList.add(future);
    }


    public LiveData<List<BattingStats>> getAllBattingStats () {
        return mBattingStats;
    }

    public List<BattingStats> getBattingStatsForPlayer(String playerId) {
        return mDatabase.getBattingStatsDao().findBattingStatsForPlayer(playerId);
    }

    public void insertBattingStats (final BattingStats battingStats) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingStatsDao().insert(battingStats);
            }
        });
        futureList.add(future);
    }


    public void updateBattingStats (final BattingStats battingStats) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingStatsDao().update(battingStats);
            }
        });
        futureList.add(future);
    }

    public void updateAllBattingStats (final List<BattingStats> battingStatsList) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingStatsDao().updateAll(battingStatsList);
            }
        });
        futureList.add(future);
    }


    public void insertAllBattingStats (final List<BattingStats> battingStats) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getBattingStatsDao().insertAll(battingStats);
            }
        });
        futureList.add(future);
    }

    public LiveData<List<PitchingStats>> getAllPitchingStats () {
        return mPitchingStats;
    }

    public List<PitchingStats> getPitchingStatsForPlayer(String playerId) {
        return mDatabase.getPitchingStatsDao().findPitchingStatsForPlayer(playerId);
    }

    public void insertPitchingStats (final PitchingStats pitchingStats) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingStatsDao().insert(pitchingStats);
            }
        });
        futureList.add(future);
    }


    public void updatePitchingStats (final PitchingStats pitchingStats) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingStatsDao().update(pitchingStats);
            }
        });
        futureList.add(future);
    }

    public void updateAllPitchingStats (final List<PitchingStats> pitchingStatsList) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingStatsDao().updateAll(pitchingStatsList);
            }
        });
        futureList.add(future);
    }


    public void insertAllPitchingStats (final List<PitchingStats> pitchingStatsList) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                mDatabase.getPitchingStatsDao().insertAll(pitchingStatsList);
            }
        });
        futureList.add(future);
    }

    public List<Future<?>> getFutureList() {
        return futureList;
    }
}
