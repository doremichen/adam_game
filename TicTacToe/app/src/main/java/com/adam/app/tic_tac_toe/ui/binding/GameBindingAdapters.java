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

package com.adam.app.tic_tac_toe.ui.binding;

import android.graphics.Color;
import android.graphics.Point;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.adam.app.tic_tac_toe.domain.entities.Player;

import java.util.List;

/**
 * Binding adapters for the game UI.
 */
public final class GameBindingAdapters {

    private GameBindingAdapters() {
        // Prevent instantiation
    }

    /**
     * Sets the cell background and text based on board state and winning cells.
     */
    @BindingAdapter(value = {"boardState", "row", "col", "winningCells"}, requireAll = true)
    public static void setCellState(@NonNull Button button,
                                    @Nullable Player[][] boardState,
                                    int row,
                                    int col,
                                    @Nullable List<Point> winningCells) {
        if (boardState != null && boardState[row][col] != null) {
            button.setText(boardState[row][col].name());
        } else {
            button.setText("");
        }

        boolean isWinningCell = false;
        if (winningCells != null) {
            for (Point point : winningCells) {
                if (point.x == row && point.y == col) {
                    isWinningCell = true;
                    break;
                }
            }
        }

        button.setBackgroundColor(isWinningCell ? Color.YELLOW : Color.LTGRAY);
    }
}
