package com.rockstar.mvvmtutorial.observer

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MainActivityObserver :LifecycleObserver {

    private val TAG = "MainActivityObserver"

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        Log.e(TAG, "onCreate: of observer", )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        Log.e(TAG, "onPause: of observer.", )
    }
}