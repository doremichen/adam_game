package com.adam.app.memorycardgame.ui.game.binding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.adam.app.memorycardgame.R;
import com.adam.app.memorycardgame.data.model.Card;
import com.adam.app.memorycardgame.util.CommonUtils;

public class CardBindingAdapter {

    // TAG
    private static final String TAG = "CardBindingAdapter";

    /**
     * Card <==> Boolean
     *
     * @param view
     * @param card
     */
    @BindingAdapter("cardImage")
    public static void setCardImage(ImageView view, Card card) {
        CommonUtils.log(TAG + ": setCardImage");
        if (card == null) {
            CommonUtils.log(TAG + ": card is null");
            view.setImageResource(R.drawable.card_back);
            return;
        }
        view.setImageResource(card.getCardState().getResId(card));
    }
}
