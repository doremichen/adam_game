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

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.memorycardgame.data.model.Card;
import com.adam.app.memorycardgame.data.repository.GameRepository;
import com.adam.app.memorycardgame.util.CommonUtils;
import com.adam.app.memorycardgame.util.SettingsManager;
import com.adam.app.memorycardgame.util.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends AndroidViewModel {
    // TAG
    private static final String TAG = "GameViewModel";

    private final GameRepository mRepo = GameRepository.getInstance();

    // live data: list of card
    private final MutableLiveData<List<Card>> mLiveDataCards = new MutableLiveData<>();

    private Card mFirstCard;
    private boolean mLockBoard;
    private boolean mIsProcessing;

    private final Context mContext;

    public GameViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        restartGame();
    }

    public LiveData<List<Card>> getCards() {
        return mLiveDataCards;
    }


    public void restartGame() {
        CommonUtils.log(TAG + "restartGame");
        List<Card> cards = mRepo.createNewGame();
        mLiveDataCards.setValue(new ArrayList<>(cards));
        mFirstCard = null;
        mLockBoard = false;
    }

    public void onCardClicked(Card card) {
        CommonUtils.log(TAG + ": onCardClicked");
        // pre check
        if (mLockBoard) {
            CommonUtils.log(TAG + ": lock board");
            return;
        }

        Card.CardState cardState = card.getCardState();

        if (mIsProcessing || cardState != Card.CardState.FACE_DOWN) {
            CommonUtils.log(TAG + ": processing");
            return;
        }

        List<Card> oldCards = mLiveDataCards.getValue();
        List<Card> newCards = new ArrayList<>();

        assert oldCards != null;
        for (Card oldCard : oldCards) {
            if (oldCard.getId() == card.getId()) {
                // flip card
                Card newCard = card.copy();
                Card.CardState newCardState = Card.CardState.FACE_UP;
                newCardState.playSound(mContext);
                newCard.setCardState(newCardState);
                newCards.add(newCard);

                if (mFirstCard == null) {
                    mFirstCard = card;
                } else {
                    mIsProcessing = true;
                    checkMatchWithFirstCard(card);
                }

            } else {
                newCards.add(oldCard.copy());
            }
        }

        CommonUtils.log(TAG + ": update live data");
        mLiveDataCards.setValue(newCards);

    }

    private void checkMatchWithFirstCard(Card card) {

        // null check
        if (mFirstCard == null || card == null) {
            return;
        }

        mLockBoard = true;
        // asynch delay 800ms
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    List<Card> currentCards = mRepo.getCards();
                    List<Card> newList = new ArrayList<>();

                    boolean isMatched = mFirstCard.getImgResId() == card.getImgResId();

                    // loop
                    for (Card c : currentCards) {
                        Card copyCard = c.copy();

                        if (c.getId() == mFirstCard.getId() ||
                            c.getId() == card.getId()) {
                            Card.CardState newCardState = (isMatched)? Card.CardState.MATCHED: Card.CardState.FACE_DOWN;
                            newCardState.playSound(mContext);
                            copyCard.setCardState(newCardState);
                        }

                        newList.add(copyCard);
                    }

                    mLockBoard = false;
                    mIsProcessing = false;
                    mFirstCard = null;

                    // update card list
                    mRepo.updateCards(newList);

                    mLiveDataCards.setValue(newList);
                },
                800L);
    }


}