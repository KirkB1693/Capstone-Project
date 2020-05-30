package com.example.android.baseballbythenumbers.viewModels;

import android.arch.lifecycle.ViewModel;

import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.repository.Repository;
import com.example.android.baseballbythenumbers.ResourceProvider;
import com.example.android.baseballbythenumbers.simulators.GameSimulator;

public class GamePlayViewModel extends ViewModel {
    private GameSimulator mGameSimulator;
    private Game game;
    private Team homeTeam;
    private Team visitingTeam;
    private String usersTeamName;
    private Organization organization;
    private Repository repository;
    private int homeErrorsAtGameStart;
    private int visitorErrorsAtGameStart;

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setVisitingTeam(Team visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    public Team getVisitingTeam() {
        return visitingTeam;
    }

    public String getUsersTeamName() {
        return usersTeamName;
    }

    public void setUsersTeamName(String usersTeamName) {
        this.usersTeamName = usersTeamName;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public int getHomeErrorsAtGameStart() {
        return homeErrorsAtGameStart;
    }

    public void setHomeErrorsAtGameStart(int homeErrorsAtGameStart) {
        this.homeErrorsAtGameStart = homeErrorsAtGameStart;
    }

    public int getVisitorErrorsAtGameStart() {
        return visitorErrorsAtGameStart;
    }

    public void setVisitorErrorsAtGameStart(int visitorErrorsAtGameStart) {
        this.visitorErrorsAtGameStart = visitorErrorsAtGameStart;
    }

    public void setGameSimulator(GameSimulator mGameSimulator) {
        this.mGameSimulator = mGameSimulator;
    }

    public GameSimulator getGameSimulator() {
        return mGameSimulator;
    }

    public void simAtBat() {
        mGameSimulator.simAtBatWithHumanControl();
    }

    public void setupGameSimulator(ResourceProvider resourceProvider) {
        boolean homeTeamControl = false;
        boolean visitingTeamControl = false;
        if (homeTeam.getTeamName().equals(usersTeamName)) {
            homeTeamControl = true;
        } else {
            visitingTeamControl = true;
        }
        mGameSimulator = new GameSimulator(resourceProvider, game, homeTeam, homeTeamControl, visitingTeam, visitingTeamControl, organization.getCurrentYear(), repository);
    }

}
