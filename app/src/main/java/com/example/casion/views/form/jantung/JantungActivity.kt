package com.example.casion.views.form.jantung

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.casion.R
import com.example.casion.databinding.ActivityJantungBinding
import com.example.casion.views.main.MainActivity

class JantungActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJantungBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJantungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }
    }
}