package com.example.casion.views.signin

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.casion.R
import com.example.casion.databinding.ActivitySignInBinding
import com.example.casion.views.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}
