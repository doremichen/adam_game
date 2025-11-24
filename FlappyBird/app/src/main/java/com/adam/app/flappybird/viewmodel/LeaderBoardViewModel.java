/**
 * This class is the view model for the leader board screen.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-21
 */
package com.adam.app.flappybird.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.flappybird.data.FlappyBird;
import com.adam.app.flappybird.data.repository.FlappyBirdRepository;
import com.adam.app.flappybird.util.GameUtil;

import java.util.List;

public class LeaderBoardViewModel extends AndroidViewModel {

    // flappy bird repository
    private final FlappyBirdRepository mRepository;

    // all scores live data
    private final MutableLiveData<List<FlappyBird>> mAllFlappyBird = new MutableLiveData<>(null);

    public LeaderBoardViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FlappyBirdRepository(application);

    }

    public LiveData<List<FlappyBird>> getAllFlappyBirds() {
        return mAllFlappyBird;
    }

    /**
     * Loads the scores from the repository.
     */
    public void loadScores() {
        mRepository.getScores(mAllFlappyBird::postValue);
    }

}
