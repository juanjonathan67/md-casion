package com.example.casion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.data.remote.response.DiseasesItem
import com.example.casion.databinding.ItemMedcheckDateBinding
import com.example.casion.util.Time
import com.example.casion.views.history.MedCheckHistoryItem

class MedCheckHistoryParentAdapter (
    var medCheckHistoryItemList: List<MedCheckHistoryItem>,
    private val onChildClick: (DiseasesItem) -> Unit
) : RecyclerView.Adapter<MedCheckHistoryParentAdapter.ParentViewHolder>() {
    inner class ParentViewHolder(private val binding: ItemMedcheckDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(medCheckHistoryItem: MedCheckHistoryItem) {
            binding.tvHistoryDate.text = Time.getReadableDateFromDate(medCheckHistoryItem.date)
            val childAdapter = MedCheckHistoryChildAdapter(medCheckHistoryItem.diseasesItem)
            binding.rvHistory.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = childAdapter
            }
            childAdapter.onItemClick = onChildClick
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemMedcheckDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(medCheckHistoryItemList[position])
    }

    override fun getItemCount(): Int = medCheckHistoryItemList.size

    fun updateHistoryItems(newMedCheckHistoryItemList: List<MedCheckHistoryItem>) {
        medCheckHistoryItemList = newMedCheckHistoryItemList
        notifyDataSetChanged()
    }
}