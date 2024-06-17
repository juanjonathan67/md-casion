package com.example.casion.views.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.R
import com.google.android.material.button.MaterialButton

class HistoryMedCheckupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_med_checkup, container, false)

//        ketika selengkapnyaButton diklik, bakal muncul bottomsheetnya
//        val selengkapnyaButton = view.findViewById<MaterialButton>(R.id.selengkapnyaButton)
//        selengkapnyaButton.setOnClickListener {
//            val bottomSheet = HistoryBottomSheet()
//            bottomSheet.show(childFragmentManager, "HistoryBottomSheet")
//        }
//
        return view
    }
}
