package com.rockstar.mvvmtutorial.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rockstar.mvvmtutorial.R
import com.rockstar.mvvmtutorial.UserDataBase
import com.rockstar.mvvmtutorial.adapter.AllMessagesAdapter
import com.rockstar.mvvmtutorial.data_model.SmsDataClass
import com.rockstar.mvvmtutorial.entity.Keywords
import com.rockstar.mvvmtutorial.utitlity.CommonMethods
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpamMessages : Fragment(), View.OnClickListener {

    lateinit var database: UserDataBase

    //RecyclerView declaration...
    private var rvAllMessages: RecyclerView? = null

    //TextView declaration..
    private var tvNoDataFound: TextView? = null

    //TextInputLayout declaration..
    private var tilAddKeyWord:TextInputLayout?=null

    //TextInputEditText declaration..
    private var etAddKeyWord:TextInputEditText?=null

    //ApCompatButton declration...
    private var btnAddKeyWord:AppCompatButton?=null
    private var btnViewKeyWord:AppCompatButton?=null
    private var btnAdd:AppCompatButton?=null

    //LinearLayout declaration..
    private var llAddKeyWordView:LinearLayout?=null

    private var smsDataClassList=ArrayList<SmsDataClass>()

    private var allMessagesAdapter: AllMessagesAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_spam_messages, container, false)
        database = UserDataBase.getDatabase(requireContext())

        //basic intialistion
        initViews(view)

        if (arguments?.getParcelableArrayList<SmsDataClass>("sms_list") != null) {
            smsDataClassList.addAll(arguments?.getParcelableArrayList("sms_list")!!)

        }
        return view
    }

    private fun initViews(view: View?) {
        //RecyclerView binding..
        rvAllMessages=view?.findViewById(R.id.rv_all_messages)
        rvAllMessages?.layoutManager= LinearLayoutManager(context)

        //TextView binding..
        tvNoDataFound=view?.findViewById(R.id.tv_no_data_found)

        //TextInputLayout binding..
        tilAddKeyWord=view?.findViewById(R.id.til_add_keyword)

        //TextInputEditText binding..
        etAddKeyWord=view?.findViewById(R.id.et_add_keyword)

        //LinearLayout declaration..
        llAddKeyWordView=view?.findViewById(R.id.ll_add_keyword_view)

        //AppCompatButton binding..
        btnAddKeyWord=view?.findViewById(R.id.btn_add_keyword)
        btnViewKeyWord=view?.findViewById(R.id.btn_view_keyword)
        btnAdd=view?.findViewById(R.id.btn_add)

        //ClickListeners..
        btnAddKeyWord?.setOnClickListener(this)
        btnViewKeyWord?.setOnClickListener(this)
        btnAdd?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add->{
                if(etAddKeyWord?.text.toString().isEmpty()){
                    tilAddKeyWord?.error="Keyword Required!"
                    tilAddKeyWord?.requestFocus()
                }else{
                    GlobalScope.launch {
                        withContext(Dispatchers.IO){
                            database.userDao().insertDictionaryWord(Keywords(0,etAddKeyWord?.text.toString()))
                        }
                        withContext(Dispatchers.Main){
                            showDialogForSuccess(requireContext(),"Keyword added to dictionary!")
                            etAddKeyWord?.text=null
                            llAddKeyWordView?.visibility=View.GONE
                        }
                    }
                }
            }

            R.id.btn_add_keyword->{
                if(llAddKeyWordView?.visibility==View.GONE){
                    llAddKeyWordView?.visibility=View.VISIBLE
                }else{
                    llAddKeyWordView?.visibility=View.GONE
                }
            }

            R.id.btn_view_keyword->{

            }
        }
    }

    fun showDialogForSuccess(context: Context, message: String) {
        val mDialog: MaterialDialog = MaterialDialog.Builder(context as Activity)
            .setTitle(message) // .setMessage("Thank you for your interest in MACSI, Your application is being processed. We will get back to you in timely manner")
            .setCancelable(false)
            .setAnimation(R.raw.success_exploration)
            .setPositiveButton("Ok"
            ) { dialogInterface: dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface?, which: Int ->
                // Delete Operation
                dialogInterface?.dismiss()
            } //.setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss())
            .build()

        // Show Dialog
        mDialog.show()
    }
}