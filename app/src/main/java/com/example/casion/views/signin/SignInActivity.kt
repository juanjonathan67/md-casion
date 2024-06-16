package com.example.casion.views.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.casion.R
import com.example.casion.data.result.Result
import com.example.casion.databinding.ActivitySignInBinding
import com.example.casion.util.UserPreferences
import com.example.casion.util.ViewModelFactory
import com.example.casion.util.datastore
import com.example.casion.util.showToast
import com.example.casion.viewmodel.AuthViewModel
import com.example.casion.views.main.MainActivity
import com.example.casion.views.signup.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class SignInActivity : AppCompatActivity() {
    private val authViewModel by viewModels<AuthViewModel> { ViewModelFactory.getAuthInstance(this) }
    private lateinit var binding: ActivitySignInBinding

    // google sign in
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var prefs: UserPreferences

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
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAuth()
        clickEvents()

        val signUpText = getString(R.string.tv_go_signup)
        val signUpSpannable = SpannableString(signUpText)

        signUpSpannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
                }
            },
            0,
            signUpText.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvsignup.text = signUpSpannable
        binding.tvsignup.movementMethod = LinkMovementMethod.getInstance()

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
                finish()
            }
        }

        binding.tvsignup.text = SpannableString(signUpText).apply {
            setSpan(clickableSpan, indexOf("Daftar"), length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
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
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }

        binding.SignUpbutton.setOnClickListener {
            authViewModel.login(
                binding.email.text.toString(),
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
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
