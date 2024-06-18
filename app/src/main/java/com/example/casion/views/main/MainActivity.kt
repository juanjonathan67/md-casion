package com.example.casion.views.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.casion.R
import com.example.casion.adapter.MessageAdapter
import com.example.casion.adapter.QuickChatAdapter
import com.example.casion.data.MessageData
import com.example.casion.data.QuickChatData
import com.example.casion.data.remote.request.ChatRequest
import com.example.casion.data.remote.request.DiseaseRequest
import com.example.casion.data.remote.request.MessagesItem
import com.example.casion.data.remote.response.ChatsItem
import com.example.casion.data.remote.response.Data
import com.example.casion.data.result.Result
import com.example.casion.databinding.ActivityMainBinding
import com.example.casion.databinding.DrawerHeaderBinding
import com.example.casion.util.BotResponse
import com.example.casion.util.Constant
import com.example.casion.util.Constant.RECEIVE_ID
import com.example.casion.util.Constant.SEND_ID
import com.example.casion.util.Time
import com.example.casion.util.UserPreferences
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.datastore
import com.example.casion.util.showToast
import com.example.casion.viewmodel.AuthViewModel
import com.example.casion.viewmodel.DatabaseViewModel
import com.example.casion.viewmodel.PredictViewModel
import com.example.casion.views.form.FormOnboardActivity
import com.example.casion.views.form.diabetes.DiabetesActivity
import com.example.casion.views.form.diabetes.DiabetesResultActivity
import com.example.casion.views.form.jantung.JantungActivity
import com.example.casion.views.history.HistoryActivity
import com.example.casion.views.mapview.MapsActivity
import com.example.casion.views.setting.SettingActivity
import com.example.casion.views.signup.SignUpActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val databaseViewModel by viewModels<DatabaseViewModel> { ViewModelFactory.getDatabaseInstance(this) }
    private val predictViewModel by viewModels<PredictViewModel> { ViewModelFactory.getPredictInstance(this) }

    private lateinit var binding: ActivityMainBinding
    private var mList = ArrayList<QuickChatData>()
    private lateinit var quickChatAdapter: QuickChatAdapter
    private var isRecyclerViewVisible = false
    private lateinit var messageAdapter: MessageAdapter
    var messageList = mutableListOf<MessageData>()

    //session manager
    private lateinit var prefs: UserPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private var isLoggedIn = false
    private var isChatSaved = false
    private var currentChatId = ""

    //prediction helper variable
    private val pickedSymptoms = ArrayList<String>()
    private var currentPrediction = "No Prediction"

    //QuickChat Dummy Data
    private fun addDataToList() {
        mList.add(QuickChatData("Kulit dan Kuku", BotResponse.skinAndNailsSymptoms.values.toList()))
        mList.add(QuickChatData("Pernapasan", BotResponse.respiratorySymptoms.values.toList()))
        mList.add(QuickChatData("Gastrointestinal", BotResponse.gastrointestinalSymptoms.values.toList()))
        mList.add(QuickChatData("Umum/Sistemik", BotResponse.generalSymptoms.values.toList()))
        mList.add(QuickChatData("Neurologis", BotResponse.neurologicalSymptoms.values.toList()))
        mList.add(QuickChatData("Urinari", BotResponse.urinarySymptoms.values.toList()))
        mList.add(QuickChatData("Kardiovaskular", BotResponse.cardiovascularSymptoms.values.toList()))
        mList.add(QuickChatData("Endokrin", BotResponse.endocrineSymptoms.values.toList()))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        isChatSaved = false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.quickchat.visibility = View.GONE

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        addDataToList()

        quickChatAdapter = QuickChatAdapter(mList, contentTextSize = 16f) { selectedText ->
            pickedSymptoms.add(selectedText)

            val currentText = binding.etMessage.text.toString()
            binding.etMessage.setText(
                if (currentText.isEmpty()) "$selectedText," else "$currentText $selectedText,"
            )
            binding.etMessage.setSelection(binding.etMessage.text.length)
        }
        binding.recyclerView.adapter = quickChatAdapter
        binding.recyclerView.visibility = View.GONE

        binding.root.setOnApplyWindowInsetsListener { _, insets ->
            val systemWindowInsets = insets.systemWindowInsets
            binding.drawerLayout.setPadding(0, systemWindowInsets.top, 0, 0)
            insets
        }

        recyclerView()

        navigationDrawer()

        clickEvents()

        sessionManager()
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
        val headerBinding = DrawerHeaderBinding.bind(binding.navView.getHeaderView(0))

        binding.sendButton.setOnClickListener {
            if(pickedSymptoms.size >= 3) {
                val allMaps = listOf(
                    BotResponse.skinAndNailsSymptoms,
                    BotResponse.respiratorySymptoms,
                    BotResponse.gastrointestinalSymptoms,
                    BotResponse.generalSymptoms,
                    BotResponse.neurologicalSymptoms,
                    BotResponse.urinarySymptoms,
                    BotResponse.cardiovascularSymptoms,
                    BotResponse.endocrineSymptoms
                )

                // Create an ArrayList to store original symptoms
                val originalSymptoms = ArrayList<String>()

                // Iterate over pickedSymptoms and add corresponding original symptoms
                pickedSymptoms.forEach { pickedSymptom ->
                    allMaps.forEach { map ->
                        getKeyFromValue(map, pickedSymptom)?.let { originalSymptoms.add(it) }
                    }
                }

//                 Assuming predictViewModel.predict expects a list of original symptoms
                predictViewModel.predict("general", originalSymptoms).observe(this) { result ->
                    when (result) {
                        is Result.Error -> { botPredictResponse("Terjadi error saat mendiagnosa penyakitmu.") }
                        Result.Loading -> {}
                        is Result.Success -> {
                            val prediction = result.data.data
                            currentPrediction = prediction.result
                            botPredictResponse("Kamu terprediksi memiliki penyakit ${prediction.result} dengan tingkat keyakinan ${prediction.confidenceScore}")
                            botPredictResponse(prediction.description)
                            botPredictResponse(prediction.suggestion)
                            if (isLoggedIn) {
                                databaseViewModel.storeDisease(DiseaseRequest(
                                    "general",
                                    prediction.result,
                                    prediction.description,
                                    prediction.suggestion,
                                    prediction.confidenceScore,
                                    prediction.createdAt
                                ))
                            }
                        }
                    }
                }
            }
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

        binding.drawerButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.SignUpbutton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        headerBinding.headerSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.quickChatTitle.setOnClickListener {
            toggleRecyclerViewVisibility()
        }

        binding.mapView.setOnClickListener {
            startActivity(Intent(this@MainActivity, MapsActivity::class.java))
        }
    }

    private fun sessionManager() {
        firebaseAuth = FirebaseAuth.getInstance()
        val googleUser = firebaseAuth.currentUser
        val headerBinding = DrawerHeaderBinding.bind(binding.navView.getHeaderView(0))
        prefs = UserPreferences.getInstance(this.datastore)

        if (googleUser != null) {
            Glide.with(this)
                .load(googleUser.photoUrl)
                .into(headerBinding.headerProfilePicture)
            headerBinding.headerTitle.text = googleUser.displayName
            headerBinding.headerSignUpButton.visibility = View.GONE
            isLoggedIn = true
        } else {
            val token = runBlocking { prefs.getUserToken().first() }
            if (token.isNotEmpty()) {
                databaseViewModel.getUserDetails().observe(this) { result ->
                    when (result) {
                        is Result.Error -> {
                            runBlocking { prefs.deleteUserToken() }
                        }
                        Result.Loading -> {}
                        is Result.Success -> {
                            headerBinding.headerTitle.text = result.data.userDetails.fullName
                            headerBinding.headerSignUpButton.visibility = View.GONE
                            isLoggedIn = true
                        }
                    }
                }
            }
        }

        val chatHistoryItem: ChatsItem? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CHAT_HISTORY_ITEM, ChatsItem::class.java)
        } else {
            intent.getParcelableExtra(CHAT_HISTORY_ITEM)
        }

        if (chatHistoryItem != null) {
            val messageItems = ArrayList<MessageData>()
            for (message in chatHistoryItem.messages) {
                val newMessage = MessageData(
                    message.message,
                    if (message.bot) RECEIVE_ID else SEND_ID,
                    message.time
                )
                messageItems.add(newMessage)
                messageAdapter.insertMessage(newMessage)
            }
            messageList = messageItems
            currentChatId = chatHistoryItem.chatId
            isChatSaved = true
        }
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

    override fun onRestart() {
        isChatSaved = true
        super.onRestart()
    }

    override fun onStop() {
        // save chat
        if (isLoggedIn && messageList.isNotEmpty() && !isChatSaved) {
            databaseViewModel.storeChat(generateChatRequest()).observe(this) { result ->
                when (result) {
                    is Result.Error -> { showToast(this, result.error) }
                    Result.Loading -> {}
                    is Result.Success -> { currentChatId = result.data.chatId }
                }
            }
        }
        // update chat if chat is saved already
        if (isLoggedIn && messageList.isNotEmpty() && isChatSaved) {
            databaseViewModel.updateChat(currentChatId, generateChatRequest()).observe(this) { result ->
                when (result) {
                    is Result.Error -> { showToast(this, result.error) }
                    Result.Loading -> {}
                    is Result.Success -> {}
                }
            }
        }
        super.onStop()
    }

    private fun generateChatRequest() : ChatRequest {
        val messagesItemList = ArrayList<MessagesItem>()
        for (message in messageList) {
            messagesItemList.add(
                MessagesItem(
                    message.id == RECEIVE_ID,
                    message.time,
                    message.message
                )
            )
        }

        return ChatRequest(
            dateTime = Time.getCurrentDateTime(),
            title = currentPrediction,
            messages = messagesItemList
        )
    }

    private fun recyclerView() {
        messageAdapter = MessageAdapter()
        binding.chatRecyclerView.adapter = messageAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun navigationDrawer() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.chatbaru -> {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    true
                }
                R.id.pemeriksaanmedis -> {
                    startActivity(Intent(this, FormOnboardActivity::class.java))
                    true
                }
                R.id.riwayat -> {
                    if (isLoggedIn) {
                        startActivity(Intent(this, HistoryActivity::class.java))
                        true
                    } else {
                        showToast(this, "Harap login dahulu")
                        false
                    }
                }
                R.id.kebijakanprivasi -> {
                    true
                }
                R.id.pengaturan -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        val timeStamp = Time.getCurrentTime()

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
        val timeStamp = Time.getCurrentTime()

        lifecycleScope.launch {
            delay(1000)

            val response = BotResponse.responses(message, pickedSymptoms)

            withContext(Dispatchers.Main) {
                messageList.add(MessageData(response, RECEIVE_ID, timeStamp))

                messageAdapter.insertMessage(MessageData(response, RECEIVE_ID, timeStamp))

                binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }
    }

    private fun botPredictResponse(message: String) {
        val timestamp = Time.getCurrentTime()

        lifecycleScope.launch {
            delay(500)

            withContext(Dispatchers.Main) {
                messageList.add(MessageData(message, RECEIVE_ID, timestamp))

                messageAdapter.insertMessage(MessageData(message, RECEIVE_ID, timestamp))

                binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount -1)
            }
        }
    }

    private fun getKeyFromValue(map: Map<String, String>, value: String): String? {
        return map.entries.firstOrNull { it.value == value }?.key
    }

    companion object {
        const val CHAT_HISTORY_ITEM = "chat_history_item"
    }
}
