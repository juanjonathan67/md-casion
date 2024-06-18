package com.example.casion.views.form

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.casion.R
import com.example.casion.databinding.ActivityFormOnboardBinding
import com.example.casion.views.form.diabetes.DiabetesActivity
import com.example.casion.views.form.jantung.JantungActivity

class FormOnboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormOnboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.diabetesButton.setOnClickListener {
            startActivity(Intent(this, DiabetesActivity::class.java))
        }
        binding.jantungButton.setOnClickListener {
            startActivity(Intent(this, JantungActivity::class.java))
        }
    }
}