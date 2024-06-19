package com.example.casion.views.form.jantung

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.casion.data.remote.request.DiseaseRequest
import com.example.casion.data.result.Result
import com.example.casion.databinding.ActivityJantungBinding
import com.example.casion.util.Time
import com.example.casion.util.UserPreferences
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.datastore
import com.example.casion.util.showToast
import com.example.casion.viewmodel.DatabaseViewModel
import com.example.casion.viewmodel.PredictViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class JantungActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJantungBinding
    private val databaseViewModel by viewModels<DatabaseViewModel> { ViewModelFactory.getDatabaseInstance(this) }
    private val predictViewModel by viewModels<PredictViewModel> { ViewModelFactory.getPredictInstance(this) }

    //session manager
    private lateinit var prefs: UserPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJantungBinding.inflate(layoutInflater)
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
                stringBuilder.append("age,$ageText,")
            } else {
                showToast(this, "Pengisian umur tidak benar")
                return@setOnClickListener
            }

            if (binding.radioMale.isChecked) {
                stringBuilder.append("sex,0,")
            } else if (binding.radioFemale.isChecked) {
                stringBuilder.append("sex,1,")
            } else {
                showToast(this, "Jenis kelamin tidak dipilih")
                return@setOnClickListener
            }

            stringBuilder.append("cp,${binding.dropdownNyeriDada.selectedItemPosition},")

            val bloodPressureText = binding.etTekananDarahIstirahat.text
            if (bloodPressureText.isNotEmpty() &&
                bloodPressureText.isNotBlank() &&
                bloodPressureText.isDigitsOnly()
            ) {
                stringBuilder.append("trestbps,${bloodPressureText},")
            } else {
                showToast(this, "Pengisian bmi tidak benar")
                return@setOnClickListener
            }

            val cholesterolText = binding.etKolesterol.text
            if (cholesterolText.isNotEmpty() &&
                cholesterolText.isNotBlank() &&
                cholesterolText.isDigitsOnly()
            ) {
                stringBuilder.append("chol,${cholesterolText},")
            } else {
                showToast(this, "Pengisian bmi tidak benar")
                return@setOnClickListener
            }

            if (binding.gulaDarahRadioYa.isChecked) {
                stringBuilder.append("fbs,0,")
            } else if (binding.gulaDarahRadioYa.isChecked) {
                stringBuilder.append("fbs,1")
            } else {
                showToast(this, "Gula darah tidak dipilih")
                return@setOnClickListener
            }

            val thalachText = binding.etKolesterol.text
            if (thalachText.isNotEmpty() &&
                thalachText.isNotBlank() &&
                thalachText.isDigitsOnly()
            ) {
                stringBuilder.append("thalach,${thalachText},")
            } else {
                showToast(this, "Pengisian detak jantung tidak benar")
                return@setOnClickListener
            }

            if (binding.aktivitasFisikRadioYa.isChecked) {
                stringBuilder.append("exang,1,")
            } else if (binding.aktivitasFisikRadioTidak.isChecked) {
                stringBuilder.append("exang,0")
            } else {
                showToast(this, "Nyeri dada tidak dipilih")
                return@setOnClickListener
            }

            predictViewModel.predict("heart", stringBuilder.toString()).observe(this) { result ->
                when (result) {
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this, result.error)
                    }
                    Result.Loading -> { binding.progressBar.visibility = View.VISIBLE }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE

                        val prediction = result.data.data

                        // intent to result
                        if (isLoggedIn) {
                                databaseViewModel.storeDisease(
                                    DiseaseRequest(
                                    "jantung",
                                    prediction.result,
                                    prediction.description,
                                    prediction.suggestion,
                                    prediction.confidenceScore,
                                    prediction.createdAt
                                )
                            )
                        }

                        val intent = Intent(this, JantungResultActivity::class.java)
                        intent.putExtra(JantungResultActivity.PREDICTION_RESULT, prediction)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}