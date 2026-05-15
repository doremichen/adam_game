/*
 * Copyright (c) 2025 Adam
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
