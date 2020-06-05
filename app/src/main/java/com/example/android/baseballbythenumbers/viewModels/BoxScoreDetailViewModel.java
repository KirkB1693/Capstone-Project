package com.example.android.baseballbythenumbers.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.data.BattingLine;
import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.data.PitchingLine;
import com.example.android.baseballbythenumbers.repository.Repository;

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
