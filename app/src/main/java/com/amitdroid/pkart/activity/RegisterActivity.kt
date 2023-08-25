package com.amitdroid.pkart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.amitdroid.pkart.R
import com.amitdroid.pkart.databinding.ActivityRegisterBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener {
            openLogIn()
        }

        binding.button.setOnClickListener {
            validateUser()
        }
    }

    private fun validateUser() {
        if(binding.userName.text!!.isEmpty() || binding.userNumber.text!!.isEmpty())
            Toast.makeText(this,"Please fill all field",Toast.LENGTH_LONG).show()

        else

            storeData()
    }

    private fun storeData() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Loading.........")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()

        val data = hashMapOf<String,Any>()
        data["name"]=binding.userName.text.toString()
        data["number"]=binding.userNumber.text.toString()


        Firebase.firestore.collection("users").document(binding.userNumber.text.toString())

            .set(data).addOnSuccessListener {

                Toast.makeText(this,"User registered",Toast.LENGTH_LONG).show()
                builder.dismiss()

                openLogIn()

            }

            .addOnFailureListener {

                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
                builder.dismiss()

            }
    }

    private fun openLogIn() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

}