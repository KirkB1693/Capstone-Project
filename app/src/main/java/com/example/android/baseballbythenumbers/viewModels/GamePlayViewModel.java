package com.example.android.baseballbythenumbers.viewModels;


import androidx.lifecycle.ViewModel;

import com.example.android.baseballbythenumbers.ResourceProvider;
import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.repository.Repository;
import com.example.android.baseballbythenumbers.simulators.GameSimulator;

public class GamePlayViewModel extends ViewModel {
    private GameSimulator mGameSimulator;
    private Game game;
    private Team homeTeam;
    private Team visitingTeam;
    private String usersTeamName;
    private Organization organization;
    private Repository repository;

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

    public GameSimulator getGameSimulator() {
        return mGameSimulator;
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
