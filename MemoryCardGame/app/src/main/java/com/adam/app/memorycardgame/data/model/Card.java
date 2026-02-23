/**
 * Copyright (C) 2026 Adam Chen. All rights reserved.
 * <p>
 * Description: This is a card model
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.data.model;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.adam.app.memorycardgame.R;

public class Card extends BaseObservable {
    // id
    private final int mId;
    // img resource id
    private final int mImgResId;
    @Bindable
    private CardState mCardState = CardState.FACE_DOWN;


    public Card(int id, int imgResId) {
        mId = id;
        mImgResId = imgResId;
    }

    // copy constructor
    public Card(Card card) {
        mId = card.getId();
        mImgResId = card.getImgResId();
        mCardState = card.getCardState();
    }


    // copy method
    public Card copy() {
        return new Card(this);
    }


    // -- get --
    public int getId() {
        return mId;
    }

    public int getImgResId() {
        return mImgResId;
    }

    @Bindable
    public CardState getCardState() {
        return mCardState;
    }

    public void setCardState(CardState cardState) {
        mCardState = cardState;
    }

    public enum CardState {
        FACE_UP {
            @Override
            public int getResId(Card card) {
                return card.getImgResId();
            }
        },
        FACE_DOWN {
            @Override
            public int getResId(Card card) {
                return R.drawable.card_back;
            }
        },
        MATCHED {
            @Override
            public int getResId(Card card) {
                return R.drawable.card_empty;
            }
        };

        public abstract int getResId(Card card);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Card &&
                ((Card) obj).getId() == getId() &&
                ((Card) obj).getImgResId() == getImgResId() &&
                ((Card) obj).getCardState() == getCardState();
    }
}
