package com.rockstar.mvvmtutorial.activity

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.rockstar.mvvmtutorial.R
import com.rockstar.mvvmtutorial.data_model.SmsDataClass
import com.rockstar.mvvmtutorial.fragment.AllMessagesFragment
import com.rockstar.mvvmtutorial.fragment.SpamMessages
import com.rockstar.mvvmtutorial.utitlity.AllKeys
import com.rockstar.mvvmtutorial.utitlity.CommonMethods
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface
import me.ibrahimsn.lib.SmoothBottomBar


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private var smoothBottomBar:SmoothBottomBar?=null

    private var smsDataClassList=ArrayList<SmsDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Basic intialisation..
        initViews()

        //Read All Messages from mobile...
        getAllSms()
    }

    private fun initViews(){
        smoothBottomBar=findViewById(R.id.bottomBar)

        smoothBottomBar?.itemActiveIndex = 0

        //Bottom navigation transaction...
        smoothBottomBar?.setOnItemSelectedListener { i ->
            when (i) {
                0 -> loadAllMessagesFragment()
                1 -> loadSpamMessagesFragment()
                2 -> logout()
            }
        }
    }

    private fun logout() {
        val mDialog = MaterialDialog.Builder(this@MainActivity)
            .setTitle("Warning")
            .setMessage("Are you sure want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialogInterface: DialogInterface, which: Int ->
                // Delete Operation
                CommonMethods.setPreference(this,
                    AllKeys.USERNAME,
                    AllKeys.DNF)
                CommonMethods.setPreference(this,
                    AllKeys.PASSWORD,
                    AllKeys.DNF)

                dialogInterface.dismiss()

                // check if permissions are given
                val intent = Intent(this@MainActivity,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { dialogInterface: DialogInterface, which: Int ->
                // Delete Operation
                dialogInterface.dismiss()
            }
            .build()

        // Show Dialog
        mDialog.show()
    }

    private fun loadAllMessagesFragment() {
        val allMessagesFragment = AllMessagesFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("sms_list", smsDataClassList)
        allMessagesFragment.arguments = bundle
        mLoadFragment(allMessagesFragment)
        supportFragmentManager.popBackStack("", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun loadSpamMessagesFragment() {
        val spamMessages = SpamMessages()
        val bundle = Bundle()
        bundle.putParcelableArrayList("sms_list", smsDataClassList)
        spamMessages.arguments = bundle
        mLoadFragment(spamMessages)
        supportFragmentManager.popBackStack("", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun mLoadFragment(fragment: Fragment?) {
        val tx: FragmentTransaction = supportFragmentManager.beginTransaction()
        tx.replace(R.id.flContent, fragment!!)
        tx.commit()
    }


    @SuppressLint("Range")
    private fun getAllSms(): List<SmsDataClass> {
        var objSms: SmsDataClass
        val message: Uri = Uri.parse("content://sms/inbox")
        val cr: ContentResolver = this.getContentResolver()
        val c: Cursor? = cr.query(message, null, null, null, null)
        this.startManagingCursor(c)
        val totalSMS: Int? = c?.count
        if (c?.moveToFirst() == true) {
            for (i in 0 until totalSMS!!) {
                objSms = SmsDataClass(c.getString(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("address")),
                    c.getString(c.getColumnIndexOrThrow("body")),
                    c.getString(c.getColumnIndex("read")),
                    c.getString(c.getColumnIndexOrThrow("date")),
                    "inbox")
                smsDataClassList.add(objSms)
                c.moveToNext()
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c?.close()
        Log.e(TAG, "getAllSms: "+smsDataClassList.size )
        Log.e(TAG, "getAllSms: "+smsDataClassList.toString() )

        //load all messages if data get..
        loadAllMessagesFragment()

        return smsDataClassList
    }
}
