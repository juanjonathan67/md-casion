package com.example.casion.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.data.remote.response.DiseasesItem
import com.example.casion.databinding.ItemHistoryMedcheckBinding
import com.example.casion.util.Time
import com.example.casion.util.capitalizeWords
import java.util.Locale

class MedCheckHistoryChildAdapter (
    private var diseasesList: List<DiseasesItem>
) : RecyclerView.Adapter<MedCheckHistoryChildAdapter.DiseaseViewHolder>(){

    var onItemClick: ((DiseasesItem) -> Unit)? = null

    inner class DiseaseViewHolder(private val binding: ItemHistoryMedcheckBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (diseasesItem: DiseasesItem) {
            val category = "Prediksi ${diseasesItem.category.replaceFirstChar { it.uppercaseChar() }}"
            binding.titleHistory.text = category
            when (diseasesItem.category) {
                "general" -> {
                    binding.suspectDiagnose.text = diseasesItem.name.capitalizeWords()
                    binding.predictPercentage.text = diseasesItem.confidenceScore
                }
                "diabetes" -> {
                    when (diseasesItem.name) {
                        "0" -> binding.suspectDiagnose.text = "Normal"
                        "1" -> binding.suspectDiagnose.text = "Prediabetes"
                        "2" -> binding.suspectDiagnose.text = "Diabetes"
                        else -> binding.suspectDiagnose.text = "None"
                    }
                    binding.predictPercentage.text = String.format(Locale.getDefault(), "%.2f%%", diseasesItem.confidenceScore.toDouble())
                }
                "jantung" -> {
                    binding.suspectDiagnose.text = diseasesItem.name.capitalizeWords()
                    binding.predictPercentage.text = "${diseasesItem.confidenceScore}%"
                }
            }

            binding.timeHistory.text = Time.getTimeFromZonedDateTime(diseasesItem.createdAt)

            binding.selengkapnyaButton.setOnClickListener {
                onItemClick?.invoke(diseasesItem)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedCheckHistoryChildAdapter.DiseaseViewHolder {
        val binding = ItemHistoryMedcheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MedCheckHistoryChildAdapter.DiseaseViewHolder,
        position: Int
    ) {
        holder.bind(diseasesList[position])
    }

    override fun getItemCount(): Int = diseasesList.size

}