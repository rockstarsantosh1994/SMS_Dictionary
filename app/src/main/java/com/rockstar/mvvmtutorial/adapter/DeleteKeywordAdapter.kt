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
import com.rockstar.mvvmtutorial.entity.Keywords

class DeleteKeywordAdapter(private var context:Context,
                           private var keywords:ArrayList<Keywords>,
                           private var deleteKeyword: DeleteKeyword
): RecyclerView.Adapter<DeleteKeywordAdapter.AllMessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMessagesViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.row_all_messages,parent,false)
        return AllMessagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllMessagesViewHolder, position: Int) {
        holder.tvSenderName.text= keywords[position].keyword

        holder.tvSenderMessage.visibility=View.GONE

        holder.llMessageClick.setOnClickListener {
           deleteKeyword.deleteKeyword(keywords[position],position)
        }
    }

    override fun getItemCount(): Int {
        return keywords.size
    }

    class AllMessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvSenderName:TextView=itemView.findViewById(R.id.tv_sender_name)
        var tvSenderMessage:TextView=itemView.findViewById(R.id.tv_sender_message)
        var llMessageClick:LinearLayout=itemView.findViewById(R.id.ll_message_click)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(context: Context?, data: ArrayList<Keywords>) {
        this.context = context!!
        this.keywords = data
        notifyDataSetChanged()
    }

    interface DeleteKeyword{
        fun deleteKeyword(keywords: Keywords, position: Int)
    }
}