package com.adam.app.whackamole

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog

object Utils {
    // TAG string
    val TAG = "WhackAMole"
    // debug flag
    val DEBUG = true

    // log message by logcat
    fun log(message: String) {
        if (DEBUG)
            Log.d(TAG, message)
    }

    /**
     * class Dialog button content
     * text: string Button label
     * listener: DialogInterface.OnClickListener Listener for button click
     */
    data class DialogButton(
        val text: String,
        val listener: DialogInterface.OnClickListener?)

    /**
     * show dialog function
     * context: Context Context of the activity
     * title: String Title of the dialog
     * message: String Message of the dialog
     * positiveButton: DialogButton Positive button
     * negativeButton: DialogButton Negative button
     */
    fun showDialog(context: Context,
                   title: String,
                   message: String,
                   positiveButton: DialogButton,
                   negativeButton: DialogButton?) {

        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton.text, positiveButton.listener)

        negativeButton?.let {
            builder.setNegativeButton(negativeButton.text, negativeButton.listener)
        }

        builder.show()
    }

    /**
     * show view dialog function
     * context: Context Context of the activity
     * title: String Title of the dialog
     * message: String Message of the dialog
     * view: View View to be displayed in the dialog
     * positiveButton: DialogButton Positive button
     * negativeButton: DialogButton Negative button
     */
    fun showViewDialog(
        context: Context,
        title: String,
        message: String,
        view: View,
        positiveButton: DialogButton,
        negativeButton: DialogButton
    ) {

        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setView(view)
            .setPositiveButton(positiveButton.text, positiveButton.listener)
            .setNegativeButton(negativeButton.text, negativeButton.listener)
            .show()
    }

    /**
     * show toast function
     * context: Context Context of the activity
     * message: String Message to be displayed
     */
    fun showToast(context: Context, message: String) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    /**
     * Vibrator with duration function
     * context: Context Context of the activity
     * duration: Long Vibrator duration, default is 500ms
     */
    fun vibrate(context: Context, duration: Long = 500) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
        vibrator?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                it.vibrate(android.os.VibrationEffect.createOneShot(duration, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(duration)
            }
        }
    }



}