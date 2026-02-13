package com.adam.app.memorycardgame.data.repository;

import com.adam.app.memorycardgame.R;
import com.adam.app.memorycardgame.data.model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton
 */
public class GameRepository {

    // list of card
    private List<Card> mCards = new ArrayList<>();


    private GameRepository() {
        // avoid client applications to use constructor
    }

    private static class Helper {
        private static final GameRepository sInstance = new GameRepository();
    }

    public static GameRepository getInstance() {
        return Helper.sInstance;
    }

    /**
     * createNewGame
     * @return List<Card>
     */
    public List<Card> createNewGame() {
        mCards.clear();
        // pictures
        int pictures[] = {R.drawable.card_1,
                R.drawable.card_2,
                R.drawable.card_3,
                R.drawable.card_4,
                R.drawable.card_5,
                R.drawable.card_6,
                R.drawable.card_7,
                R.drawable.card_8,};

        int id = 0;
        for (int img: pictures) {
            mCards.add(new Card(id++, img));
            mCards.add(new Card(id++, img));
        }

        Collections.shuffle(mCards);
        return mCards;
    }

    public List<Card> getCards() {
        return mCards;
    }

}
