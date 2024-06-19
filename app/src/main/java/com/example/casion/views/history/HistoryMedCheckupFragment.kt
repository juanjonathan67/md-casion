package com.example.casion.views.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casion.R
import com.example.casion.adapter.MedCheckHistoryParentAdapter
import com.example.casion.data.result.Result
import com.example.casion.databinding.FragmentHistoryMedCheckupBinding
import com.example.casion.util.Time
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.showToast
import com.example.casion.viewmodel.DatabaseViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryMedCheckupFragment : Fragment() {
    private val databaseViewModel by viewModels<DatabaseViewModel> { ViewModelFactory.getDatabaseInstance(requireContext()) }
    private var _binding: FragmentHistoryMedCheckupBinding? = null
    private val binding get() = _binding!!
    private lateinit var medCheckHistoryParentAdapter: MedCheckHistoryParentAdapter
    private var medCheckHistoryItemList = ArrayList<MedCheckHistoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryMedCheckupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        medCheckHistoryParentAdapter = MedCheckHistoryParentAdapter(emptyList()) { diseaseItem ->
            val bottomSheet = HistoryBottomSheet(diseaseItem)
            bottomSheet.show(childFragmentManager, "HistoryBottomSheet")
        }
        binding.rvDateHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDateHistory.adapter = medCheckHistoryParentAdapter

        getDiseaseHistory()
    }

    private fun getDiseaseHistory() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            delay(250)
        }

        databaseViewModel.getDiseaseHistory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(requireContext(), result.error)
                }
                Result.Loading -> {}
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val groupedChats = result.data.diseases.groupBy { disease ->
                        // Extract the date part from dateTime
                        Time.getDateFromZonedDateTime(disease.createdAt)
                    }

                    for ((date, disease) in groupedChats) {
                        medCheckHistoryItemList.add(MedCheckHistoryItem(disease, date))
                    }

                    medCheckHistoryParentAdapter.updateHistoryItems(medCheckHistoryItemList)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
