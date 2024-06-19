package com.example.casion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.data.remote.response.ChatsItem
import com.example.casion.databinding.ItemHistoryDateBinding
import com.example.casion.util.Time
import com.example.casion.views.history.ChatHistoryItem

class ChatHistoryParentAdapter(
    var chatHistoryItemList: List<ChatHistoryItem>,
    private val onChildClick: (ChatsItem) -> Unit
) : RecyclerView.Adapter<ChatHistoryParentAdapter.ParentViewHolder>() {

    inner class ParentViewHolder(private val binding: ItemHistoryDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatHistoryItem: ChatHistoryItem) {
            binding.tvHistoryDate.text = Time.getReadableDateFromDate(chatHistoryItem.date)
            val childAdapter = ChatHistoryChildAdapter(chatHistoryItem.chats)
            binding.rvHistory.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = childAdapter
            }
            childAdapter.onItemClick = onChildClick
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemHistoryDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(chatHistoryItemList[position])
    }

    override fun getItemCount(): Int = chatHistoryItemList.size

    fun updateHistoryItems(newChatHistoryItems: List<ChatHistoryItem>) {
        chatHistoryItemList = newChatHistoryItems
        notifyDataSetChanged()
    }
}