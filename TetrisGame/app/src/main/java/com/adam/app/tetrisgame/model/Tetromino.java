/**
 * Copyright 2025 Adam
 * Description: Tetromino is the shape of the tetris.
 * Author: Adam
 * Date: 2025/06/23
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
