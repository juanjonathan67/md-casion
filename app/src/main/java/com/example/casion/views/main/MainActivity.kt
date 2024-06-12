package com.example.casion.views.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.casion.R
import com.example.casion.adapter.QuickChatAdapter
import com.example.casion.data.QuickChatData
import com.example.casion.databinding.ActivityMainBinding
import com.example.casion.views.signup.SignUpActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mList = ArrayList<QuickChatData>()
    private lateinit var adapter: QuickChatAdapter
    private var isRecyclerViewVisible = false

    //QuickChat Data
    private fun addDataToList() {
        mList.add(QuickChatData("Kepala", listOf("Pusing", "Migren", "Sakit Kepala")))
        mList.add(QuickChatData("Perut", listOf("Mual", "Kram", "Sakit Perut")))
        mList.add(QuickChatData("Tangan", listOf("Kebas", "Sakit Sendi", "Bengkak")))
        mList.add(QuickChatData("Kaki", listOf("Bengkak", "Kram", "Lemah")))
        mList.add(QuickChatData("Mata", listOf("Iritasi", "Penglihatan Kabur", "Mata Merah")))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        addDataToList()
        adapter = QuickChatAdapter(mList, contentTextSize = 16f)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.visibility = View.GONE

        val drawerButton = binding.drawerButton
        val signUpButton = binding.SignUpbutton
        val headerView = binding.navView.getHeaderView(0)
        val headerSignUpButton = headerView.findViewById<MaterialButton>(R.id.header_sign_up_button)

        binding.root.setOnApplyWindowInsetsListener { view, insets ->
            val systemWindowInsets = insets.systemWindowInsets
            binding.drawerLayout.setPadding(0, systemWindowInsets.top, 0, 0)
            insets
        }

        drawerButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        headerSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.quickChatTitle.setOnClickListener {
            toggleRecyclerViewVisibility()
        }
    }

    private fun toggleRecyclerViewVisibility() {
        if (!isRecyclerViewVisible) {
            binding.recyclerView.visibility = View.VISIBLE
            isRecyclerViewVisible = true
        } else {
            binding.recyclerView.visibility = View.GONE
            isRecyclerViewVisible = false
        }
    }
}
