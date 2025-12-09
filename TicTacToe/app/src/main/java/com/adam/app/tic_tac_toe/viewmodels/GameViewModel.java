/**
 * This class is the Game view model for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.viewmodels;

import android.app.Application;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.tic_tac_toe.manager.SettingsManager;
import com.adam.app.tic_tac_toe.models.Board;
import com.adam.app.tic_tac_toe.models.Player;
import com.adam.app.tic_tac_toe.models.strategy.AIStrategy;
import com.adam.app.tic_tac_toe.utils.GameUtils;

public class GameViewModel extends AndroidViewModel {

    private static final String TAG = "GameViewModel";


    private final Board mBoard;
    private Player mCurrentPlayer;

    // --- Live Data ---
    private final MutableLiveData<Player[][]> mBoardState = new MutableLiveData<>(null);
    private final MutableLiveData<String> mStatusText = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> mIsGameOver = new MutableLiveData<>(false);


    /**
     * GameMode
     */
    public enum GameMode {
        PVP,  // player vs player
        PVE   // player vs AI
    }
    private GameMode mGameMode = GameMode.PVP;
    private Player mAiPlayer = Player.O; //default
    private AIStrategy mAiStrategy = AIStrategy.EasyAIStrategy;


    public GameViewModel(@NonNull Application application) {
        super(application);
        mBoard = new Board();
        updateSetting();
        startGame();
    }

    private void updateSetting() {
        SettingsManager settingsManager = SettingsManager.getInstance(getApplication());
        boolean isGameModePve = settingsManager.isGameModePve();
        boolean isAiStrategyHard = settingsManager.isAiStrategyHard();

        mGameMode = isGameModePve ? GameMode.PVE : GameMode.PVP;
        mAiStrategy = isAiStrategyHard ? AIStrategy.HardAIStrategy : AIStrategy.EasyAIStrategy;
    }

    /**
     * onCellClicked (data binding)
     * @param row int
     * @param col int
     */
    public void onCellClicked(int row, int col) {
        // guard
        if (mBoard.getWinner() != null || mIsGameOver.getValue() == Boolean.TRUE) {
            return;
        }

//        if (mGameMode == GameMode.PVE) {
//            if (mCurrentPlayer == mAiPlayer) {
//                return;
//            }
//        }

        // process
        if (mBoard.placeMove(mCurrentPlayer, row, col)) {
            switchPlayerAndUpdate();
        }
    }

    private void switchPlayerAndUpdate() {
        switchPlayer();
        updateUI();

        // check mode
        if (mGameMode == GameMode.PVE) {
            if (mCurrentPlayer == mAiPlayer) {
                triggerAIMove();
            }
        }
    }

    private void triggerAIMove() {
        GameUtils.log(TAG, "triggerAIMove");
        // delay 500 ms
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Point bestMove = mAiStrategy.findBestMove(mBoard, mAiPlayer);
            if (bestMove != null) {
                GameUtils.log(TAG, "bestMove: " + bestMove.x + ", " + bestMove.y);
                onCellClicked(bestMove.x, bestMove.y);
            }

        }, 500L);
    }

    /**
     * resetGame (data binding)
     */
    public void resetGame() {
        startGame();
    }

    /**
     * switchPlayer
     */
    private void switchPlayer() {
        GameUtils.log(TAG, "switchPlayer");
        mCurrentPlayer = mCurrentPlayer == Player.X ? Player.O : Player.X;
    }


    private void startGame() {
        // reset game
        mBoard.reset();
        mCurrentPlayer = Player.X;
        updateUI();
    }

    private void updateUI() {
        GameUtils.log(TAG, "updateUI");
        // update board status
        final int size = Board.BOARD_SIZE;
        Player[][] currentBoardState = new Player[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                currentBoardState[i][j] = mBoard.getCells()[i][j].getPlayer();
            }
        }
        mBoardState.setValue(currentBoardState);

        // update status text
        if (mBoard.getWinner() != null) {
            mStatusText.setValue("Player " + mBoard.getWinner() + " wins!");
            mIsGameOver.setValue(Boolean.TRUE);
        } else if (mBoard.isDraw()) {
            mStatusText.setValue("It's a draw!");
            mIsGameOver.setValue(Boolean.TRUE);
        } else {
            mStatusText.setValue("Player " + mCurrentPlayer + "'s turn");
            mIsGameOver.setValue(Boolean.FALSE);
        }
    }


    // --- get ---
    public LiveData<Player[][]> getBoardState() {
        return mBoardState;
    }

    public LiveData<String> getStatusText() {
        return mStatusText;
    }

    public LiveData<Boolean> getIsGameOver() {
        return mIsGameOver;
    }



}