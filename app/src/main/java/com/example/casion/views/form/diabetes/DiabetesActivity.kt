package com.example.casion.views.form.diabetes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.casion.data.remote.request.DiseaseRequest
import com.example.casion.data.remote.response.Data
import com.example.casion.data.result.Result
import com.example.casion.databinding.ActivityDiabetesBinding
import com.example.casion.util.Time
import com.example.casion.util.UserPreferences
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.datastore
import com.example.casion.util.showToast
import com.example.casion.viewmodel.DatabaseViewModel
import com.example.casion.viewmodel.PredictViewModel
import com.example.casion.views.form.diabetes.DiabetesResultActivity.Companion.PREDICTION_RESULT
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.math.pow

class DiabetesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiabetesBinding
    private val databaseViewModel by viewModels<DatabaseViewModel> { ViewModelFactory.getDatabaseInstance(this) }
    private val predictViewModel by viewModels<PredictViewModel> { ViewModelFactory.getPredictInstance(this) }

    //session manager
    private lateinit var prefs: UserPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiabetesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        sessionManager()

        clickEvents()
    }

    private fun sessionManager() {
        firebaseAuth = FirebaseAuth.getInstance()
        val googleUser = firebaseAuth.currentUser
        prefs = UserPreferences.getInstance(this.datastore)
        if (googleUser != null) {

            isLoggedIn = true
        } else {
            val token = runBlocking { prefs.getUserToken().first() }
            if (token.isNotEmpty()) {
                getUserDetails()
            }
        }
    }

    private fun getUserDetails() {
        databaseViewModel.getUserDetails().observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    runBlocking { prefs.deleteUserToken() }
                }
                Result.Loading -> {}
                is Result.Success -> {
                    val userDetails = result.data.userDetails
                    if (userDetails.birthday != null) {
                        binding.etUmur.setText(Time.getAgeFromLocalDate(userDetails.birthday))
                    }
                    if (userDetails.gender == "true") {
                        binding.radioMale.isChecked = true
                    } else if (userDetails.gender == "false") {
                        binding.radioFemale.isChecked = false
                    }
                    isLoggedIn = true
                }
            }
        }
    }

    private fun clickEvents() {
        binding.submitButton.setOnClickListener {
            val stringBuilder = StringBuilder()

            val ageText = binding.etUmur.text
            if(ageText.isNotEmpty() &&
                ageText.isNotBlank() &&
                ageText.isDigitsOnly()
                ) {
                stringBuilder.append("$ageText,")
            } else {
                showToast(this, "Pengisian umur tidak benar")
                return@setOnClickListener
            }

            if (binding.radioMale.isChecked) {
                stringBuilder.append("Laki-laki,")
            } else if (binding.radioFemale.isChecked) {
                stringBuilder.append("Perempuan,")
            } else {
                showToast(this, "Jenis kelamin tidak dipilih")
                return@setOnClickListener
            }

            val educationSelected = binding.dropdownPendidikan.selectedItemPosition
            when (educationSelected) {
                0 -> {
                    stringBuilder.append("TK,")
                }
                5 -> {
                    stringBuilder.append("S1,")
                }
                else -> {
                    stringBuilder.append("${binding.dropdownPendidikan.selectedItem},")
                }
            }

            val heightText = binding.etTinggiBadan.text
            var height = 0
            if (heightText.isNotEmpty() &&
                heightText.isNotBlank() &&
                heightText.isDigitsOnly()
                ) {
                height = Integer.parseInt(heightText.trimEnd().toString())
            } else {
                showToast(this, "Pengisian bmi tidak benar")
                return@setOnClickListener
            }

            val weightText = binding.etBeratBadan.text
            var weight = 0
            if (weightText.isNotEmpty() &&
                weightText.isNotBlank() &&
                weightText.isDigitsOnly()
            ) {
                weight = Integer.parseInt(weightText.trimEnd().toString()) / 100
            } else {
                showToast(this, "Pengisian bmi tidak benar")
                return@setOnClickListener
            }

            stringBuilder.append("${weight / height.toDouble().pow(2)},")

            val fitnessText = binding.etkesehatanFisik.text
            if (fitnessText.isNotEmpty() &&
                fitnessText.isNotBlank() &&
                fitnessText.isDigitsOnly()
            ) {
                stringBuilder.append("${fitnessText},")
            } else {
                showToast(this, "Pengisian bmi tidak benar")
                return@setOnClickListener
            }

            if (binding.aktivitasFisikRadioYa.isChecked) {
                stringBuilder.append("Ya,")
            } else if (binding.aktivitasFisikRadioTidak.isChecked) {
                stringBuilder.append("Tidak,")
            } else {
                showToast(this, "Aktivitas fisik tidak dipilih")
                return@setOnClickListener
            }

            if (binding.darahTinggiRadioAda.isChecked) {
                stringBuilder.append("Ada,")
            } else if (binding.darahTinggiRadioTidakAda.isChecked) {
                stringBuilder.append("Tidak ada,")
            } else {
                showToast(this, "Darah tinggi tidak dipilih")
                return@setOnClickListener
            }

            if (binding.kolesterolTinggiRadioAda.isChecked) {
                stringBuilder.append("Ada,")
            } else if (binding.kolesterolTinggiRadioTidakAda.isChecked) {
                stringBuilder.append("Tidak ada,")
            } else {
                showToast(this, "Tingkat kolesterol tidak dipilih")
                return@setOnClickListener
            }

            if (binding.strokeRadioPernah.isChecked) {
                stringBuilder.append("Pernah,")
            } else if (binding.strokeRadioTidak.isChecked) {
                stringBuilder.append("Tidak pernah,")
            } else {
                showToast(this, "Riwayat stroke tidak dipilih")
                return@setOnClickListener
            }

            if (binding.sulitJalanRadioYa.isChecked) {
                stringBuilder.append("Ya,")
            } else if (binding.sulitJalanRadioTidak.isChecked) {
                stringBuilder.append("Tidak,")
            } else {
                showToast(this, "Kondisi berjalan tidak dipilih")
                return@setOnClickListener
            }

            if (binding.penyakitJantungRadioPernah.isChecked) {
                stringBuilder.append("Pernah,")
            } else if (binding.penyakitJantungRadioTidak.isChecked) {
                stringBuilder.append("Tidak,")
            } else {
                showToast(this, "Riwayat penyakit jantung tidak dipilih")
                return@setOnClickListener
            }


            stringBuilder.append("${binding.dropdownkesehatanUmum.selectedItem}")

            predictViewModel.predict("diabetes", stringBuilder.toString()).observe(this) { result ->
                when (result) {
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this, result.error)
                    }
                    Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE

                        val prediction = result.data.data

                        // intent to result
                        if (isLoggedIn) {
                            databaseViewModel.storeDisease(DiseaseRequest(
                                    "diabetes",
                                    prediction.result,
                                    prediction.description,
                                    prediction.suggestion,
                                    prediction.confidenceScore,
                                    prediction.createdAt
                                )
                            )
                        }

                        val intent = Intent(this, DiabetesResultActivity::class.java)
                        intent.putExtra(PREDICTION_RESULT, prediction)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}