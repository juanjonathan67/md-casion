package com.example.casion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.data.remote.response.ChatsItem
import com.example.casion.databinding.ItemHistoryDateBinding
import com.example.casion.util.Time
import com.example.casion.views.history.HistoryItem

class ChatHistoryParentAdapter(
    var historyItems: List<HistoryItem>,
    private val onChildClick: (ChatsItem) -> Unit
) : RecyclerView.Adapter<ChatHistoryParentAdapter.ParentViewHolder>() {

    inner class ParentViewHolder(private val binding: ItemHistoryDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: HistoryItem) {
            binding.tvHistoryDate.text = Time.getReadableDateFromDate(historyItem.date)
            val childAdapter = ChatHistoryChildAdapter(historyItem.chats)
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
        holder.bind(historyItems[position])
    }

    override fun getItemCount(): Int = historyItems.size

    fun updateHistoryItems(newHistoryItems: List<HistoryItem>) {
        historyItems = newHistoryItems
        notifyDataSetChanged()
    }
}