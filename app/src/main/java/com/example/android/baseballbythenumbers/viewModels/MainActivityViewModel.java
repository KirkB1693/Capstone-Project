package com.example.android.baseballbythenumbers.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.android.baseballbythenumbers.AppExecutors;
import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.ResourceProvider;
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
import com.example.android.baseballbythenumbers.repository.Repository;
import com.example.android.baseballbythenumbers.simulators.GameSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Future;

import timber.log.Timber;

public class MainActivityViewModel extends AndroidViewModel {

    private final Repository mRepository;
    private Organization organization;
    private List<Future<?>> futureList;
    private TreeMap<String, Team> mapOfAllTeams;
    private TreeMap<Integer, List<Game>> listOfAllGamesByDay;
    private Team usersTeam;
    private Team homeTeam;
    private Team visitingTeam;
    private Boolean teamsAreLoaded;
    private Boolean orgLoaded;
    private Boolean gamesAreLoaded;
    private ResourceProvider resourceProvider;
    private List<Game> listOfGamesToUpdate;
    private List<BoxScore> listOfBoxScoresToUpdate;
    private List<BattingLine> listOfBattingLinesToInsert;
    private List<PitchingLine> listOfPitchingLinesToInsert;
    private List<Team> listOfTeamsToUpdate;
    private List<Player> listOfPlayersToUpdate;
    private List<BattingStats> listOfBattingStatsToUpdate;
    private List<PitchingStats> listOfPitchingStatsToUpdate;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((BaseballByTheNumbersApp) application).getRepository();
        resourceProvider = ((BaseballByTheNumbersApp) application).getResourceProvider();
        futureList = new ArrayList<>();
        listOfGamesToUpdate = new ArrayList<>();
        listOfBoxScoresToUpdate = new ArrayList<>();
        listOfBattingLinesToInsert = new ArrayList<>();
        listOfPitchingLinesToInsert = new ArrayList<>();
        listOfTeamsToUpdate = new ArrayList<>();
        listOfPlayersToUpdate = new ArrayList<>();
        listOfBattingStatsToUpdate = new ArrayList<>();
        listOfPitchingStatsToUpdate = new ArrayList<>();
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public List<Future<?>> getFutureList() {
        return futureList;
    }

    public Team getVisitingTeam() {
        return visitingTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getUsersTeam() {
        return usersTeam;
    }

    public Boolean isOrganizationLoaded (){
        return teamsAreLoaded && orgLoaded && gamesAreLoaded;
    }

    public TreeMap<Integer, List<Game>> getListOfAllGamesByDay() {
        return listOfAllGamesByDay;
    }

    public TreeMap<String, Team> getMapOfAllTeams() {
        return mapOfAllTeams;
    }

    public List<Game> getListOfGamesPlayed() {
        return listOfGamesToUpdate;
    }

    public void simAllGamesForDay(int dayOfSchedule) {
        if (teamsAreLoaded && gamesAreLoaded) {
            listOfGamesToUpdate.clear();
            listOfBoxScoresToUpdate.clear();
            listOfBattingLinesToInsert.clear();
            listOfPitchingLinesToInsert.clear();
            listOfTeamsToUpdate.clear();
            listOfPlayersToUpdate.clear();
            listOfBattingStatsToUpdate.clear();
            listOfPitchingStatsToUpdate.clear();
            List<Game> gameList;
            Team homeTeam;
            Team visitingTeam;
            gameList = getListOfAllGamesByDay().get(dayOfSchedule);
            if (gameList != null) {
                for (Game game : gameList) {
                    if (!game.isPlayedGame()) {
                        homeTeam = mapOfAllTeams.get(game.getHomeTeamName());
                        visitingTeam = mapOfAllTeams.get((game.getVisitingTeamName()));
                        simGame(game, homeTeam, visitingTeam);
                    }
                }
            }
            saveStatsFromGamesSimmed();
        } else {
            Timber.e("Error teams and games should be loaded but aren't!!!");
        }
    }

    private void saveStatsFromGamesSimmed() {
        mRepository.updateAllGames(listOfGamesToUpdate);
        mRepository.updateAllBoxScores(listOfBoxScoresToUpdate);
        mRepository.insertAllBattingLines(listOfBattingLinesToInsert);
        mRepository.insertAllPitchingLines(listOfPitchingLinesToInsert);
        mRepository.updateAllTeams(listOfTeamsToUpdate);
        mRepository.updateAllPlayers(listOfPlayersToUpdate);
        mRepository.updateAllBattingStats(listOfBattingStatsToUpdate);
        mRepository.updateAllPitchingStats(listOfPitchingStatsToUpdate);
    }

    public void simGame(final Game game, final Team homeTeam, final Team visitingTeam) {
        GameSimulator gameSimulator = new GameSimulator(resourceProvider, game, homeTeam, false, visitingTeam, false, organization.getCurrentYear(), mRepository);
        int[] result = gameSimulator.simulateGame();

        game.setHomeScore(result[0]);
        game.setVisitorScore(result[1]);
        game.setPlayedGame(true);

        listOfGamesToUpdate.add(game);
        listOfBoxScoresToUpdate.add(game.getHomeBoxScore());
        listOfBoxScoresToUpdate.add(game.getVisitorBoxScore());
        listOfBattingLinesToInsert.addAll(game.getHomeBoxScore().getBattingLines());
        listOfBattingLinesToInsert.addAll(game.getVisitorBoxScore().getBattingLines());
        listOfPitchingLinesToInsert.addAll(game.getHomeBoxScore().getPitchingLines());
        listOfPitchingLinesToInsert.addAll(game.getVisitorBoxScore().getPitchingLines());
        listOfTeamsToUpdate.add(homeTeam);
        listOfTeamsToUpdate.add(visitingTeam);
        listOfPlayersToUpdate.addAll(homeTeam.getPlayers());
        listOfPlayersToUpdate.addAll(visitingTeam.getPlayers());
        for (Player player : listOfPlayersToUpdate) {
            listOfBattingStatsToUpdate.add(player.getBattingStats().get(organization.getCurrentYear()));
            listOfPitchingStatsToUpdate.add(player.getPitchingStats().get(organization.getCurrentYear()));
        }

    }

    public void setNewListOfAllGamesByDay(List<Game> gameList) {
        if (gameList == null) {
            Timber.e("Error!!!, Tried to setNewListOfAllGamesByDay with a null list of games!!!");
            return;
        }
        Collections.sort(gameList);
        int numOfDaysInSchedule = gameList.get(gameList.size() - 1).getDay() + 1;
        TreeMap<Integer, List<Game>> gamesTreeMap = new TreeMap<>();
        for (int i = 0; i < numOfDaysInSchedule; i++) {
            List<Game> gamesForDay = new ArrayList<>();
            for (Game game : gameList) {
                if (game.getDay() == i) {
                    gamesForDay.add(game);
                }
            }
            gamesTreeMap.put(i, gamesForDay);
        }
        listOfAllGamesByDay = gamesTreeMap;
    }

    public Game findNextUnplayedGame(List<Game> gameListForUserTeam) {
        Collections.sort(gameListForUserTeam);
        for (Game game : gameListForUserTeam) {
            if (!game.isPlayedGame()) {
                if (teamsAreLoaded) {
                    homeTeam = mapOfAllTeams.get(game.getHomeTeamName());
                    visitingTeam = mapOfAllTeams.get(game.getVisitingTeamName());
                }
                return game;
            }
        }
        return null;
    }

    public Game findLastUserGamePlayed(List<Game> gameListForUsersTeam) {
        Collections.sort(gameListForUsersTeam);
        Game lastPlayedGame = null;
        for (Game game : gameListForUsersTeam) {
            if (!game.isPlayedGame()) {
                return lastPlayedGame;
            } else {
                lastPlayedGame = game;
            }
        }
        return lastPlayedGame;
    }

    public List<Game> generateListOfGamesForUser() {
        if (!isOrganizationLoaded()) {
            return null;
        }
        usersTeam = mapOfAllTeams.get(organization.getUserTeamName());
        List<Game> gameListForUser = new ArrayList<>();
        for (TreeMap.Entry<Integer, List<Game>> entry : listOfAllGamesByDay.entrySet()) {
            List<Game> gameListForDay = entry.getValue();
            if (gameListForDay != null) {
                for (Game game : gameListForDay) {
                    if (game.getHomeTeamName().equals(usersTeam.getTeamName()) || game.getVisitingTeamName().equals(usersTeam.getTeamName())) {
                        gameListForUser.add(game);
                    }
                }
            }

        }
        return gameListForUser;
    }

    public void loadOrganizationData(final String orgId) {
        Future<?> future = AppExecutors.getInstance().diskIO().submit(new Runnable() {
            @Override
            public void run() {
                organization = mRepository.getOrganizationById(orgId);
                List<Schedule> schedules = mRepository.getSchedulesForOrganization(orgId);
                organization.setSchedules(schedules);
                List<League> leagueList = mRepository.getLeaguesForOrganization(orgId);
                organization.setLeagues(leagueList);
                List<Division> divisionList = new ArrayList<>();
                for (League league : leagueList) {
                    league.setDivisions(mRepository.getDivisionsForLeague(league.getLeagueId()));
                    divisionList.addAll(league.getDivisions());
                }
                List<Team> teamList = new ArrayList<>();
                for (Division division : divisionList) {
                    teamList.addAll(mRepository.getTeamsForDivision(division.getDivisionId()));
                }
                List<Player> playerList = new ArrayList<>();
                for (Team team : teamList) {
                    team.setPlayers(mRepository.getPlayersForTeam(team.getTeamId()));
                    playerList.addAll(team.getPlayers());
                }
                for (Player player : playerList) {
                    List<BattingStats> battingStatsList = mRepository.getBattingStatsForPlayer(player.getPlayerId());
                    Collections.sort(battingStatsList);
                    player.setBattingStats(battingStatsList);
                    List<PitchingStats> pitchingStatsList = mRepository.getPitchingStatsForPlayer(player.getPlayerId());
                    Collections.sort(pitchingStatsList);
                    player.setPitchingStats(pitchingStatsList);
                }
                TreeMap<String, Team> teamTreeMap = new TreeMap<>();
                if (!teamList.isEmpty()) {
                    for (Team team : teamList) {
                        teamTreeMap.put(team.getTeamName(), team);
                    }
                    mapOfAllTeams = teamTreeMap;
                    teamsAreLoaded = true;
                    orgLoaded = true;
                }
                for (Division division : divisionList) {
                    List<Team> teamListForDiv = new ArrayList<>();
                    for (TreeMap.Entry<String, Team> entry : mapOfAllTeams.entrySet()) {
                        Team team = entry.getValue();
                        if (division.getDivisionId().equals(team.getDivisionId())) {
                            teamListForDiv.add(team);
                        }
                    }
                    division.setTeams(teamListForDiv);
                }

                List<Game> gameList = mRepository.getGamesForSchedule(organization.getScheduleForCurrentYear().getScheduleId());
                if (gameList != null) {
                    Collections.sort(gameList);
                    int numOfDaysInSchedule = gameList.get(gameList.size() - 1).getDay() + 1;
                    TreeMap<Integer, List<Game>> gamesTreeMap = new TreeMap<>();
                    for (int i = 0; i < numOfDaysInSchedule; i++) {
                        List<Game> gamesForDay = new ArrayList<>();
                        for (Game game : gameList) {
                            if (game.getDay() == i) {
                                gamesForDay.add(game);
                            }
                        }
                        gamesTreeMap.put(i, gamesForDay);
                    }
                    listOfAllGamesByDay = gamesTreeMap;
                    gamesAreLoaded = true;
                }
            }
        });
        futureList.add(future);
    }
}
