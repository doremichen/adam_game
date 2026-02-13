package com.adam.app.memorycardgame.ui.game.binding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.adam.app.memorycardgame.R;
import com.adam.app.memorycardgame.data.model.Card;

public class CardBindingAdapter {

    /**
     * Card <==> Boolean
     * @param view
     * @param card
     */
    @BindingAdapter("cardImage")
    public static void setCardImage(ImageView view, Card card) {
        view.setImageResource(card.getCardState().getResId(card));
    }
}
