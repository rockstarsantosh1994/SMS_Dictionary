package com.rockstar.mvvmtutorial

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rockstar.mvvmtutorial.utitlity.AllKeys
import com.rockstar.mvvmtutorial.utitlity.CommonMethods

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Change android system's buttons color
        window.navigationBarColor = ContextCompat.getColor(this@SplashActivity, R.color.purple_700)
        //Get Token
       /* FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.e(TAG, "onCreate: $token" )
            CommonMethods.setPreference(this@SplashActivity, AllKeys.GCM_TOKEN, token)
        })*/

        val SPLASHDISPLAYDURATION = 2000

        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
                if (CommonMethods.getPrefrence(this@SplashActivity, AllKeys.USERNAME).equals(AllKeys.DNF)) {
                    // check if permissions are given
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
        }, SPLASHDISPLAYDURATION.toLong())
    }
}