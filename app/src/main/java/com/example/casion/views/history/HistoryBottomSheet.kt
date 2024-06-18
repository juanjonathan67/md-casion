package com.example.casion.views.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.casion.R
import com.example.casion.data.remote.response.DiseasesItem
import com.example.casion.databinding.FragmentBottomsheetBinding
import com.example.casion.util.Time
import com.example.casion.util.showToast
import com.example.casion.views.main.MainActivity
import com.example.casion.views.mapview.MapsActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale

class HistoryBottomSheet(private var diseasesItem: DiseasesItem): BottomSheetDialogFragment() {
    private var _binding: FragmentBottomsheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomsheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (diseasesItem.category) {
            "general" -> {
                binding.tvGeneral.text = diseasesItem.name
                binding.tvGeneral.visibility = View.VISIBLE
                binding.gauge.visibility = View.GONE
                binding.persentasePeluang.text = diseasesItem.confidenceScore
            }
            "diabetes" -> {
                setGaugeDiabetes(Integer.parseInt(diseasesItem.name))
                binding.persentasePeluang.text = String.format(Locale.getDefault(), "%.2f%%", diseasesItem.confidenceScore.toDouble())
            }
            "jantung" -> {
                setGaugeHeart(diseasesItem.name)
                val percent = "${diseasesItem.confidenceScore}%"
                binding.persentasePeluang.text = percent
            }
        }


        binding.timeDate.text = Time.getDateTimeFromZonedDateTime(diseasesItem.createdAt)
        binding.description.text = diseasesItem.description
        binding.sugesstion.text = diseasesItem.suggestion

        clickEvents()
    }

    private fun setGaugeDiabetes(diabetesType: Int) {
        when(diabetesType) {
            0 -> {
                binding.gauge.setImageResource(R.drawable.gauge_normal)
            }
            1 -> {
                binding.gauge.setImageResource(R.drawable.gauge_prediabetes)
            }
            2 -> {
                binding.gauge.setImageResource(R.drawable.gauge_diabetes)
            }
            else -> {
                showToast(requireContext(), "Hasil prediksi tidak valid")
            }
        }
    }

    private fun setGaugeHeart(heartType: String) {
        when(heartType) {
            "low chance" -> {
                binding.gauge.setImageResource(R.drawable.gauge_low)
            }
            "high chance" -> {
                binding.gauge.setImageResource(R.drawable.gauge_high)
            }
            else -> {
                showToast(requireContext(), "Hasil prediksi tidak valid")
            }
        }
    }

    private fun clickEvents() {
        binding.PuskesmasButton.setOnClickListener {
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}