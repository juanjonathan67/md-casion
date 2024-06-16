package com.example.casion.views.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.casion.adapter.ChatHistoryParentAdapter
import com.example.casion.data.result.Result
import com.example.casion.databinding.ActivityHistoryBinding
import com.example.casion.util.Time
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.showToast
import com.example.casion.viewmodel.DatabaseViewModel
import com.example.casion.views.main.MainActivity
import com.example.casion.views.main.MainActivity.Companion.CHAT_HISTORY_ITEM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private val databaseViewModel by viewModels<DatabaseViewModel> { ViewModelFactory.getDatabaseInstance(this) }
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var chatHistoryParentAdapter: ChatHistoryParentAdapter
    private var historyItemList = ArrayList<HistoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatHistoryParentAdapter = ChatHistoryParentAdapter(emptyList()) { chatItem ->
            val intent = Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(CHAT_HISTORY_ITEM, chatItem)
            startActivity(intent)
        }
        binding.rvDateHistory.layoutManager = LinearLayoutManager(this)
        binding.rvDateHistory.adapter = chatHistoryParentAdapter

        getChatHistory()
    }

    private fun getChatHistory() {
        lifecycleScope.launch {
            delay(250)
        }

        databaseViewModel.getChatHistory().observe(this) { result ->
            when (result) {
                is Result.Error -> { showToast(this, result.error) }
                Result.Loading -> {}
                is Result.Success -> {
                    val groupedChats = result.data.chats.groupBy { chat ->
                        // Extract the date part from dateTime
                        Time.getDateFromDateTime(chat.dateTime)
                    }

                    for ((date, chats) in groupedChats) {
                        historyItemList.add(HistoryItem(chats, date))
                    }

                    chatHistoryParentAdapter.updateHistoryItems(historyItemList)
                }
            }
        }
    }
}