package com.example.avts_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class InputFormActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_form)
        sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)

        val username: EditText = findViewById(R.id.editTextPersonName)
        val phoneNumber: EditText = findViewById(R.id.editTextPhone)
        val emailAddress: EditText = findViewById(R.id.editTextEmailAddress)
        val submitButton: Button = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            if (username.text.trim().isEmpty() || phoneNumber.text.trim().isEmpty() || emailAddress.text.trim().isEmpty()) {
                Toast.makeText(this, "Please complete your input form.", Toast.LENGTH_SHORT).show()
            } else {
                sharedPref.apply {
                    edit {
                        putString(getString(R.string.username), username.text.toString())
                        putString(getString(R.string.phone_number), phoneNumber.text.toString())
                        putString(getString(R.string.email_address), emailAddress.text.toString())
                    }
                }
                val activeBluetooth = Intent(this, ActiveBluetoothActivity::class.java)
                startActivity(activeBluetooth)
                finish()
            }
        }
    }
}