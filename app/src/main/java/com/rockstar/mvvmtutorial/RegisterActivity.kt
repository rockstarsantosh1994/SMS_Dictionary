package com.rockstar.mvvmtutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rockstar.mvvmtutorial.entity.User
import com.rockstar.mvvmtutorial.utitlity.CommonMethods
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"

    lateinit var database: UserDataBase

    //TextInputLayout declaration..
    private var tilFirstName: TextInputLayout? = null
    private var tilLastName: TextInputLayout? = null
    private var tilMobileNumber: TextInputLayout? = null
    private var tilPassword: TextInputLayout? = null
    private var tilConfimPassword: TextInputLayout? = null

    //TextInputEditText declaration...
    private var etFirstName: TextInputEditText? = null
    private var etLastName: TextInputEditText? = null
    private var etMobileNumber: TextInputEditText? = null
    private var etPassword: TextInputEditText? = null
    private var etConfimPassword: TextInputEditText? = null

    //AppCompatButton declaration..
    private var btnSignIn: AppCompatButton? = null

    var user:User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        database = UserDataBase.getDatabase(applicationContext)
        //Basic intialisation...
        initViews()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initViews() {
        //TextInputLayout declaration..
        tilFirstName = findViewById(R.id.til_first_name)
        tilLastName = findViewById(R.id.til_last_name)
        tilMobileNumber = findViewById(R.id.til_mobile_number)
        tilPassword = findViewById(R.id.til_password)
        tilConfimPassword = findViewById(R.id.til_confirm_password)

        //TextInputEditText declaration..
        etFirstName = findViewById(R.id.et_first_name)
        etLastName = findViewById(R.id.et_last_name)
        etMobileNumber = findViewById(R.id.et_mobile_number)
        etPassword = findViewById(R.id.et_password)
        etConfimPassword = findViewById(R.id.et_confirm_password)

        //AppCompatButton declaration..
        btnSignIn = findViewById(R.id.btn_signup)

        btnSignIn?.setOnClickListener {
            if (isValidated()) {
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        user = database.userDao().getParticularUser(etMobileNumber?.text.toString())
                        Log.e(TAG, "initViews: $user")
                    }
                    withContext(Dispatchers.Main) {
                        if (user?.mobileNumber == etMobileNumber?.text.toString()) {
                            CommonMethods.showDialogForError(
                                this@RegisterActivity,
                                "User Already Exist!"
                            )
                        } else {
                            register()
                        }
                    }
            }

        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun register() {
    val user = User(
        0,
        etFirstName?.text.toString(),
        etLastName?.text.toString(),
        etMobileNumber?.text.toString(),
        etPassword?.text.toString()
    )
    GlobalScope.launch {
        database.userDao().insertUser(user)
        withContext(Dispatchers.Main) {
            CommonMethods.showDialogForSuccess(
                this@RegisterActivity, "User Registration Successful!"
            )
        }
    }
}

private fun isValidated(): Boolean {
    if (etFirstName?.text.toString().isEmpty()) {
        tilFirstName?.error = "FirstName Required!"
        tilFirstName?.requestFocus()
        return false
    }

    if (etLastName?.text.toString().isEmpty()) {
        tilLastName?.error = "LastName Required!"
        tilLastName?.requestFocus()
        return false
    }

    if (etMobileNumber?.text.toString().isEmpty()) {
        tilMobileNumber?.error = "Mobile Number Required!"
        tilMobileNumber?.requestFocus()
        return false
    }

    if (etMobileNumber?.text.toString().length != 10) {
        tilMobileNumber?.error = "Invalid Mobile Number!"
        tilMobileNumber?.requestFocus()
        return false
    }

    if (etPassword?.text.toString().isEmpty()) {
        tilPassword?.error = "Password required!"
        tilPassword?.requestFocus()
        return false
    }

    if (etConfimPassword?.text.toString().isEmpty()) {
        tilConfimPassword?.error = "Confirm Password required!"
        tilConfimPassword?.requestFocus()
        return false
    }

    if (etPassword?.text.toString() != etConfimPassword?.text.toString()) {
        tilConfimPassword?.error = "Password not match!"
        tilConfimPassword?.requestFocus()
        return false
    }

    return true
}
}