package com.example.casion.views.history

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.casion.adapter.ChatHistoryParentAdapter
import com.example.casion.data.result.Result
import com.example.casion.databinding.FragmentHistoryChatBinding
import com.example.casion.util.Time
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.showToast
import com.example.casion.viewmodel.DatabaseViewModel
import com.example.casion.views.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryChatFragment : Fragment() {
    private val databaseViewModel by viewModels<DatabaseViewModel> { ViewModelFactory.getDatabaseInstance(requireContext()) }
    private var _binding: FragmentHistoryChatBinding? = null
    private val binding get() =_binding!!
    private lateinit var chatHistoryParentAdapter: ChatHistoryParentAdapter
    private var chatHistoryItemList = ArrayList<ChatHistoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatHistoryParentAdapter = ChatHistoryParentAdapter(emptyList()) { chatItem ->
            val intent = Intent(requireContext(), MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(MainActivity.CHAT_HISTORY_ITEM, chatItem)
            startActivity(intent)
        }
        binding.rvDateHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDateHistory.adapter = chatHistoryParentAdapter

        getChatHistory()
    }

    private fun getChatHistory() {
        lifecycleScope.launch {
            delay(250)
        }

        databaseViewModel.getChatHistory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> { showToast(requireContext(), result.error) }
                Result.Loading -> {}
                is Result.Success -> {
                    val groupedChats = result.data.chats.groupBy { chat ->
                        // Extract the date part from dateTime
                        Time.getDateFromDateTime(chat.dateTime)
                    }

                    for ((date, chats) in groupedChats) {
                        chatHistoryItemList.add(ChatHistoryItem(chats, date))
                    }

                    chatHistoryParentAdapter.updateHistoryItems(chatHistoryItemList)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}