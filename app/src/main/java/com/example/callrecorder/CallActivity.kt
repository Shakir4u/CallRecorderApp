package com.example.callrecorder

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_call.*
import java.util.concurrent.TimeUnit

class CallActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "CallActivity"
    }

    private var updatesDisposable = Disposables.empty()
    private var timerDisposable = Disposables.empty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        hideBottomNavigationBar()


        tvStartStop.setOnClickListener {
            if((tvStartStop.tag as Int)==0){
                tvStartStop.tag=1
                tvStartStop.text="Stop"
                CallManager.acceptCall()
            }else{
                tvStartStop.tag=0
                tvStartStop.text="Start"
                CallManager.cancelCall()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        updatesDisposable = CallManager.updates()
            .doOnEach { Log.i(LOG_TAG, "updated call: $it") }
            .doOnError { throwable -> Log.e(LOG_TAG, "Error processing call", throwable) }
            .subscribe { updateView(it) }
    }

    private fun updateView(gsmCall: GsmCall) {
        textStatus.visibility = when (gsmCall.status) {
            GsmCall.Status.ACTIVE -> View.GONE
            else                  -> View.VISIBLE
        }
        textStatus.text = when (gsmCall.status) {
            GsmCall.Status.CONNECTING -> "Connecting…"
            GsmCall.Status.DIALING -> "Calling…"
            GsmCall.Status.RINGING -> "Incoming call"
            GsmCall.Status.ACTIVE -> ""
            GsmCall.Status.DISCONNECTED -> "Finished call"
            GsmCall.Status.UNKNOWN -> ""
        }
        textDuration.visibility = when (gsmCall.status) {
            GsmCall.Status.ACTIVE -> View.VISIBLE
            else                  -> View.GONE
        }

        if (gsmCall.status == GsmCall.Status.DISCONNECTED) {
            tvStartStop.postDelayed({ finish() }, 3000)
        }

        when (gsmCall.status) {
            GsmCall.Status.ACTIVE -> startTimer()
            GsmCall.Status.DISCONNECTED -> stopTimer()
            else                        -> Unit
        }

        textDisplayName.text = /*gsmCall.displayName*/"Unknown"
        tvUserNumber.text = gsmCall.displayName ?: "Unknown"
    }

    override fun onPause() {
        super.onPause()
        updatesDisposable.dispose()
    }

    private fun hideBottomNavigationBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun startTimer() {
        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { textDuration.text = it.toDurationString() }
    }

    private fun stopTimer() {
        timerDisposable.dispose()
    }

    private fun Long.toDurationString() = String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60))
}
