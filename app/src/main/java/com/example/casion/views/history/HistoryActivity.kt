package com.example.casion.views.history

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentPagerAdapter
import com.example.casion.R
import com.example.casion.adapter.ViewHistoryAdapter
import com.example.casion.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewHistoryAdapter = ViewHistoryAdapter(
            supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewHistoryAdapter.addFragment(HistoryChatFragment(), "Chat")
        viewHistoryAdapter.addFragment(HistoryMedCheckupFragment(), "Pemeriksaan Medis")

        binding.viewHistory.adapter = viewHistoryAdapter
        binding.tabBarHistory.setupWithViewPager(binding.viewHistory)
    }
}