/*
 * Copyright (c) 2026 Adam Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.adam.app.tic_tac_toe.ui.viewmodels;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.tic_tac_toe.R;
import com.adam.app.tic_tac_toe.application.usecases.GameUseCase;
import com.adam.app.tic_tac_toe.domain.entities.Board;
import com.adam.app.tic_tac_toe.domain.entities.Player;
import com.adam.app.tic_tac_toe.domain.strategy.AIStrategy;
import com.adam.app.tic_tac_toe.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * View model for the Game screen.
 */
@HiltViewModel
public class GameViewModel extends ViewModel {

    private static final String TAG = "GameViewModel";
    private static final long AI_MOVE_DELAY_MS = 500L;

    private final GameUseCase mGameUseCase;
    private final Context mContext;
    private final Board mBoard;
    private final MutableLiveData<Player[][]> mBoardState = new MutableLiveData<>(null);
    private final MutableLiveData<String> mStatusText = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> mIsGameOver = new MutableLiveData<>(false);
    private final MutableLiveData<List<Point>> mWinningCells = new MutableLiveData<>(new ArrayList<>());
    
    private Player mCurrentPlayer;
    private GameMode mGameMode = GameMode.PVP;
    private final Player mAiPlayer = Player.O;
    private AIStrategy mAiStrategy = AIStrategy.EasyAIStrategy;

    @Inject
    public GameViewModel(GameUseCase gameUseCase, @NonNull @ApplicationContext Context context) {
        mGameUseCase = gameUseCase;
        mContext = context;
        mBoard = new Board();
        updateSettings();
        startGame();
    }

    private void updateSettings() {
        GameUseCase.Settings settings = (GameUseCase.Settings) mGameUseCase.execute(GameUseCase.Action.GET_SETTINGS);
        mGameMode = settings.isPve() ? GameMode.PVE : GameMode.PVP;
        mAiStrategy = settings.isHardAi() ? AIStrategy.HardAIStrategy : AIStrategy.EasyAIStrategy;
    }

    public void onCellClicked(int row, int col) {
        if (Boolean.TRUE.equals(mIsGameOver.getValue())) {
            return;
        }

        if (mBoard.placeMove(mCurrentPlayer, row, col)) {
            switchPlayerAndUpdate();
        }
    }

    private void switchPlayerAndUpdate() {
        mCurrentPlayer = (mCurrentPlayer == Player.X) ? Player.O : Player.X;
        updateUI();

        if (mGameMode == GameMode.PVE && mCurrentPlayer == mAiPlayer) {
            triggerAIMove();
        }
    }

    private void triggerAIMove() {
        Utils.logDebug(TAG, "triggerAIMove");
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Point bestMove = mAiStrategy.findBestMove(mBoard, mAiPlayer);
            if (bestMove != null) {
                Utils.logDebug(TAG, "bestMove: " + bestMove.x + ", " + bestMove.y);
                onCellClicked(bestMove.x, bestMove.y);
            }
        }, AI_MOVE_DELAY_MS);
    }

    public void resetGame() {
        mGameUseCase.execute(GameUseCase.Action.RESET_BOARD, mBoard);
        startGame();
    }

    private void startGame() {
        mCurrentPlayer = Player.X;
        updateUI();
    }

    private void updateUI() {
        Utils.logDebug(TAG, "updateUI");
        final int size = Board.BOARD_SIZE;
        Player[][] currentBoardState = new Player[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                currentBoardState[i][j] = mBoard.getCells()[i][j].getPlayer();
            }
        }
        mBoardState.setValue(currentBoardState);
        mWinningCells.setValue(mBoard.getWinningPoints());

        boolean isGameOver = (boolean) mGameUseCase.execute(GameUseCase.Action.CHECK_GAME_OVER, mBoard);
        if (isGameOver) {
            mIsGameOver.setValue(true);
            if (mBoard.getWinner() != null) {
                mStatusText.setValue(mContext.getString(R.string.tic_tac_toe_game_over_message, mBoard.getWinner().name()));
            } else {
                mStatusText.setValue(mContext.getString(R.string.tic_tac_toe_draw_game_over_message));
            }
        } else {
            mIsGameOver.setValue(false);
            mStatusText.setValue(mContext.getString(R.string.tic_tac_toe_player_turn, mCurrentPlayer.name()));
        }
    }

    @NonNull
    public LiveData<Player[][]> getBoardState() {
        return mBoardState;
    }

    @NonNull
    public LiveData<String> getStatusText() {
        return mStatusText;
    }

    @NonNull
    public LiveData<Boolean> getIsGameOver() {
        return mIsGameOver;
    }

    @NonNull
    public LiveData<List<Point>> getWinningCells() {
        return mWinningCells;
    }

    @Nullable
    public Player getBoardWinner() {
        return mBoard.getWinner();
    }

    public enum GameMode {
        PVP, PVE
    }
}
