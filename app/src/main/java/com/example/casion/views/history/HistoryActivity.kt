package com.example.casion.views.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.casion.adapter.ChatHistoryParentAdapter
import com.example.casion.adapter.ViewHistoryAdapter
import com.example.casion.data.result.Result
import com.example.casion.databinding.ActivityHistoryBinding
import com.example.casion.util.Time
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.showToast
import com.example.casion.viewmodel.DatabaseViewModel
import com.example.casion.views.main.MainActivity
import com.example.casion.views.main.MainActivity.Companion.CHAT_HISTORY_ITEM
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewHistory.adapter = ViewHistoryAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabBarHistory, binding.viewHistory) { tab, position ->
            when (position) {
                0 -> { tab.text = "Chat" }
                1 -> { tab.text = "Penyakit" }
            }
        }.attach()

        binding.backButton.setOnClickListener { finish() }

    }
}