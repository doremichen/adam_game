/**
 * Copyright (C) 2026 Adam Chen. All rights reserved.
 *
 * Description: This is a card model
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.data.model;

public class Card {
    // id
    private final int mId;
    // img resource id
    private final int mImgResId;
    // check face up
    private boolean mIsFaceUp;
    // check matched
    private boolean mIsMatched;

    public Card(int id, int imgResId) {
        mId = id;
        mImgResId = imgResId;
        mIsFaceUp = false;
        mIsMatched = false;
    }

    // -- set --
    public void setFaceUp(boolean isFaceUp) {
        mIsFaceUp = isFaceUp;
    }
    public void setMatched(boolean isMatched) {
        mIsMatched = isMatched;
    }

    // -- get --
    public int getId() {
        return mId;
    }
    public int getImgResId() {
        return mImgResId;
    }
    public boolean isFaceUp() {
        return mIsFaceUp;
    }
    public boolean isMatched() {
        return mIsMatched;
    }
}
