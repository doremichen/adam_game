/**
 * Copyright 2025 Adam
 * Description: Whack-A-Mole main activity
 * Author Adam
 * Date: 2025/06/25
 */
package com.adam.app.whackamole

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.adam.app.whackamole.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // view binding
    private lateinit var mBinding: ActivityMainBinding
    // score value
    private var mScore = 0
    // Handler
    private val mHandler = Handler(Looper.getMainLooper())
    // Current mole index
    private var mCurrentMoleIndex = -1
    // mole buttons
    private lateinit var mMoleButtons: List<ImageButton>
    // interval milliseconds
    private var mInterval = 1000L
    // game duration time: milliseconds
    private val mGameDurationMillis = 30000L
    // CountDownTimer
    private var mCountDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // initial mole buttons list
        mMoleButtons = listOf(
            mBinding.mole0,
            mBinding.mole1,
            mBinding.mole2,
            mBinding.mole3,
            mBinding.mole4,
            mBinding.mole5,
            mBinding.mole6,
            mBinding.mole7,
            mBinding.mole8
        )

        // hide all mole
        mMoleButtons.forEach {
            it.setImageResource(android.R.color.transparent)
        }

        // set click listener
        mMoleButtons.forEachIndexed { index, imageButton ->
            imageButton.setOnClickListener {
                // log message: index and mCurrentMoleIndex
                //Utils.log("index: $index, mCurrentMoleIndex: $mCurrentMoleIndex")

                if (index == mCurrentMoleIndex) {
                    mScore++
                    updateScore()
                    hideMode(index)
                    mCurrentMoleIndex = -1
                }
            }

        }

        // exit button click listener
        mBinding.exitButton.setOnClickListener {
            // stop game
            stopGame()

            // show dialog when click exit button
            Utils.showDialog(
                this,
                getString(R.string.dialog_title),
                getString(R.string.dialog_message),
                Utils.DialogButton(
                    getString(R.string.dialog_positive_button),
                    DialogInterface.OnClickListener { dialog, _ ->
                        // finish activity
                        finishAffinity()
                    }
                ),
                Utils.DialogButton(
                    getString(R.string.dialog_negative_button),
                    DialogInterface.OnClickListener { dialog, _ ->
                        // dismiss dialog
                        dialog.dismiss()
                        // start game
                        startGameLoop()
                    }
                )
            )
        }

        updateScore()
        startGameLoop()
        // startCountdownTimer
        startCountdownTimer()
    }

    private fun startCountdownTimer() {
        Utils.log("startCountdownTimer")
        mCountDownTimer = object : CountDownTimer(mGameDurationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Utils.log("onTick: $millisUntilFinished")
                mBinding.timeValue.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                // stop game
                stopGame()
                // showGameOverDialog
                showGameOverDialog()
            }
        }.start()
    }

    // stop countdown timer
    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer?.cancel()
    }

    private fun showGameOverDialog() {
        // show game over dialog
        Utils.showDialog(
            this,
            getString(R.string.dialog_title),
            getString(R.string.game_over_message),
            Utils.DialogButton(
                getString(R.string.dialog_positive_button),
                DialogInterface.OnClickListener { dialog, _ ->
                    // dialog dismiss
                    dialog.dismiss()
                    // restart game
                    mScore = 0
                    updateScore()
                    startGameLoop()
                    startCountdownTimer()
                }),
            null
        )
    }

    // override onCreateOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // override onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_set_interval -> {

                // EditText view
                val input = EditText(this).apply {
                    hint = context.getString(R.string.input_hint_enter_interval)
                    inputType = InputType.TYPE_CLASS_NUMBER
                }

                Utils.showEditDialog(
                    this,
                    getString(R.string.set_interval_title),
                    getString(R.string.set_interval_message),
                    input,
                    Utils.DialogButton(
                        getString(R.string.dialog_comfirm_button),
                        DialogInterface.OnClickListener { dialog, _ ->
                            // get input text
                            val interval = input.text.toString().toLongOrNull()

                            // maxValue
                            val maxInterval = 5000L
                            // minValue
                            val minInterval = 100L


                            if (interval != null && interval  in minInterval..maxInterval) {
                                mInterval = interval
                                // show toast to user: interval is set to $interval seconds
                                Utils.showToast(this, getString(R.string.interval_is_set, interval))
                            } else {
                                Utils.showToast(this, getString(R.string.invalid_interval_range, minInterval, maxInterval))
                            }

                        }
                    ),
                    Utils.DialogButton(
                        getString(R.string.dialog_negative_button),
                        null
                    )
                )
                return true
            }
            else -> return super.onOptionsItemSelected(item)

        }

    }



    private fun stopGame() {

        // remove all callbacks
        mHandler.removeCallbacksAndMessages(null)
        // hide all moles
        mMoleButtons.forEach {
            it.setImageResource(android.R.color.transparent)
        }
    }

    private fun startGameLoop() {
        mHandler.post(object : Runnable {
            override fun run() {
                showRandomMole()
                mHandler.postDelayed(this, mInterval)
            }
        })
    }

    private fun showRandomMole() {
        // check mCurrentMoleIndex
        if (mCurrentMoleIndex != -1) {
            hideMode(mCurrentMoleIndex)
        }
        // generate random index
        var randomIndex  = Random.nextInt(mMoleButtons.size)
        // set image resource of random index
        mMoleButtons[randomIndex ].setImageResource(R.drawable.mole)
        // update mCurrentMoleIndex
        mCurrentMoleIndex = randomIndex
    }

    private fun hideMode(id: Int) {
        mMoleButtons[id].setImageResource(android.R.color.transparent)
        // mCurrentMoleIndex is -1
        mCurrentMoleIndex = -1

    }

    private fun updateScore() {
        mBinding.scoreValue.text = mScore.toString()
    }
}