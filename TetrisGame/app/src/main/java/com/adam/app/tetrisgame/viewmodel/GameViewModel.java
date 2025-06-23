package com.adam.app.tetrisgame.viewmodel;

import androidx.lifecycle.ViewModel;

import com.adam.app.tetrisgame.Utils;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.view.TetrisView;

public class GameViewModel extends ViewModel{
    // TetrisBord
    private TetrisBoard mTetrisBoard;
    // running flag
    private boolean mRunning = true;

    // init tetrisbord method
    public void initTetrisBoard(TetrisBoard.GameOverListener listener) {
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
}
