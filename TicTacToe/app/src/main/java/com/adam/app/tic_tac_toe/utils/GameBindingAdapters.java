/**
 * This class provides BindingAdapters for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2026-01-13
 */
package com.adam.app.tic_tac_toe.utils;

import android.graphics.Color;
import android.graphics.Point;
import android.widget.Button;

import androidx.databinding.BindingAdapter;

import com.adam.app.tic_tac_toe.models.Player;

import java.util.List;

public class GameBindingAdapters {

    @BindingAdapter(
            value = {"boardState", "row", "col", "winningCells"},
            requireAll = true
    )
    public static void setCellBackground(Button button,
                                         Player[][] boardState,
                                         int row,
                                         int col,
                                         List<Point> winningCells) {
        // setup the display at this current moment
        if (boardState != null) {
            String show = (boardState[row][col] == null) ? "" : boardState[row][col].toString();
            button.setText(show);
        }

        // setup background
        boolean isWinningCell = false;
        // check if is winning cell
        if (winningCells != null) {
            for (Point point : winningCells) {
                if (point.x == row && point.y == col) {
                    isWinningCell = true;
                    break;
                }
            }
        }

        Color backGround = Color.valueOf((isWinningCell) ? Color.YELLOW : Color.WHITE);
        button.setBackgroundColor(backGround.toArgb());

    }

}
