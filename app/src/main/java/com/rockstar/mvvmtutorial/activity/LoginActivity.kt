package com.rockstar.mvvmtutorial.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rockstar.mvvmtutorial.R
import com.rockstar.mvvmtutorial.UserDataBase
import com.rockstar.mvvmtutorial.entity.User
import com.rockstar.mvvmtutorial.utitlity.AllKeys
import com.rockstar.mvvmtutorial.utitlity.CommonMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    private val TAG = "LoginActivity"

    lateinit var database: UserDataBase

    private val PERMISSION_ID = 44

    //TextInputLayout and Editext Declaration..
    private var tilUserName: TextInputLayout?=null
    private var etUserName: TextInputEditText?=null
    private var tilPassWord: TextInputLayout?=null
    private var etPassword: TextInputEditText?=null

    //CheckBox Declaration..
    private var chRememberMe: CheckBox?=null

    //AppCompatButton Declaration...
    private var btnLogin: AppCompatButton?=null
    private var btnLoginWithOtp: AppCompatButton?=null

    var user: User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        database = UserDataBase.getDatabase(applicationContext)

        //Basic intialisation...
        initViews()

        //request run time permission
        requestPermissions()
    }

    private fun initViews() {
        //Binding all views...
        //CheckBox binding..
        chRememberMe=findViewById(R.id.ch_remember_me)

        //TextInputLayout and Editext binding...
        tilUserName=findViewById(R.id.til_username)
        etUserName=findViewById(R.id.et_username)
        tilPassWord=findViewById(R.id.til_password)
        etPassword=findViewById(R.id.et_password)

        //AppCompatButton declaration...
        btnLogin=findViewById(R.id.btn_signin)
        btnLoginWithOtp = findViewById(R.id.btn_register)

        //setOnClickListeners..
        btnLogin?.setOnClickListener(this)
        btnLoginWithOtp?.setOnClickListener(this)
        chRememberMe?.setOnClickListener(this)

        if(applicationContext?.let { CommonMethods.getPrefrence(it,AllKeys.REMEMBER_ME).equals("1") } == true) {
            chRememberMe?.isChecked=true
            etUserName?.setText(CommonMethods.getPrefrence(applicationContext,AllKeys.ET_USERNAME))
            etPassword?.setText(CommonMethods.getPrefrence(applicationContext,AllKeys.ET_PASSWORD))
        }else {
            chRememberMe?.isChecked=false
            etUserName?.setText("")
            etPassword?.setText("")
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_signin -> {
                if (isValidated()) {
                    //authenticate User...
                    if (CommonMethods.isNetworkAvailable(applicationContext)) {
                        login()
                    } else {
                        CommonMethods.showDialogForError(applicationContext, AllKeys.NO_INTERNET_AVAILABLE)
                    }
                }
            }

            R.id.btn_register -> {
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            R.id.ch_remember_me -> {
                 if(isValidated()){
                    if(chRememberMe?.isChecked == true){
                        applicationContext?.let { CommonMethods.setPreference(it,AllKeys.REMEMBER_ME,"1") }
                        applicationContext?.let { CommonMethods.setPreference(it,AllKeys.ET_USERNAME,etUserName?.text.toString()) }
                        applicationContext?.let { CommonMethods.setPreference(it,AllKeys.ET_PASSWORD,etPassword?.text.toString()) }
                    }else{
                        applicationContext?.let { CommonMethods.setPreference(it,AllKeys.REMEMBER_ME,"0") }
                        applicationContext?.let { CommonMethods.setPreference(it,AllKeys.ET_USERNAME,AllKeys.DNF) }
                        applicationContext?.let { CommonMethods.setPreference(it,AllKeys.ET_PASSWORD,AllKeys.DNF) }
                    }
                }
            }
        }
    }

    private fun login() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                user = database.userDao().getUserLogin(etUserName?.text.toString(),etPassword?.text.toString())
                Log.e(TAG, "initViews: $user")
            }

            withContext(Dispatchers.Main){
                if(user?.mobileNumber==etUserName?.text.toString() && user?.password==etPassword?.text.toString()){
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.USERNAME,etUserName?.text.toString())
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.PASSWORD,etPassword?.text.toString())

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }else{
                    CommonMethods.showDialogForError(this@LoginActivity,"Invalid Login!")
                }
            }
        }
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_SMS,
            ),
            PERMISSION_ID)
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "All Permsission granted...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidated():Boolean{
        if(etUserName?.text.toString().isEmpty()){
            tilUserName?.error="Mobile Number Required!"
            tilUserName?.requestFocus()
            return false
        }

        if(etUserName?.text.toString().length!=10){
            tilUserName?.error="Invalid Mobile Number!"
            tilUserName?.requestFocus()
            return false
        }

        if(etPassword?.text.toString().isEmpty()){
            tilPassWord?.error="Password required!"
            tilPassWord?.requestFocus()
            return false
        }
        return true
    }
}