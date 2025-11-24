/**
 * This class is used to provide some utility functions.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class GameUtil {

    // TAG
    public static final String TAG = "FlappyBird";
    public static final String GAME_TAG = "GameUtil";

    private GameUtil() {
    }

    /**
     * log message
     *
     * @param title   title
     * @param message message
     */
    public static void info(String title, String message) {
        Log.i(TAG, title + " : " + message);
    }

    /**
     * log error
     *
     * @param title   title
     * @param message message
     */
    public static void error(String title, String message) {
        Log.e(TAG, title + " : " + message);
    }

    /**
     * show dialog
     *
     * @param context        context
     * @param title          title
     * @param message        message
     * @param positiveButton positive button
     * @param negativeButton negative button
     */
    public static void showDialog(Context context,
                                  String title,
                                  String message,
                                  ButtonContent positiveButton,
                                  ButtonContent negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabel(), positiveButton.getOnClickListener());
        }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabel(), negativeButton.getOnClickListener());
        }
        builder.show();
    }

    public static void showToast(Context mContext, String message) {
        // check if the main thread
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * class ButtonContent
     * This class is used to provide the content of button.
     *
     */
    public static class ButtonContent {
        private final String mLabel;
        private final DialogInterface.OnClickListener mOnClickListener;

        public ButtonContent(String label, DialogInterface.OnClickListener onClickListener) {
            mLabel = label;
            mOnClickListener = onClickListener;
        }

        // --- getter ---
        public String getLabel() {
            return mLabel;
        }

        public DialogInterface.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }

    }

    public static class SCALE {
        public static final float SCALE_X = 1f;
        public static final float SCALE_Y = 1f;

        private static float sScaleX = SCALE_X;
        private static float sScaleY = SCALE_Y;

        public static void setScale(float origX, float origY) {
            sScaleX = origX/GameConstants.SCREEN_WIDTH;
            sScaleY = origY/GameConstants.SCREEN_HEIGHT;
        }

        public static float wX(float worldX) {
            return worldX * ((sScaleX == SCALE_X)? SCALE_X: sScaleX);
        }

        public static float wY(float worldY) {
            return worldY * ((sScaleY == SCALE_Y)? SCALE_Y: sScaleY);
        }

        public static float getScaleX() {
            return sScaleX;
        }

        public static float getScaleY() {
            return sScaleY;
        }

    }


    public static String formateDate(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
