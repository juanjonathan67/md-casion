package com.example.casion.views.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.casion.R
import com.example.casion.data.result.Result
import com.example.casion.databinding.ActivitySignUpBinding
import com.example.casion.util.Time.localDateFromTimestamp
import com.example.casion.util.UserPreferences
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.datastore
import com.example.casion.util.isValidEmail
import com.example.casion.util.isValidPassword
import com.example.casion.util.showToast
import com.example.casion.viewmodel.AuthViewModel
import com.example.casion.views.main.MainActivity
import com.example.casion.views.signin.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.time.LocalDate

class SignUpActivity : AppCompatActivity() {
    private val authViewModel by viewModels<AuthViewModel> { ViewModelFactory.getAuthInstance(this) }
    private lateinit var binding: ActivitySignUpBinding
    private var isRadioChecked = false

    // google sign in
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var prefs: UserPreferences

    private var birthday = LocalDate.now().toString()

    private var datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Tanggal Lahir")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e : Exception) {
                showToast(this, "Google sign in failed: ${e.message}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAuth()
        clickEvents()

        val privacyPolicyText = getString(R.string.privacy_policy_button)
        val privacyPolicySpannable = SpannableString(privacyPolicyText)

        val signInText = getString(R.string.tv_go_signin)
        val signInSpannable = SpannableString(signInText)

        privacyPolicySpannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary)),
            privacyPolicyText.indexOf("Kebijakan Privasi"),
            privacyPolicyText.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        signInSpannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {}
            },
            0,
            signInText.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.radioButton.text = privacyPolicySpannable
        binding.tvsignin.text = signInSpannable
        binding.tvsignin.movementMethod = LinkMovementMethod.getInstance()

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                finish()
            }
        }

        binding.tvsignin.text = SpannableString(signInText).apply {
            setSpan(clickableSpan, indexOf("Masuk"), length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    getUidFromGoogleAuth()
                } else {
                    showToast(this, "Authentication failed")
                }
            }
    }

    private fun getUidFromGoogleAuth() {
        val user = firebaseAuth.currentUser
        user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
            if (tokenTask.isSuccessful) {
                runBlocking {
                    prefs.saveUserToken(tokenTask.result.token.toString())
                }
                showToast(this, "Signed in as ${user.displayName}")
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                showToast(this, "Get Token Error")
            }
        }
    }

    private fun initAuth() {
        prefs = UserPreferences.getInstance(this.datastore)

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        firebaseAuth = Firebase.auth
    }

    private fun clickEvents() {
        binding.GoogleSignUpbutton.setOnClickListener {
            if (binding.radioButton.isChecked) {
                val signInIntent = googleSignInClient.signInIntent
                resultLauncher.launch(signInIntent)
            } else {
                showToast(this, "Mohon untuk membaca Kebijakan Privasi terlebih dahulu!")
            }
        }

        datePicker.addOnPositiveButtonClickListener {
            birthday = localDateFromTimestamp(it).toString()
            binding.birthday.text = birthday
        }

        binding.birthday.setOnClickListener {
            datePicker.show(supportFragmentManager, "Tanggal Lahir")
        }

        binding.SignUpbutton.setOnClickListener {
            if (binding.radioButton.isChecked) {
                authViewModel.register(
                    binding.fullname.text.toString(),
                    binding.email.text.toString(),
                    birthday,
                    binding.male.isChecked,
                    binding.pass.text.toString(),
                ).observe(this) { result ->
                    when (result) {
                        is Result.Error -> {
                            showToast(this, result.error)
                            binding.progressBar.visibility = View.GONE
                        }
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            showToast(this, result.data.message)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } else {
                showToast(this, "Mohon untuk membaca Kebijakan Privasi terlebih dahulu!")
            }
        }

        binding.radioButton.setOnClickListener {
            if (isRadioChecked) {
                binding.radioButton.isChecked = false
                isRadioChecked = false
            } else {
                isRadioChecked = true
            }
        }
    }

    private fun inputValidator() {
        binding.email.doOnTextChanged { text, _, _, _ ->
            if (!text.isValidEmail()) {
                binding.email.error = "Invalid email format"
            } else {
                binding.email.error = null
            }
        }

        binding.pass.doOnTextChanged { text, _, _, _ ->
            if (!text.isValidPassword()) {
                binding.pass.error = "Password must contain a digit, lowercase letter, uppercase letter, one of !@#\$%^&+=;', no spaces, and at least 8 characters long"
            } else {
                binding.pass.error = null
            }
        }
    }
}

