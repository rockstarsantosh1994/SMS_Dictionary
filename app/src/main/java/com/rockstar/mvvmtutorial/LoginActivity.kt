package com.rockstar.mvvmtutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rockstar.mvvmtutorial.utitlity.AllKeys
import com.rockstar.mvvmtutorial.utitlity.CommonMethods

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    private val TAG = "LoginActivity"

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Basic intialisation...
        initViews()
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
        Toast.makeText(applicationContext, "Login Soon...", Toast.LENGTH_LONG).show()
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