package com.example.casion.views.signup

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.casion.R
import com.example.casion.databinding.ActivitySignUpBinding
import com.example.casion.views.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var isRadioChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.radioButton.setOnClickListener {
            if (isRadioChecked) {
                binding.radioButton.isChecked = false
                isRadioChecked = false
            } else {
                isRadioChecked = true
            }
        }
    }
}

