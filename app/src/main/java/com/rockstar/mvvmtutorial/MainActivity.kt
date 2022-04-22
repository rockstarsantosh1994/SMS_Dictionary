package com.rockstar.mvvmtutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rockstar.mvvmtutorial.observer.MainActivityObserver

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(MainActivityObserver())
        
        Log.e(TAG, "onCreate: of activity", )
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: of activity", )
    }
}
