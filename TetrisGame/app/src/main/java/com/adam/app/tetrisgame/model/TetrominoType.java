/**
 * Copyright 2025 Adam
 * Description: Tetromino is the shape of the tetris.
 * Author: Adam
 * Date: 2025/06/23
 */
package com.adam.app.tetrisgame.model;

import android.graphics.Color;

public enum TetrominoType {

    I(
            new int[][]{
                    {0, 0, 0, 0},
                    {1, 1, 1, 1},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
            },
            Color.CYAN
    ),
    O(
            new int[][]{
                    {1, 1},
                    {1, 1}
            },
            Color.YELLOW
    ),
    T(
            new int[][]{
                    {0, 1, 0},
                    {1, 1, 1},
                    {0, 0, 0}
            },
            Color.MAGENTA
    ),
    S(
            new int[][]{
                    {0, 1, 1},
                    {1, 1, 0},
                    {0, 0, 0}
            },
            Color.GREEN
    ),
    Z(
            new int[][]{
                    {1, 1, 0},
                    {0, 1, 1},
                    {0, 0, 0}
            },
            Color.RED
    ),
    J(
            new int[][]{
                    {1, 0, 0},
                    {1, 1, 1},
                    {0, 0, 0}
            },
            Color.BLUE
    ),
    L(
            new int[][]{
                    {0, 0, 1},
                    {1, 1, 1},
                    {0, 0, 0}
            },
            Color.rgb(255, 165, 0) // Orange
    );

    // two dimensional shap array of integers
    private final int[][] mShape;
    // color
    private final int mColor;

    TetrominoType(int[][] shape, int color) {
        mShape = shape;
        mColor = color;
    }

    public int[][] getShape() {
        return mShape;
    }

    public int getColor() {
        return mColor;
    }
}
