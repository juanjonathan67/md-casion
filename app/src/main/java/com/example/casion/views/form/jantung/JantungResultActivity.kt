package com.example.casion.views.form.jantung

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.casion.R
import com.example.casion.data.remote.response.Data
import com.example.casion.databinding.ActivityJantungResultBinding
import com.example.casion.util.showToast
import com.example.casion.views.main.MainActivity
import com.example.casion.views.mapview.MapsActivity

class JantungResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJantungResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJantungResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: Data? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PREDICTION_RESULT, Data::class.java)
        } else {
            intent.getParcelableExtra(PREDICTION_RESULT)
        }

        if (data != null) {
            setResult(data)
        }

        clickEvents()
    }

    private fun setResult(data: Data) {
        when (data.result) {
            "low chance" -> {
                binding.gaugeJantung.setImageResource(R.drawable.gauge_low)
            }
            "high chance" -> {
                binding.gaugeJantung.setImageResource(R.drawable.gauge_high)
            }
            else -> {
                showToast(this, "Hasil prediksi tidak valid")
            }
        }

        binding.persentasePeluangJantung.text = data.confidenceScore
        binding.descriptionJantung.text = data.description
        binding.sugesstionJantung.text = data.suggestion
    }

    private fun clickEvents() {
        binding.PuskesmasButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.BacktoHomeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    companion object {
        const val PREDICTION_RESULT = "prediction_result"
    }
}