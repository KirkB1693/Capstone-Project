package com.example.android.baseballbythenumbers.Generators;

import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.List;

public class ScheduleGenerator {

    private Organization organization;

    public ScheduleGenerator(Organization organization){
        this.organization = organization;
    }

    public Schedule generateSchedule(int seriesLength, boolean interLeaguePlay){
        Schedule newSchedule = new Schedule(organization.getId(), null);
        newSchedule.setGameList(generateListOfGames(seriesLength, interLeaguePlay, newSchedule.getScheduleId()));
        return newSchedule;
    }

    private List<Game> generateListOfGames(int seriesLength, boolean interLeaguePlay, int scheduleId) {
        List<Team> teamList;
        List<Game> gameList = new ArrayList<>();
        if (interLeaguePlay) {
            teamList = getListOfAllTeams();
            if (teamList.size() % 2 == 0) {
                gameList = gameListWithEvenNumberOfTeams(seriesLength, teamList, scheduleId);
            } else {
                gameList = gameListWithOddNumberOfTeams(seriesLength, teamList, scheduleId);
            }
        } else {
            for (League league: organization.getLeagues()) {
                teamList = getListOfTeams(league);
                if (teamList.size() % 2 == 0) {
                    gameList.addAll(gameListWithEvenNumberOfTeams(seriesLength, teamList, scheduleId));
                } else {
                    gameList.addAll(gameListWithOddNumberOfTeams(seriesLength, teamList, scheduleId));
                }
            }
        }
        return gameList;
    }

    private List<Game> gameListWithEvenNumberOfTeams(int seriesLength, List<Team> teamList, int scheduleId) {
        List<Game> gameList = new ArrayList<>();
        List<Team> homeList = teamList.subList(0, (teamList.size()/2-1));
        List<Team> visitorList = teamList.subList(teamList.size()/2, teamList.size()-1);
        for (int i = 0; i < teamList.size()-1; i++) {
            for (int j = 0; j < teamList.size()/2; j++) {
                if (homeList.get(j) != null && visitorList.get(j) != null) {
                    gameList.add(createNewGame(scheduleId, i, homeList.get(j).getTeamId(), visitorList.get(j).getTeamId()));
                }
            }
            rotateTeams(homeList, visitorList);
        }
        List<Game> reverseGameList = new ArrayList<>();
        for (Game game: gameList) {
            reverseGameList.add(createNewGame(scheduleId, (game.getDay()+teamList.size()), game.getVisitingTeamId(), game.getHomeTeamId()));
        }
        gameList.addAll(reverseGameList);
        if (seriesLength > 1) {
            adjustGameListForSeriesLength(seriesLength, gameList);
        }
        return gameList;
    }

    private void adjustGameListForSeriesLength(int seriesLength, List<Game> gameList) {
        List<Game> gamesToAdd = new ArrayList<>();
        for (int i = 0; i < gameList.size() ; i++) {
            gameList.get(i).setDay(gameList.get(i).getDay()*seriesLength);  // change all existing game days by multiplying by series length so there are empty days to add games in.
            for (int j = 1; j < seriesLength; j++) {
                gamesToAdd.add(gameList.get(i));   // copy the game from the list at i position
                gamesToAdd.get(gamesToAdd.size()-1).setDay(gamesToAdd.get(gamesToAdd.size()-1).getDay() + j);   //change the day on the last game copied (i.e. last added to the list)
            }
            gameList.addAll(gamesToAdd);  // add all the copied games with adjusted dates to the list
            gamesToAdd = new ArrayList<>();
        }

    }

    private void rotateTeams(List<Team> homeList, List<Team> visitorList) {
        Team teamToMoveFromVisitorList = visitorList.get(0);
        Team teamToMoveFromHomeList = homeList.get(homeList.size()-1);
        visitorList.remove(teamToMoveFromVisitorList);
        homeList.add(1, teamToMoveFromVisitorList);
        homeList.remove(teamToMoveFromHomeList);
        visitorList.add(teamToMoveFromHomeList);
    }

    private Game createNewGame(int scheduleId, int day, int homeTeamId, int visitingTeamId) {
        Game newGame = new Game(scheduleId);
        newGame.setDay(day);
        newGame.setHomeTeamId(homeTeamId);
        newGame.setVisitingTeamId(visitingTeamId);
        return newGame;
    }

    private List<Game> gameListWithOddNumberOfTeams(int seriesLength, List<Team> teamList, int scheduleId) {
        Team nullTeam = null;
        teamList.add(nullTeam);
        return gameListWithEvenNumberOfTeams(seriesLength, teamList, scheduleId);
    }

    private List<Team> getListOfTeams(League league) {
        List<Team> teamList = new ArrayList<>();
            for (Division division: league.getDivisions()) {
                teamList.addAll(division.getTeams());
            }
        return teamList;
    }

    private List<Team> getListOfAllTeams() {
        List<Team> teamList = new ArrayList<>();
        for (League league: organization.getLeagues()) {
            for (Division division: league.getDivisions()) {
                teamList.addAll(division.getTeams());
                }
            }
        return teamList;
    }
}
