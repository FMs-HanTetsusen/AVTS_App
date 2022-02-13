package com.example.avts_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        val checkBox: CheckBox = findViewById(R.id.checkBox)
        val confirmButton: Button = findViewById(R.id.confirmButton)

        confirmButton.setOnClickListener {
            if (checkBox.isChecked) {
                val inputForm = Intent(this, InputFormActivity::class.java)
                startActivity(inputForm)
                finish()
            } else {
                Toast.makeText(this, "Please agree to proceed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}