package com.amitdroid.pkart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.amitdroid.pkart.MainActivity
import com.amitdroid.pkart.databinding.ActivityOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val userOtp = binding.userOtp.text.toString().trim()

            if (userOtp.isEmpty()) {
                Toast.makeText(this, "Please provide the correct OTP", Toast.LENGTH_SHORT).show()
            } else {
                verifyUser(userOtp)
            }
        }
    }

    private fun verifyUser(otp: String) {
        val verificationId = intent.getStringExtra("VerificationId")

        if (verificationId != null) {
            val preferences = this.getSharedPreferences("users", MODE_PRIVATE)
            val editor=preferences.edit()
            editor.putString("number", intent.getStringExtra("number")!! )

            editor.apply()
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            signInWithPhoneAuthCredential(credential)
        } else {
            Toast.makeText(this, "Verification ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
