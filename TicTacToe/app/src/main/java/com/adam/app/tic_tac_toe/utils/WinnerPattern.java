/**
 * This class provides WinnerPattern for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.utils;

import android.graphics.Point;

import com.adam.app.tic_tac_toe.models.Cell;
import com.adam.app.tic_tac_toe.models.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WinnerPattern {

    // define winner pattern
    public static final List<List<Cell>> WIN_PATTERNS = new ArrayList<>();

    static {
        // Rows
        WIN_PATTERNS.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 0)),
                new Cell(Player.X, new Point(0, 1)),
                new Cell(Player.X, new Point(0, 2))
        ));
        WIN_PATTERNS.add(Arrays.asList(
                new Cell(Player.X, new Point(1, 0)),
                new Cell(Player.X, new Point(1, 1)),
                new Cell(Player.X, new Point(1, 2))
        ));
        WIN_PATTERNS.add(Arrays.asList(
                new Cell(Player.X, new Point(2, 0)),
                new Cell(Player.X, new Point(2, 1)),
                new Cell(Player.X, new Point(2, 2))
        ));

        // Columns
        WIN_PATTERNS.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 0)),
                new Cell(Player.X, new Point(1, 0)),
                new Cell(Player.X, new Point(2, 0))
        ));
        WIN_PATTERNS.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 1)),
                new Cell(Player.X, new Point(1, 1)),
                new Cell(Player.X, new Point(2, 1))
        ));
        WIN_PATTERNS.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 2)),
                new Cell(Player.X, new Point(1, 2)),
                new Cell(Player.X, new Point(2, 2))
        ));

        // Diagonal (top-left to bottom-right)
        WIN_PATTERNS.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 0)),
                new Cell(Player.X, new Point(1, 1)),
                new Cell(Player.X, new Point(2, 2))
        ));

        // Diagonal (top-right to bottom-left)
        WIN_PATTERNS.add(Arrays.asList(
                new Cell(Player.X, new Point(0, 2)),
                new Cell(Player.X, new Point(1, 1)),
                new Cell(Player.X, new Point(2, 0))
        ));

    }


}
