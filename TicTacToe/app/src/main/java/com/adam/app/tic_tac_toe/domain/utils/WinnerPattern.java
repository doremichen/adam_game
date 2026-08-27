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

package com.adam.app.tic_tac_toe.domain.utils;

import android.graphics.Point;

import com.adam.app.tic_tac_toe.domain.entities.Cell;
import com.adam.app.tic_tac_toe.domain.entities.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Defines winning patterns for the game.
 */
public final class WinnerPattern {

    private static final List<List<Cell>> sWinPatterns = new ArrayList<>();

    static {
        // Rows
        sWinPatterns.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 0)),
                new Cell(Player.X, new Point(0, 1)),
                new Cell(Player.X, new Point(0, 2))
        ));
        sWinPatterns.add(Arrays.asList(
                new Cell(Player.X, new Point(1, 0)),
                new Cell(Player.X, new Point(1, 1)),
                new Cell(Player.X, new Point(1, 2))
        ));
        sWinPatterns.add(Arrays.asList(
                new Cell(Player.X, new Point(2, 0)),
                new Cell(Player.X, new Point(2, 1)),
                new Cell(Player.X, new Point(2, 2))
        ));

        // Columns
        sWinPatterns.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 0)),
                new Cell(Player.X, new Point(1, 0)),
                new Cell(Player.X, new Point(2, 0))
        ));
        sWinPatterns.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 1)),
                new Cell(Player.X, new Point(1, 1)),
                new Cell(Player.X, new Point(2, 1))
        ));
        sWinPatterns.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 2)),
                new Cell(Player.X, new Point(1, 2)),
                new Cell(Player.X, new Point(2, 2))
        ));

        // Diagonals
        sWinPatterns.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 0)),
                new Cell(Player.X, new Point(1, 1)),
                new Cell(Player.X, new Point(2, 2))
        ));
        sWinPatterns.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 2)),
                new Cell(Player.X, new Point(1, 1)),
                new Cell(Player.X, new Point(2, 0))
        ));
    }

    public static List<List<Cell>> getWinPatterns() {
        return Collections.unmodifiableList(sWinPatterns);
    }

    private WinnerPattern() {
        // Prevent instantiation
    }
}
