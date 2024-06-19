package com.example.casion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.data.remote.response.ChatsItem
import com.example.casion.databinding.ItemHistoryBinding
import com.example.casion.util.Time

class ChatHistoryChildAdapter(
    private var chatList: List<ChatsItem>
) : RecyclerView.Adapter<ChatHistoryChildAdapter.ChatViewHolder>() {

    var onItemClick: ((ChatsItem) -> Unit)? = null

    inner class ChatViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatsItem) {
            binding.titleHistory.text = chat.title
            binding.descHistory.text = chat.messages.lastOrNull()?.message ?: "No description"
            binding.timeHistory.text = Time.getTimeFromDateTime(chat.dateTime)

            binding.root.setOnClickListener {
                onItemClick?.invoke(chat)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ChatViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChatViewHolder, position: Int
    ) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int = chatList.size
}