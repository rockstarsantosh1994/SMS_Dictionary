package com.rockstar.mvvmtutorial.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rockstar.mvvmtutorial.R
import com.rockstar.mvvmtutorial.data_model.SmsDataClass
import com.rockstar.mvvmtutorial.utitlity.CommonMethods

class AllMessagesAdapter(private var context:Context,
                         private var smsDataClassList:ArrayList<SmsDataClass>
): RecyclerView.Adapter<AllMessagesAdapter.AllMessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMessagesViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.row_all_messages,parent,false)
        return AllMessagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllMessagesViewHolder, position: Int) {
        holder.tvSenderName.text= smsDataClassList[position]._address
        holder.tvSenderMessage.text= smsDataClassList[position]._msg

        holder.llMessageClick.setOnClickListener {
            CommonMethods.showDialogForError(context,smsDataClassList[position]._msg)
        }
    }

    override fun getItemCount(): Int {
        return smsDataClassList.size
    }

    class AllMessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvSenderName:TextView=itemView.findViewById(R.id.tv_sender_name)
        var tvSenderMessage:TextView=itemView.findViewById(R.id.tv_sender_message)
        var llMessageClick:LinearLayout=itemView.findViewById(R.id.ll_message_click)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(context: Context?, data: ArrayList<SmsDataClass>) {
        this.context = context!!
        this.smsDataClassList = data
        notifyDataSetChanged()
    }
}