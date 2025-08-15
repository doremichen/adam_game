package com.adam.app.tetrisgame.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.tetrisgame.Utils;
import com.adam.app.tetrisgame.data.ScoreDatabase;
import com.adam.app.tetrisgame.model.ScoreDao;
import com.adam.app.tetrisgame.model.ScoreRecord;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.view.TetrisView;

public class GameViewModel extends ViewModel{
    public static final int MAX_NUM = 100;
    public static final int RESERVE_NUM = 99;
    //    public static final String GAME_PREFS = "game_prefs";
//    public static final String HIGH_SCORE_KEY = "high_score";
//    public static final String GAME_SETTINGS = "game_settings";
//    public static final String SPEED_KEY = "speed";
//    public static final String SOUND_KEY = "sound";

    /**
     * interface GAME_PREFS
     * fileName: string
     * highScorekey: string
     */
    private interface GAME_PREFS {
        String fileName = "game_prefs";
        String highScorekey = "high_score";
    }

    /**
     * interface GAME_SETTINGS
     * fileName: string
     * speedKey: string
     * soundKey: string
     */
    private interface GAME_SETTINGS {
        String fileName = "game_settings";
        String speedKey = "speed";
        String soundKey = "sound";
    }



    // TetrisBord
    private TetrisBoard mTetrisBoard;
    // running flag
    private boolean mRunning = true;

    // MutableLiveData currentScore and highScore
    private final MutableLiveData<Integer> mCurrentScore = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> mHighScore = new MutableLiveData<>(0);

    // speed and sound setting live data
    private final MutableLiveData<Integer> mSpeedLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> mSoundLiveData = new MutableLiveData<>(true);

    // init tetrisbord method
    public void initTetrisBoard(TetrisBoard.GameListener listener) {
        // check this.mTetrisBoard null
        if (this.mTetrisBoard != null) {
            throw new ExceptionInInitializerError("this.mTetrisBoard has been initialized!!!");
        }

        this.mTetrisBoard = new TetrisBoard(listener);
    }

    // get bord
    public TetrisBoard getTetrisBoard() {
        return this.mTetrisBoard;
    }

    // update bord
    public void updateTetrisBoard() {
        // check this.mTetrisBoard null
        if (this.mTetrisBoard == null) {
            Utils.log("this.mTetrisBoard is null");
            return;
        }
        if (!mRunning) {
            Utils.log("mRunning is false");
            return;
        }
        this.mTetrisBoard.update();
    }

    // applyToView with TetrisView
    public void applyToView(TetrisView view) {
        // check this.mTetrisBoard null or view is null
        if (this.mTetrisBoard == null || view == null) {
            Utils.log("this.mTetrisBoard or view is null");
            return;
        }
        this.mTetrisBoard.applyToView(view);
    }

    // reset
    public void reset() {
        // check this.mTetrisBoard null
        if (this.mTetrisBoard == null) {
            Utils.log("this.mTetrisBoard is null");
            return;
        }

        this.mTetrisBoard.reset();
    }

    // set running flag
    public void setRunning(boolean running) {
        this.mRunning = running;
    }

    // is running
    public boolean isRunning() {
        return this.mRunning;
    }

    // get current score
    public MutableLiveData<Integer> getCurrentScore() {
        return mCurrentScore;
    }

    // get high score
    public MutableLiveData<Integer> getHighScore() {
        return mHighScore;
    }

   // get speed
    public MutableLiveData<Integer> getSpeed() {
        return mSpeedLiveData;
    }

    // set speed
    public void setSpeed(int speed) {
        mSpeedLiveData.setValue(speed);
    }


    // get integer speed from sharedeferences
    public int getSpeedInt(Context context) {
        // get speed from shared preferences
        SharedPreferences prefs = context.getSharedPreferences(GAME_SETTINGS.fileName, Context.MODE_PRIVATE);
        return prefs.getInt(GAME_SETTINGS.speedKey, 0);
    }

    // set sound
    public void setSound(boolean sound) {
        mSoundLiveData.setValue(sound);
    }

    // get sound
    public MutableLiveData<Boolean> getSound() {
        return mSoundLiveData;
    }

    // get sound switch setting from sharedeferences
    public boolean isSoundEffectEnabled(Context context) {
        // get speed from shared preferences
        SharedPreferences prefs = context.getSharedPreferences(GAME_SETTINGS.fileName, Context.MODE_PRIVATE);
        return prefs.getBoolean(GAME_SETTINGS.soundKey, true);
    }


    public void increaseScore(int value) {
        int newScore = (mCurrentScore.getValue() != null ? mCurrentScore.getValue() : 0) + value;
        mCurrentScore.setValue(newScore);
        // high score
        int highScore = mHighScore.getValue() != null ? mHighScore.getValue() : 0;
        if (newScore > highScore) {
            mHighScore.setValue(newScore);
        }
    }

    // reset score
    public void resetScore() {
        mCurrentScore.setValue(0);
    }

    // load high score
    public void loadHighScore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GAME_PREFS.fileName, Context.MODE_PRIVATE);
        int savedHigh = prefs.getInt(GAME_PREFS.highScorekey, 0);
        mHighScore.setValue(savedHigh);
    }

    /**
     * Save score in database: score_database
     *
     */
    public void saveScore(Context context) {
        // get database dao
        ScoreDao dao = ScoreDatabase.getDatabase(context).scoreDao();
        // delete lowest scores when the count is large than 100
        int count = dao.getCount();
        if (count > MAX_NUM) {
            dao.deleteLowestScores(count - RESERVE_NUM);
        }
        // insert score to database
        int score = mCurrentScore.getValue() != null ? mCurrentScore.getValue() : 0;
        ScoreRecord record = new ScoreRecord(System.currentTimeMillis(), score);
        dao.insert(record);
    }



    public void saveHighScore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GAME_PREFS.fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int saveHigh = mHighScore.getValue() != null ? mHighScore.getValue() : 0;
        editor.putInt(GAME_PREFS.highScorekey, saveHigh);
        editor.apply();
    }

    // load speed and sound from shared preferences: game_settings
    public void loadSettings(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GAME_SETTINGS.fileName, Context.MODE_PRIVATE);
        mSpeedLiveData.setValue(prefs.getInt(GAME_SETTINGS.speedKey, 0));
        mSoundLiveData.setValue(prefs.getBoolean(GAME_SETTINGS.soundKey, true));
    }

    // save speed and sound to shared preferences: game_settings
    public void saveSettings(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GAME_SETTINGS.fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int speed = mSpeedLiveData.getValue() != null ? mSpeedLiveData.getValue() : 0;
        boolean sound = mSoundLiveData.getValue() != null ? mSoundLiveData.getValue() : true;
        editor.putInt(GAME_SETTINGS.speedKey, speed);
        editor.putBoolean(GAME_SETTINGS.soundKey, sound);
        editor.apply();
    }

}
