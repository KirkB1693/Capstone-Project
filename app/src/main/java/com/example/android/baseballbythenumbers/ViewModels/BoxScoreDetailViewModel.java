package com.example.android.baseballbythenumbers.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.Data.BattingLine;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.PitchingLine;
import com.example.android.baseballbythenumbers.Repository.Repository;

import java.util.List;

public class BoxScoreDetailViewModel extends AndroidViewModel {
    private final Repository mRepository;

    private LiveData<Game> mGame;

    private LiveData<List<BattingLine>> mBattingLines;

    private LiveData<List<PitchingLine>> mPitchingLines;

    public BoxScoreDetailViewModel(Application application){
        super(application);
        mRepository = ((BaseballByTheNumbersApp) application).getRepository();
    }

    public LiveData<Game> getGame() {
        return mGame;
    }

    public void setGame(String gameId) {
        mGame = mRepository.getLiveDataForGame(gameId);
    }

    public LiveData<List<BattingLine>> getBattingLines() {
        return mBattingLines;
    }

    public void setBattingLines(String boxScoreId) {
        mBattingLines = mRepository.getLiveDataBattingLinesForBoxScore(boxScoreId);
    }

    public LiveData<List<PitchingLine>> getPitchingLines() {
        return mPitchingLines;
    }

    public void setPitchingLines(String boxScoreId) {
        mPitchingLines = mRepository.getLiveDataPitchingLinesForBoxScore(boxScoreId);
    }
}
