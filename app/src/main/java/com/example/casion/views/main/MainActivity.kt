package com.example.casion.views.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.casion.R
import com.example.casion.adapter.MessageAdapter
import com.example.casion.adapter.QuickChatAdapter
import com.example.casion.data.MessageData
import com.example.casion.data.QuickChatData
import com.example.casion.databinding.ActivityMainBinding
import com.example.casion.util.BotResponse
import com.example.casion.util.Constant.RECEIVE_ID
import com.example.casion.util.Constant.SEND_ID
import com.example.casion.util.Time
import com.example.casion.views.signup.SignUpActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mList = ArrayList<QuickChatData>()
    private lateinit var quickChatAdapter: QuickChatAdapter
    private var isRecyclerViewVisible = false
    private lateinit var messageAdapter: MessageAdapter
    var messageList = mutableListOf<MessageData>()

    //QuickChat Dummy Data
    private fun addDataToList() {
        mList.add(QuickChatData("Kepala", listOf("Pusing", "Migren", "Sakit Kepala")))
        mList.add(QuickChatData("Perut", listOf("Mual", "Kram", "Sakit Perut")))
        mList.add(QuickChatData("Mata", listOf("Mata Merah", "Mata Kering", "Nyeri Mata")))
        mList.add(QuickChatData("Lambung", listOf("Sakit Maag", "Perut Kembung")))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.quickchat.visibility = View.GONE

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        addDataToList()
        quickChatAdapter = QuickChatAdapter(mList, contentTextSize = 16f) { selectedText ->
            val currentText = binding.etMessage.text.toString()
            binding.etMessage.setText(
                if (currentText.isEmpty()) selectedText else "$currentText $selectedText,"
            )
            binding.etMessage.setSelection(binding.etMessage.text.length)
        }
        binding.recyclerView.adapter = quickChatAdapter
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

        recyclerView()

        clickEvents()
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

    private fun clickEvents() {

        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        binding.etMessage.setOnClickListener {
            lifecycleScope.launch {
                delay(100)

                binding.quickchat.visibility = View.VISIBLE

                withContext(Dispatchers.Main) {
                    binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)

                }
            }
        }
    }

    private fun recyclerView() {
        messageAdapter = MessageAdapter()
        binding.chatRecyclerView.adapter = messageAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            messageList.add(MessageData(message, SEND_ID, timeStamp))
            binding.etMessage.setText("")

            messageAdapter.insertMessage(MessageData(message, SEND_ID, timeStamp))
            binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)

            binding.imageView.visibility = View.GONE

            botResponse(message)

            binding.quickchat.visibility = View.GONE
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        lifecycleScope.launch {
            delay(1000)

            val response = BotResponse.responses(message)

            withContext(Dispatchers.Main) {
                messageList.add(MessageData(response, RECEIVE_ID, timeStamp))

                messageAdapter.insertMessage(MessageData(response, RECEIVE_ID, timeStamp))

                binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }
    }
}
