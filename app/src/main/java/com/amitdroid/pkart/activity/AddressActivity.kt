package com.amitdroid.pkart.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.amitdroid.pkart.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: ActivityAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = this.getSharedPreferences("users", MODE_PRIVATE)

        loadUserInfo()

        binding.proceed.setOnClickListener {
            validateData(
                // remove space in future
                binding.userNumber.text.toString(),

                binding.userName.text.toString(),

                binding.userState.text.toString(),

                binding.userCity.text.toString(),

                binding.userPinCode.text.toString()
            )
        }
    }

    private fun validateData(number: String, name: String, city: String, state: String, pinCode: String) {
        if (number.isEmpty() || state.isEmpty() || name.isEmpty() || city.isEmpty() || pinCode.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            storeData(pinCode, state, city)
        }
    }

    private fun storeData(pinCode: String, state: String, city: String) {
        val map = hashMapOf<String, Any>()
        map["city"] = city
        map["state"] = state
        map["pinCode"] = pinCode


        Firebase.firestore.collection("users")
            .document(preferences.getString("userNumber", "")!!)
            .update(map)
            .addOnSuccessListener {
                startActivity(Intent(this, CheckoutActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {
        Firebase.firestore.collection("users")
            .document(preferences.getString("userNumber", "false")!!)
            .get()
            .addOnSuccessListener { it ->
                val user = it.data
                if (user != null) {
                    binding.userName.setText(user["userName"].toString())
                    binding.userNumber.setText(user["userNumber"].toString())
                    binding.userCity.setText(user["city"].toString())
                    binding.userState.setText(user["state"].toString())
                    binding.userPinCode.setText(user["pinCode"].toString())
                }
                else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
    }
}
