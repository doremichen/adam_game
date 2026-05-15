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

public class Tetromino {
    // Tetromino type
    private TetrominoType mType;
    // two dimensional shap array of integers
    private int[][] mShape;
    // color
    private final int mColor;

    public Tetromino(TetrominoType type) {
        mType = type;
        mShape = type.getShape();
        mColor = type.getColor();
    }

    // set shape
    public void setShape(int[][] shape) {
        int n = shape.length;
        mShape = new int[n][shape[0].length];
        for (int i = 0; i < n; i++) {
            System.arraycopy(shape[i], 0, mShape[i], 0, shape[i].length);
        }
    }

    public int[][] getShape() {
        return mShape;
    }

    public int getColor() {
        return mColor;
    }

    public TetrominoType getType() {
        return mType;
    }

    public void rotate() {
        int n = mShape.length;
        int[][] rotated = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = mShape[i][j];
            }
        }
        mShape = rotated;
    }

}
