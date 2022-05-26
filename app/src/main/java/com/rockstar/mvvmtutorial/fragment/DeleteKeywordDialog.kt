package com.rockstar.mvvmtutorial.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rockstar.mvvmtutorial.BuildConfig
import com.rockstar.mvvmtutorial.R
import com.rockstar.mvvmtutorial.UserDataBase
import com.rockstar.mvvmtutorial.activity.LoginActivity
import com.rockstar.mvvmtutorial.adapter.DeleteKeywordAdapter
import com.rockstar.mvvmtutorial.entity.Keywords
import com.rockstar.mvvmtutorial.utitlity.AllKeys
import com.rockstar.mvvmtutorial.utitlity.CommonMethods
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteKeywordDialog : DialogFragment(),DeleteKeywordAdapter.DeleteKeyword {

    private val TAG = "DeleteKeywordDialog"

    //EditText declaration..
    private var etSearch: EditText? = null

    //RecyclerView declaration...
    private var rvAllKeywords: RecyclerView? = null

    //TextView declaration..
    private var tvNoDataFound: TextView? = null

    //LinearLayout declaration..
    private var llMessageView: LinearLayout?=null

    private var deleteKeywordAdapter: DeleteKeywordAdapter?=null

    val keywordArrayList=ArrayList<Keywords>()

    lateinit var database: UserDataBase

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_all_messages, container, false)
        database = UserDataBase.getDatabase(requireContext())

        //basic intialisation..
        initViews(view)

        val arguments = arguments
        if (BuildConfig.DEBUG && arguments == null) {
            error("Assertion failed")
        }

        if (getArguments()?.getParcelableArrayList<Keywords>("keywords_list") != null) {
            keywordArrayList.addAll(arguments?.getParcelableArrayList("keywords_list")!!)
            deleteKeywordAdapter= DeleteKeywordAdapter(requireContext(),keywordArrayList,this)
            rvAllKeywords?.adapter=deleteKeywordAdapter
        }
        return view
    }

    private fun initViews(view: View?) {
        //EditText binding...
        etSearch=view?.findViewById(R.id.et_search)

        //RecyclerView binding..
        rvAllKeywords=view?.findViewById(R.id.rv_all_messages)
        rvAllKeywords?.layoutManager= LinearLayoutManager(context)

        //TextView binding..
        tvNoDataFound=view?.findViewById(R.id.tv_no_data_found)

        //LinearLayout binding..
        llMessageView=view?.findViewById(R.id.ll_messages_view)

        etSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filterTable(s.toString())
            }
        })
    }

    private fun filterTable(text: String) {
        val filteredList1: ArrayList<Keywords> = ArrayList()
        for (item in keywordArrayList) {
            if (text.let { item.keyword.contains(it, true) } ) {
                filteredList1.add(item)
            }
        }
        //Log.e(TAG, "filter: size" + filteredList1.size());
        // Log.e(TAG, "filter: List" + filteredList1.toString());
        deleteKeywordAdapter?.updateData(requireContext(), filteredList1)
    }

    override fun deleteKeyword(keywords: Keywords, position: Int) {
        val mDialog = MaterialDialog.Builder(requireContext() as Activity)
            .setTitle("Warning")
            .setMessage("Are you sure want to delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialogInterface: DialogInterface, which: Int ->
                GlobalScope.launch {
                    withContext(Dispatchers.IO){
                        database.userDao().deleteKeyword(keywords)
                    }
                    withContext(Dispatchers.Main){
                        keywordArrayList.removeAt(position)
                        deleteKeywordAdapter?.notifyItemRemoved(position)
                        deleteKeywordAdapter?.notifyItemRangeChanged(position, keywordArrayList.size)
                    }
                }

                dialogInterface.dismiss()
            }
            .setNegativeButton("No") { dialogInterface: DialogInterface, which: Int ->
                // Delete Operation
                dialogInterface.dismiss()
            }
            .build()

        // Show Dialog
        mDialog.show()
    }
}