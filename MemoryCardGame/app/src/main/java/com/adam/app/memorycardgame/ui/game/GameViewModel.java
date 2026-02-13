/**
 * Copyright (c) 2020 Adam Chen. ALL rights reserved.
 * <p>
 * Description: This is the GameViewModel class.
 * </p>
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.ui.game;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.memorycardgame.data.model.Card;
import com.adam.app.memorycardgame.data.repository.GameRepository;

import java.util.List;

public class GameViewModel extends ViewModel {

    private final GameRepository mRepo = GameRepository.getInstance();

    // live data: list of card
    private final MutableLiveData<List<Card>> mLiveDataCards = new MutableLiveData<>();
    public LiveData<List<Card>> getCards() {
        return mLiveDataCards;
    }

    private Card mFirstCard;
    private boolean mLockBoard;
    private boolean mIsProcessing;

    public GameViewModel() {
        restartGame();
    }

    public void restartGame() {
        List<Card> cards = mRepo.createNewGame();
        mLiveDataCards.setValue(cards);
        mFirstCard = null;
        mLockBoard = false;
    }

    public void onCardClicked(Card card) {
        // pre check
        if (mLockBoard) {
            return;
        }

        Card.CardState cardState = card.getCardState();

        if (mIsProcessing || cardState != Card.CardState.FACE_DOWN) {
            return;
        }

        // flip card
        card.setCardState(Card.CardState.FACE_UP);
        // update live data
        mLiveDataCards.setValue(mRepo.getCards());

        if (mFirstCard == null) {
            mFirstCard = card;
        } else {
            mIsProcessing = true;
            checkMatchWithFirstCard(card);
        }
    }

    private void checkMatchWithFirstCard(Card card) {

        // null check
        if (mFirstCard == null || card == null) {
            return;
        }

        mLockBoard = true;
        // aync delay 800ms
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // compare image res with the first image
            int firstImageRes = mFirstCard.getImgResId();
            int secondImageRes = card.getImgResId();

            if (firstImageRes == secondImageRes) {
                mFirstCard.setCardState(Card.CardState.MATCHED);
                card.setCardState(Card.CardState.MATCHED);
            } else {
                mFirstCard.setCardState(Card.CardState.FACE_DOWN);
                card.setCardState(Card.CardState.FACE_DOWN);
            }

            mLockBoard = false;
            mIsProcessing = false;
            mFirstCard = null;
            mLiveDataCards.postValue(mRepo.getCards());},
                800L);
    }


}