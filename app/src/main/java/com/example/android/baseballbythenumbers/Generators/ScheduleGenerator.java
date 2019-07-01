package com.example.android.baseballbythenumbers.Generators;

import android.widget.ProgressBar;

import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleGenerator {

    private Organization organization;
    private ProgressBar progressBar;
    private int progress;

    public ScheduleGenerator(Organization organization){
        this.organization = organization;
    }

    public Schedule generateSchedule(int seriesLength, boolean interLeaguePlay, ProgressBar generateOrgProgressbar){
        progressBar = generateOrgProgressbar;
        progress = progressBar.getProgress();
        Schedule newSchedule = new Schedule(organization.getId(), null);
        newSchedule.setGameList(generateListOfGames(seriesLength, interLeaguePlay, newSchedule.getScheduleId()));
        return newSchedule;
    }

    private List<Game> generateListOfGames(int seriesLength, boolean interLeaguePlay, String scheduleId) {
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
        Collections.sort(gameList);
        return gameList;
    }

    private List<Game> gameListWithEvenNumberOfTeams(int seriesLength, List<Team> teamList, String scheduleId) {
        List<Game> gameList = new ArrayList<>();
        List<Team> homeList = new ArrayList<>();
        List<Team> visitorList = new ArrayList<>();
        for (int i = 0; i < teamList.size()/2; i++) {
            homeList.add(teamList.get(i));
            visitorList.add(teamList.get(i+(teamList.size()/2)));
        }

        for (int i = 0; i < teamList.size()-1; i++) {
            for (int j = 0; j < homeList.size(); j++) {
                if (homeList.get(j) != null && visitorList.get(j) != null) {
                    gameList.add(createNewGame(scheduleId, i, homeList.get(j).getTeamName(), visitorList.get(j).getTeamName()));
                    progress++;
                    if (progressBar != null) {
                        progressBar.setProgress(progress);
                    }
                }
            }
            rotateTeams(homeList, visitorList);
        }
        List<Game> reverseGameList = new ArrayList<>();
        for (Game game: gameList) {
            reverseGameList.add(createNewGame(scheduleId, (game.getDay()+teamList.size()-1), game.getVisitingTeamName(), game.getHomeTeamName()));
            progress++;
            if (progressBar != null) {
                progressBar.setProgress(progress);
            }
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
                gamesToAdd.add(createNewGame(gameList.get(i).getScheduleId(),gameList.get(i).getDay() + j, gameList.get(i).getHomeTeamName(), gameList.get(i).getVisitingTeamName()));   // copy the game from the list at i position
                progress++;
                if (progressBar != null) {
                    progressBar.setProgress(progress);
                }
            }
        }
        gameList.addAll(gamesToAdd);  // add all the copied games with adjusted dates to the list
    }

    private void rotateTeams(List<Team> homeList, List<Team> visitorList) {
        Team teamToMoveFromVisitorList = visitorList.get(0);
        Team teamToMoveFromHomeList = homeList.get(homeList.size()-1);
        visitorList.remove(teamToMoveFromVisitorList);
        homeList.add(1, teamToMoveFromVisitorList);
        homeList.remove(teamToMoveFromHomeList);
        visitorList.add(teamToMoveFromHomeList);
    }

    private Game createNewGame(String scheduleId, int day, String homeTeamId, String visitingTeamId) {
        Game newGame = new Game(scheduleId);
        newGame.setDay(day);
        newGame.setHomeTeamName(homeTeamId);
        newGame.setVisitingTeamName(visitingTeamId);
        return newGame;
    }

    private List<Game> gameListWithOddNumberOfTeams(int seriesLength, List<Team> teamList, String scheduleId) {
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
