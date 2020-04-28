package com.example.callrecorder

import android.annotation.TargetApi
import android.os.Build
import android.telecom.Call
import android.widget.Toast

@TargetApi(Build.VERSION_CODES.M)
fun Call.toGsmCall() = GsmCall(
    status = state.toGsmCallStatus(),
    displayName = details.handle.schemeSpecificPart
)

private fun Int.toGsmCallStatus() = when (this) {
    Call.STATE_ACTIVE -> {
        Toast.makeText(MyApplication.mContext,"connected",Toast.LENGTH_SHORT).show()
        GsmCall.Status.ACTIVE
    }
    Call.STATE_RINGING -> GsmCall.Status.RINGING
    Call.STATE_CONNECTING -> GsmCall.Status.CONNECTING
    Call.STATE_DIALING -> GsmCall.Status.DIALING
    Call.STATE_DISCONNECTED ->{
        Toast.makeText(MyApplication.mContext,"Disconnected",Toast.LENGTH_SHORT).show()
        GsmCall.Status.DISCONNECTED
    }
    else -> GsmCall.Status.UNKNOWN
}
