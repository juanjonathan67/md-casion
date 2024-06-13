package com.example.casion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.R
import com.example.casion.data.MessageData
import com.example.casion.util.Constant.RECEIVE_ID
import com.example.casion.util.Constant.SEND_ID

class MessageAdapter: RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var messageList = mutableListOf<MessageData>()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                messageList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chatbot, parent, false))
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when (currentMessage.id) {
            SEND_ID -> {
                holder.itemView.findViewById<LinearLayout>(R.id.user_message).apply {
                    findViewById<TextView>(R.id.tv_user_message).text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.itemView.findViewById<LinearLayout>(R.id.bot_message).visibility = View.GONE
            }
            RECEIVE_ID -> {
                holder.itemView.findViewById<LinearLayout>(R.id.bot_message).apply {
                    findViewById<TextView>(R.id.tv_bot_message).text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.itemView.findViewById<LinearLayout>(R.id.user_message).visibility = View.GONE
            }
        }
    }

    fun insertMessage(message: MessageData) {
        this.messageList.add(message)
        notifyItemInserted(messageList.size)
    }
}