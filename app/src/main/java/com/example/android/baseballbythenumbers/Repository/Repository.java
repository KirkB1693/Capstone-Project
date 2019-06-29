package com.example.android.baseballbythenumbers.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

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
import com.example.android.baseballbythenumbers.Database.BattingLineDao;
import com.example.android.baseballbythenumbers.Database.BattingStatsDao;
import com.example.android.baseballbythenumbers.Database.BoxScoreDao;
import com.example.android.baseballbythenumbers.Database.Database;
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

    private BattingLineDao mBattingLineDao;
    private LiveData<List<BattingLine>> mBattingLines;
    private MutableLiveData<List<BattingLine>> mBattingLineSearchResults;

    private BattingStatsDao mBattingStatsDao;
    private LiveData<List<BattingStats>> mBattingStats;

    private BoxScoreDao mBoxScoreDao;
    private LiveData<List<BoxScore>> mBoxScore;

    private DivisionDao mDivisionDao;
    private LiveData<List<Division>> mDivisions;

    private GameDao mGameDao;
    private LiveData<List<Game>> mGames;

    private LeagueDao mLeagueDao;
    private LiveData<List<League>> mLeagues;

    private OrganizationDao mOrganizationDao;
    private LiveData<List<Organization>> mOrganizations;

    private PitchingLineDao mPitchingLineDao;
    private LiveData<List<PitchingLine>> mPitchingLines;

    private PitchingStatsDao mPitchingStatsDao;
    private LiveData<List<PitchingStats>> mPitchingStats;

    private PlayersDao mPlayersDao;
    private LiveData<List<Player>> mPlayers;

    private ScheduleDao mScheduleDao;
    private LiveData<List<Schedule>> mSchedules;

    private TeamDao mTeamDao;
    private LiveData<List<Team>> mTeams;


    public Repository(Application application) {
        Database db = Database.getInstance(application);

        mBattingLineDao = db.getBattingLineDao();
        mBattingLines = mBattingLineDao.getAllBattingLines();

        mOrganizationDao = db.getOrganizationDao();
        mOrganizations = mOrganizationDao.getAllOrganizations();

        mScheduleDao = db.getScheduleDao();
        mSchedules = mScheduleDao.getAllSchedules();
        
        mGameDao = db.getGameDao();
        mGames = mGameDao.getAllGames();
        
        mBoxScoreDao = db.getBoxScoreDao();
        mBoxScore = mBoxScoreDao.getAllBoxScores();
        
        mPitchingLineDao = db.getPitchingLineDao();
        mPitchingLines = mPitchingLineDao.getAllPitchingLines();
        
        mLeagueDao = db.getLeagueDao();
        mLeagues = mLeagueDao.getAllLeagues();
        
        mDivisionDao = db.getDivisionDao();
        mDivisions = mDivisionDao.getAllDivisions();
        
        mTeamDao = db.getTeamDao();
        mTeams = mTeamDao.getAllTeams();
        
        mPlayersDao = db.getPlayersDao();
        mPlayers = mPlayersDao.getAllPlayers();
        
        mBattingStatsDao = db.getBattingStatsDao();
        mBattingStats = mBattingStatsDao.getAllBattingStats();
        
        mPitchingStatsDao = db.getPitchingStatsDao();
        mPitchingStats = mPitchingStatsDao.getAllPitchingStats();
    }

    public void deleteAll()  {
        new deleteAllOrganizationsAsyncTask(mOrganizationDao).execute();
    }

    public Organization getOrganizationById (String id) {
        return mOrganizationDao.getOrganizationById(id);
    }

    public List<Schedule> getSchedulesForOrganization (String orgId){
        return mScheduleDao.getSchedulesForOrganization(orgId);
    }

    public Team getTeamWithTeamName (String teamName) {
        return mTeamDao.getTeamWithTeamName(teamName);
    }

    public LiveData<List<BattingLine>> getAllBattingLines () {
        return mBattingLines;
    }

    public List<BattingLine> getBattingLinesForBoxScore (String boxScoreId) {
        return mBattingLineDao.findBattingLinesForBoxScore(boxScoreId);
    }

    public void insertBattingLine (BattingLine battingLine) {
        new insertBattingLineAsyncTask(mBattingLineDao).execute(battingLine);
    }

    private static class insertBattingLineAsyncTask extends AsyncTask<BattingLine, Void, Void> {

        private BattingLineDao mAsyncTaskBattingLineDao;

        insertBattingLineAsyncTask(BattingLineDao battingLineDao) {
            mAsyncTaskBattingLineDao = battingLineDao;
        }

        @Override
        protected Void doInBackground(final BattingLine... battingLines) {
            mAsyncTaskBattingLineDao.insert(battingLines[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllBattingLines (List<BattingLine> battingLines) {
        new insertAllBattingLinesAsyncTask(mBattingLineDao).execute(battingLines);
    }

    private static class insertAllBattingLinesAsyncTask extends AsyncTask<List<BattingLine>, Void, Void> {

        private BattingLineDao mAsyncTaskBattingLineDao;

        insertAllBattingLinesAsyncTask(BattingLineDao battingLineDao) {
            mAsyncTaskBattingLineDao = battingLineDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<BattingLine>... battingLinesLists) {
            mAsyncTaskBattingLineDao.insertAll(battingLinesLists[0]);
            return null;
        }

    }

    public void updateBattingLine (BattingLine battingLine) {
        new updateBattingLineAsyncTask(mBattingLineDao).execute(battingLine);
    }

    private static class updateBattingLineAsyncTask extends AsyncTask<BattingLine, Void, Void>{
        private BattingLineDao mAsyncTaskBattingLineDao;

        updateBattingLineAsyncTask(BattingLineDao mBattingLineDao) {
            mAsyncTaskBattingLineDao = mBattingLineDao;
        }

        @Override
        protected Void doInBackground(BattingLine... battingLines) {
            mAsyncTaskBattingLineDao.update(battingLines[0]);
            return null;
        }
    }


    public LiveData<List<Organization>> getAllOrganizations () {
        return mOrganizations;
    }

    public Organization[] getAnyOrganization() {
        return mOrganizationDao.getAnyOrganization();
    }

    public void insertOrganization (Organization Organization) {
        new insertOrganizationAsyncTask(mOrganizationDao).execute(Organization);
    }

    private static class insertOrganizationAsyncTask extends AsyncTask<Organization, Void, Void> {

        private OrganizationDao mAsyncTaskOrganizationDao;

        insertOrganizationAsyncTask(OrganizationDao OrganizationDao) {
            mAsyncTaskOrganizationDao = OrganizationDao;
        }

        @Override
        protected Void doInBackground(final Organization... Organizations) {
            mAsyncTaskOrganizationDao.insert(Organizations[0]);
            return null;
        }
    }



    public void updateOrganization (Organization Organization) {
        new updateOrganizationAsyncTask(mOrganizationDao).execute(Organization);
    }

    private static class updateOrganizationAsyncTask extends AsyncTask<Organization, Void, Void>{
        private OrganizationDao mAsyncTaskOrganizationDao;

        updateOrganizationAsyncTask(OrganizationDao mOrganizationDao) {
            mAsyncTaskOrganizationDao = mOrganizationDao;
        }

        @Override
        protected Void doInBackground(Organization... Organizations) {
            mAsyncTaskOrganizationDao.update(Organizations[0]);
            return null;
        }
    }


    public LiveData<List<Schedule>> getAllSchedules () {
        return mSchedules;
    }

    public void insertSchedule (Schedule schedule) {
        new insertScheduleAsyncTask(mScheduleDao).execute(schedule);
    }

    private static class insertScheduleAsyncTask extends AsyncTask<Schedule, Void, Void> {

        private ScheduleDao mAsyncTaskScheduleDao;

        insertScheduleAsyncTask(ScheduleDao ScheduleDao) {
            mAsyncTaskScheduleDao = ScheduleDao;
        }

        @Override
        protected Void doInBackground(final Schedule... Schedules) {
            mAsyncTaskScheduleDao.insert(Schedules[0]);
            return null;
        }
    }



    public void updateSchedule (Schedule schedule) {
        new updateScheduleAsyncTask(mScheduleDao).execute(schedule);
    }

    private static class updateScheduleAsyncTask extends AsyncTask<Schedule, Void, Void>{
        private ScheduleDao mAsyncTaskScheduleDao;

        updateScheduleAsyncTask(ScheduleDao mScheduleDao) {
            mAsyncTaskScheduleDao = mScheduleDao;
        }

        @Override
        protected Void doInBackground(Schedule... schedules) {
            mAsyncTaskScheduleDao.update(schedules[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllSchedules (List<Schedule> schedules) {
        new insertAllSchedulesAsyncTask(mScheduleDao).execute(schedules);
    }

    private static class insertAllSchedulesAsyncTask extends AsyncTask<List<Schedule>, Void, Void> {

        private ScheduleDao mAsyncTaskScheduleDao;

        insertAllSchedulesAsyncTask(ScheduleDao scheduleDao) {
            mAsyncTaskScheduleDao = scheduleDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<Schedule>... schedulesLists) {
            mAsyncTaskScheduleDao.insertAll(schedulesLists[0]);
            return null;
        }

    }


    public LiveData<List<Game>> getAllGames () {
        return mGames;
    }
    
    public List<Game> getGamesForSchedule(String scheduleId) {
        return mGameDao.findGamesForSchedule(scheduleId); 
    }
    
    public List<Game> getGamesForDay (int day) {
        return mGameDao.findGamesForDay(day);
    }

    public void insertGame (Game game) {
        new insertGameAsyncTask(mGameDao).execute(game);
    }

    private static class insertGameAsyncTask extends AsyncTask<Game, Void, Void> {

        private GameDao mAsyncTaskGameDao;

        insertGameAsyncTask(GameDao GameDao) {
            mAsyncTaskGameDao = GameDao;
        }

        @Override
        protected Void doInBackground(final Game... Games) {
            mAsyncTaskGameDao.insert(Games[0]);
            return null;
        }
    }



    public void updateGame (Game game) {
        new updateGameAsyncTask(mGameDao).execute(game);
    }

    private static class updateGameAsyncTask extends AsyncTask<Game, Void, Void>{
        private GameDao mAsyncTaskGameDao;

        updateGameAsyncTask(GameDao mGameDao) {
            mAsyncTaskGameDao = mGameDao;
        }

        @Override
        protected Void doInBackground(Game... games) {
            mAsyncTaskGameDao.update(games[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllGames (List<Game> games) {
        new insertAllGamesAsyncTask(mGameDao).execute(games);
    }

    private static class insertAllGamesAsyncTask extends AsyncTask<List<Game>, Void, Void> {

        private GameDao mAsyncTaskGameDao;

        insertAllGamesAsyncTask(GameDao gameDao) {
            mAsyncTaskGameDao = gameDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<Game>... gamesLists) {
            mAsyncTaskGameDao.insertAll(gamesLists[0]);
            return null;
        }

    }


    public LiveData<List<BoxScore>> getAllBoxScores () {
        return mBoxScore;
    }

    public List<BoxScore> getBoxScoresForGame(String gameId) {
        return mBoxScoreDao.findBoxScoresForGame(gameId);
    }

    public void insertBoxScore (BoxScore boxScore) {
        new insertBoxScoreAsyncTask(mBoxScoreDao).execute(boxScore);
    }

    private static class insertBoxScoreAsyncTask extends AsyncTask<BoxScore, Void, Void> {

        private BoxScoreDao mAsyncTaskBoxScoreDao;

        insertBoxScoreAsyncTask(BoxScoreDao BoxScoreDao) {
            mAsyncTaskBoxScoreDao = BoxScoreDao;
        }

        @Override
        protected Void doInBackground(final BoxScore... BoxScores) {
            mAsyncTaskBoxScoreDao.insert(BoxScores[0]);
            return null;
        }
    }
    
    public void updateBoxScore (BoxScore boxScore) {
        new updateBoxScoreAsyncTask(mBoxScoreDao).execute(boxScore);
    }

    private static class updateBoxScoreAsyncTask extends AsyncTask<BoxScore, Void, Void>{
        private BoxScoreDao mAsyncTaskBoxScoreDao;

        updateBoxScoreAsyncTask(BoxScoreDao mBoxScoreDao) {
            mAsyncTaskBoxScoreDao = mBoxScoreDao;
        }

        @Override
        protected Void doInBackground(BoxScore... boxScores) {
            mAsyncTaskBoxScoreDao.update(boxScores[0]);
            return null;
        }
    }

    public LiveData<List<PitchingLine>> getAllPitchingLines () {
        return mPitchingLines;
    }

    public List<PitchingLine> getPitchingLinesForBoxScore (String boxScoreId) {
        return mPitchingLineDao.findPitchingLinesForBoxScore(boxScoreId);
    }

    public void insertPitchingLine (PitchingLine pitchingLine) {
        new insertPitchingLineAsyncTask(mPitchingLineDao).execute(pitchingLine);
    }

    private static class insertPitchingLineAsyncTask extends AsyncTask<PitchingLine, Void, Void> {

        private PitchingLineDao mAsyncTaskPitchingLineDao;

        insertPitchingLineAsyncTask(PitchingLineDao PitchingLineDao) {
            mAsyncTaskPitchingLineDao = PitchingLineDao;
        }

        @Override
        protected Void doInBackground(final PitchingLine... PitchingLines) {
            mAsyncTaskPitchingLineDao.insert(PitchingLines[0]);
            return null;
        }
    }



    public void updatePitchingLine (PitchingLine pitchingLine) {
        new updatePitchingLineAsyncTask(mPitchingLineDao).execute(pitchingLine);
    }

    private static class updatePitchingLineAsyncTask extends AsyncTask<PitchingLine, Void, Void>{
        private PitchingLineDao mAsyncTaskPitchingLineDao;

        updatePitchingLineAsyncTask(PitchingLineDao mPitchingLineDao) {
            mAsyncTaskPitchingLineDao = mPitchingLineDao;
        }

        @Override
        protected Void doInBackground(PitchingLine... pitchingLines) {
            mAsyncTaskPitchingLineDao.update(pitchingLines[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllPitchingLines (List<PitchingLine> pitchingLines) {
        new insertAllPitchingLinesAsyncTask(mPitchingLineDao).execute(pitchingLines);
    }

    private static class insertAllPitchingLinesAsyncTask extends AsyncTask<List<PitchingLine>, Void, Void> {

        private PitchingLineDao mAsyncTaskPitchingLineDao;

        insertAllPitchingLinesAsyncTask(PitchingLineDao pitchingLineDao) {
            mAsyncTaskPitchingLineDao = pitchingLineDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<PitchingLine>... pitchingLinesLists) {
            mAsyncTaskPitchingLineDao.insertAll(pitchingLinesLists[0]);
            return null;
        }

    }

    public LiveData<List<League>> getAllLeagues () {
        return mLeagues;
    }

    public List<League> getLeaguesForOrganization (String orgId) {
        return mLeagueDao.findLeaguesForOrganization(orgId);
    }

    public void insertLeague (League league) {
        new insertLeagueAsyncTask(mLeagueDao).execute(league);
    }

    private static class insertLeagueAsyncTask extends AsyncTask<League, Void, Void> {

        private LeagueDao mAsyncTaskLeagueDao;

        insertLeagueAsyncTask(LeagueDao LeagueDao) {
            mAsyncTaskLeagueDao = LeagueDao;
        }

        @Override
        protected Void doInBackground(final League... Leagues) {
            mAsyncTaskLeagueDao.insert(Leagues[0]);
            return null;
        }
    }



    public void updateLeague (League league) {
        new updateLeagueAsyncTask(mLeagueDao).execute(league);
    }

    private static class updateLeagueAsyncTask extends AsyncTask<League, Void, Void>{
        private LeagueDao mAsyncTaskLeagueDao;

        updateLeagueAsyncTask(LeagueDao mLeagueDao) {
            mAsyncTaskLeagueDao = mLeagueDao;
        }

        @Override
        protected Void doInBackground(League... leagues) {
            mAsyncTaskLeagueDao.update(leagues[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllLeagues (List<League> leagues) {
        new insertAllLeaguesAsyncTask(mLeagueDao).execute(leagues);
    }

    private static class insertAllLeaguesAsyncTask extends AsyncTask<List<League>, Void, Void> {

        private LeagueDao mAsyncTaskLeagueDao;

        insertAllLeaguesAsyncTask(LeagueDao leagueDao) {
            mAsyncTaskLeagueDao = leagueDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<League>... leaguesLists) {
            mAsyncTaskLeagueDao.insertAll(leaguesLists[0]);
            return null;
        }

    }

    public LiveData<List<Division>> getAllDivisions () {
        return mDivisions;
    }

    public List<Division> getDivisionsForLeague(String leagueId) {
        return mDivisionDao.findDivisionsForLeague(leagueId);
    }

    public void insertDivision (Division division) {
        new insertDivisionAsyncTask(mDivisionDao).execute(division);
    }

    private static class insertDivisionAsyncTask extends AsyncTask<Division, Void, Void> {

        private DivisionDao mAsyncTaskDivisionDao;

        insertDivisionAsyncTask(DivisionDao DivisionDao) {
            mAsyncTaskDivisionDao = DivisionDao;
        }

        @Override
        protected Void doInBackground(final Division... Divisions) {
            mAsyncTaskDivisionDao.insert(Divisions[0]);
            return null;
        }
    }



    public void updateDivision (Division division) {
        new updateDivisionAsyncTask(mDivisionDao).execute(division);
    }

    private static class updateDivisionAsyncTask extends AsyncTask<Division, Void, Void>{
        private DivisionDao mAsyncTaskDivisionDao;

        updateDivisionAsyncTask(DivisionDao mDivisionDao) {
            mAsyncTaskDivisionDao = mDivisionDao;
        }

        @Override
        protected Void doInBackground(Division... divisions) {
            mAsyncTaskDivisionDao.update(divisions[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllDivisions (List<Division> divisions) {
        new insertAllDivisionsAsyncTask(mDivisionDao).execute(divisions);
    }

    private static class insertAllDivisionsAsyncTask extends AsyncTask<List<Division>, Void, Void> {

        private DivisionDao mAsyncTaskDivisionDao;

        insertAllDivisionsAsyncTask(DivisionDao divisionDao) {
            mAsyncTaskDivisionDao = divisionDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<Division>... divisionsLists) {
            mAsyncTaskDivisionDao.insertAll(divisionsLists[0]);
            return null;
        }

    }

    public LiveData<List<Team>> getAllTeams () {
        return mTeams;
    }

    public List<Team> getTeamsForDivision(String divisionId) {
        return mTeamDao.findTeamsForDivision(divisionId);
    }

    public void insertTeam (Team team) {
        new insertTeamAsyncTask(mTeamDao).execute(team);
    }

    private static class insertTeamAsyncTask extends AsyncTask<Team, Void, Void> {

        private TeamDao mAsyncTaskTeamDao;

        insertTeamAsyncTask(TeamDao TeamDao) {
            mAsyncTaskTeamDao = TeamDao;
        }

        @Override
        protected Void doInBackground(final Team... Teams) {
            mAsyncTaskTeamDao.insert(Teams[0]);
            return null;
        }
    }



    public void updateTeam (Team team) {
        new updateTeamAsyncTask(mTeamDao).execute(team);
    }

    private static class updateTeamAsyncTask extends AsyncTask<Team, Void, Void>{
        private TeamDao mAsyncTaskTeamDao;

        updateTeamAsyncTask(TeamDao mTeamDao) {
            mAsyncTaskTeamDao = mTeamDao;
        }

        @Override
        protected Void doInBackground(Team... teams) {
            mAsyncTaskTeamDao.update(teams[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllTeams (List<Team> teams) {
        new insertAllTeamsAsyncTask(mTeamDao).execute(teams);
    }

    private static class insertAllTeamsAsyncTask extends AsyncTask<List<Team>, Void, Void> {

        private TeamDao mAsyncTaskTeamDao;

        insertAllTeamsAsyncTask(TeamDao teamDao) {
            mAsyncTaskTeamDao = teamDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<Team>... teamsLists) {
            mAsyncTaskTeamDao.insertAll(teamsLists[0]);
            return null;
        }

    }

    public LiveData<List<Player>> getAllPlayers () {
        return mPlayers;
    }

    public List<Player> getPlayersForTeam(String teamId) {
        return mPlayersDao.findPlayersForTeam(teamId);
    }

    public void insertPlayer (Player player) {
        new insertPlayerAsyncTask(mPlayersDao).execute(player);
    }

    private static class insertPlayerAsyncTask extends AsyncTask<Player, Void, Void> {

        private PlayersDao mAsyncTaskPlayerDao;

        insertPlayerAsyncTask(PlayersDao PlayerDao) {
            mAsyncTaskPlayerDao = PlayerDao;
        }

        @Override
        protected Void doInBackground(final Player... Players) {
            mAsyncTaskPlayerDao.insert(Players[0]);
            return null;
        }
    }



    public void updatePlayer (Player player) {
        new updatePlayerAsyncTask(mPlayersDao).execute(player);
    }

    private static class updatePlayerAsyncTask extends AsyncTask<Player, Void, Void>{
        private PlayersDao mAsyncTaskPlayerDao;

        updatePlayerAsyncTask(PlayersDao mPlayersDao) {
            mAsyncTaskPlayerDao = mPlayersDao;
        }

        @Override
        protected Void doInBackground(Player... players) {
            mAsyncTaskPlayerDao.update(players[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllPlayers (List<Player> players) {
        new insertAllPlayersAsyncTask(mPlayersDao).execute(players);
    }

    private static class insertAllPlayersAsyncTask extends AsyncTask<List<Player>, Void, Void> {

        private PlayersDao mAsyncTaskPlayerDao;

        insertAllPlayersAsyncTask(PlayersDao playerDao) {
            mAsyncTaskPlayerDao = playerDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<Player>... playersLists) {
            mAsyncTaskPlayerDao.insertAll(playersLists[0]);
            return null;
        }

    }


    public LiveData<List<BattingStats>> getAllBattingStats () {
        return mBattingStats;
    }

    public List<BattingStats> getBattingStatsForPlayer(String playerId) {
        return mBattingStatsDao.findBattingStatsForPlayer(playerId);
    }

    public void insertBattingStats (BattingStats game) {
        new insertBattingStatsAsyncTask(mBattingStatsDao).execute(game);
    }

    private static class insertBattingStatsAsyncTask extends AsyncTask<BattingStats, Void, Void> {

        private BattingStatsDao mAsyncTaskBattingStatsDao;

        insertBattingStatsAsyncTask(BattingStatsDao BattingStatsDao) {
            mAsyncTaskBattingStatsDao = BattingStatsDao;
        }

        @Override
        protected Void doInBackground(final BattingStats... BattingStats) {
            mAsyncTaskBattingStatsDao.insert(BattingStats[0]);
            return null;
        }
    }



    public void updateBattingStats (BattingStats game) {
        new updateBattingStatsAsyncTask(mBattingStatsDao).execute(game);
    }

    private static class updateBattingStatsAsyncTask extends AsyncTask<BattingStats, Void, Void>{
        private BattingStatsDao mAsyncTaskBattingStatsDao;

        updateBattingStatsAsyncTask(BattingStatsDao mBattingStatsDao) {
            mAsyncTaskBattingStatsDao = mBattingStatsDao;
        }

        @Override
        protected Void doInBackground(BattingStats... games) {
            mAsyncTaskBattingStatsDao.update(games[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllBattingStats (List<BattingStats> games) {
        new insertAllBattingStatsAsyncTask(mBattingStatsDao).execute(games);
    }

    private static class insertAllBattingStatsAsyncTask extends AsyncTask<List<BattingStats>, Void, Void> {

        private BattingStatsDao mAsyncTaskBattingStatsDao;

        insertAllBattingStatsAsyncTask(BattingStatsDao gameDao) {
            mAsyncTaskBattingStatsDao = gameDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<BattingStats>... gamesLists) {
            mAsyncTaskBattingStatsDao.insertAll(gamesLists[0]);
            return null;
        }

    }

    public LiveData<List<PitchingStats>> getAllPitchingStats () {
        return mPitchingStats;
    }

    public List<PitchingStats> getPitchingStatsForPlayer(String playerId) {
        return mPitchingStatsDao.findPitchingStatsForPlayer(playerId);
    }

    public void insertPitchingStats (PitchingStats game) {
        new insertPitchingStatsAsyncTask(mPitchingStatsDao).execute(game);
    }

    private static class insertPitchingStatsAsyncTask extends AsyncTask<PitchingStats, Void, Void> {

        private PitchingStatsDao mAsyncTaskPitchingStatsDao;

        insertPitchingStatsAsyncTask(PitchingStatsDao PitchingStatsDao) {
            mAsyncTaskPitchingStatsDao = PitchingStatsDao;
        }

        @Override
        protected Void doInBackground(final PitchingStats... PitchingStats) {
            mAsyncTaskPitchingStatsDao.insert(PitchingStats[0]);
            return null;
        }
    }



    public void updatePitchingStats (PitchingStats game) {
        new updatePitchingStatsAsyncTask(mPitchingStatsDao).execute(game);
    }

    private static class updatePitchingStatsAsyncTask extends AsyncTask<PitchingStats, Void, Void>{
        private PitchingStatsDao mAsyncTaskPitchingStatsDao;

        updatePitchingStatsAsyncTask(PitchingStatsDao mPitchingStatsDao) {
            mAsyncTaskPitchingStatsDao = mPitchingStatsDao;
        }

        @Override
        protected Void doInBackground(PitchingStats... games) {
            mAsyncTaskPitchingStatsDao.update(games[0]);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAllPitchingStats (List<PitchingStats> games) {
        new insertAllPitchingStatsAsyncTask(mPitchingStatsDao).execute(games);
    }

    private static class insertAllPitchingStatsAsyncTask extends AsyncTask<List<PitchingStats>, Void, Void> {

        private PitchingStatsDao mAsyncTaskPitchingStatsDao;

        insertAllPitchingStatsAsyncTask(PitchingStatsDao gameDao) {
            mAsyncTaskPitchingStatsDao = gameDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<PitchingStats>... gamesLists) {
            mAsyncTaskPitchingStatsDao.insertAll(gamesLists[0]);
            return null;
        }

    }


    private static class deleteAllOrganizationsAsyncTask extends AsyncTask<Void, Void, Void> {
        private OrganizationDao mAsyncTaskDao;

        deleteAllOrganizationsAsyncTask(OrganizationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


}
