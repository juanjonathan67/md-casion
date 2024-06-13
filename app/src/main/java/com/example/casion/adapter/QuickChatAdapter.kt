package com.example.casion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.R
import com.example.casion.data.QuickChatData

class QuickChatAdapter(
    private var mList: List<QuickChatData>,
    private val contentTextSize: Float,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<QuickChatAdapter.QuickChatViewHolder>() {

    inner class QuickChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTv: TextView = itemView.findViewById(R.id.titleTv)
        val containerLinearLayout: LinearLayout = itemView.findViewById(R.id.containerLinearLayout)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)

        fun collapseExpandedView() {
            containerLinearLayout.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuickChatAdapter.QuickChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quickchat, parent, false)
        return QuickChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuickChatAdapter.QuickChatViewHolder, position: Int) {
        val quickChatData = mList[position]
        holder.titleTv.text = quickChatData.title

        holder.containerLinearLayout.removeAllViews()

        for (content in quickChatData.content) {
            val contentLinearLayout = LinearLayout(holder.itemView.context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(8, 8, 8, 8)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            val textView = TextView(holder.itemView.context).apply {
                text = content
                setPadding(8, 8, 8, 8)
                textSize = contentTextSize
                setOnClickListener {
                    onItemClick(content)
                }
            }
            contentLinearLayout.addView(textView)
            holder.containerLinearLayout.addView(contentLinearLayout)
        }

        val isExpand: Boolean = quickChatData.isExpand
        holder.containerLinearLayout.visibility = if (isExpand) View.VISIBLE else View.GONE

        holder.constraintLayout.setOnClickListener {
            isAnyItemExpanded(position)
            quickChatData.isExpand = !quickChatData.isExpand
            notifyItemChanged(position, Unit)
        }
    }

    private fun isAnyItemExpanded(position: Int) {
        val temp = mList.indexOfFirst {
            it.isExpand
        }

        if (temp >= 0 && temp != position) {
            mList[temp].isExpand = false
            notifyItemChanged(temp, 0)
        }
    }

    override fun onBindViewHolder(
        holder: QuickChatViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == 0) {
            holder.collapseExpandedView()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
