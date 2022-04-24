package com.rockstar.mvvmtutorial.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rockstar.mvvmtutorial.R
import com.rockstar.mvvmtutorial.adapter.AllMessagesAdapter
import com.rockstar.mvvmtutorial.data_model.SmsDataClass
import java.util.*
import kotlin.collections.ArrayList


class AllMessagesFragment : Fragment() {

    private val TAG = "AllMessagesFragment"

    //EditText declaration..
    private var etSearch: EditText? = null

    //RecyclerView declaration...
    private var rvAllMessages: RecyclerView? = null

    //TextView declaration..
    private var tvNoDataFound: TextView? = null

    //LinearLayout declaration..
    private var llMessageView:LinearLayout?=null

    private var smsDataClassList=ArrayList<SmsDataClass>()

    private var allMessagesAdapter:AllMessagesAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_all_messages, container, false)

        //basic intialisation..
        initViews(view)

        if (arguments?.getParcelableArrayList<SmsDataClass>("sms_list") != null) {
            smsDataClassList.addAll(arguments?.getParcelableArrayList("sms_list")!!)

            if(smsDataClassList.size >0){
                llMessageView?.visibility=View.VISIBLE
                tvNoDataFound?.visibility=View.GONE
                allMessagesAdapter= AllMessagesAdapter(requireContext(),smsDataClassList)
                rvAllMessages?.adapter=allMessagesAdapter
            }else{
                llMessageView?.visibility=View.GONE
                tvNoDataFound?.visibility=View.VISIBLE
            }
        }
        return view
    }

    private fun initViews(view: View?) {
        //EditText binding...
        etSearch=view?.findViewById(R.id.et_search)

        //RecyclerView binding..
        rvAllMessages=view?.findViewById(R.id.rv_all_messages)
        rvAllMessages?.layoutManager=LinearLayoutManager(context)

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
        val filteredList1: ArrayList<SmsDataClass> = ArrayList()
        for (item in smsDataClassList) {
            if (text.let { item._address.contains(it, true) } || text.let { item._msg.contains(it, true) }) {
                filteredList1.add(item)
            }
        }
        //Log.e(TAG, "filter: size" + filteredList1.size());
        // Log.e(TAG, "filter: List" + filteredList1.toString());
        allMessagesAdapter?.updateData(requireContext(), filteredList1)
    }
}