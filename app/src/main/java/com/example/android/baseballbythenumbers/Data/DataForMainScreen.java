package com.example.android.baseballbythenumbers.Data;

import java.util.List;

public class DataForMainScreen {
    
    private Organization mOrganization;
    private Team mUserTeam;
    private Team mHomeTeam;
    private Team mVisitingTeam;
    private Game mNextGame;
    private List<Game> mGamesForTeamList;
    
    public DataForMainScreen(Organization organization, Team userTeam, Team homeTeam, Team visitingTeam, Game nextGame, List<Game> gamesForTeamList) {
        mOrganization = organization;
        mUserTeam = userTeam;
        mHomeTeam = homeTeam;
        mVisitingTeam = visitingTeam;
        mNextGame = nextGame;
        mGamesForTeamList = gamesForTeamList;
    }

    public Organization getOrganization() {
        return mOrganization;
    }

    public void setOrganization(Organization mOrganization) {
        this.mOrganization = mOrganization;
    }

    public Team getHomeTeam() {
        return mHomeTeam;
    }

    public void setHomeTeam(Team mHomeTeam) {
        this.mHomeTeam = mHomeTeam;
    }

    public Team getUserTeam() {
        return mUserTeam;
    }

    public void setUserTeam(Team mUserTeam) {
        this.mUserTeam = mUserTeam;
    }

    public Team getVisitingTeam() {
        return mVisitingTeam;
    }

    public void setVisitingTeam(Team mVisitingTeam) {
        this.mVisitingTeam = mVisitingTeam;
    }

    public Game getNextGame() {
        return mNextGame;
    }

    public void setNextGame(Game mNextGame) {
        this.mNextGame = mNextGame;
    }

    public List<Game> getGamesForTeamList() {
        return mGamesForTeamList;
    }

    public void setGamesForTeamList(List<Game> mGamesForTeamList) {
        this.mGamesForTeamList = mGamesForTeamList;
    }
}
