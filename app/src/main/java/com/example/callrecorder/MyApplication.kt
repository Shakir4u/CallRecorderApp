package com.example.callrecorder

import android.app.Application
import android.content.Context

class MyApplication: Application() {
    companion object {
        lateinit var mContext:Context
    }
    override fun onCreate() {
        super.onCreate()
        mContext=this
    }
}