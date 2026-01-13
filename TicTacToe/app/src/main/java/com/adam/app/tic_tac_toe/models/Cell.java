/**
 * This class is the Cell class for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.models;

import android.graphics.Point;

public class Cell {
    private Player mPlayer;
    private Point mPosition;

    public Cell(Player player, Point position) {
        mPlayer = player;
        mPosition = position;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public Cell setPlayer(Player player) {
        mPlayer = player;
        return this;
    }

    public Point getPosition() {
        return mPosition;
    }

    public void setPosition(Point position) {
        mPosition = position;
    }
}
