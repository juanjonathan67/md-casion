package com.example.casion.views.form.diabetes

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.casion.R
import com.example.casion.databinding.ActivityDiabetesBinding
import com.example.casion.views.main.MainActivity

class DiabetesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiabetesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiabetesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }
    }
}