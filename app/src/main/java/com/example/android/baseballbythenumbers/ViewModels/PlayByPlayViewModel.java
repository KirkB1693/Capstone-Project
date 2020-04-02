package com.example.android.baseballbythenumbers.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Repository.Repository;

public class PlayByPlayViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private LiveData<Game> mGame;

    public PlayByPlayViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((BaseballByTheNumbersApp) application).getRepository();
    }

    public LiveData<Game> getGame() {
        return mGame;
    }

    public void setGame(String gameId) {
        mGame = mRepository.getLiveDataForGame(gameId);
    }
}
