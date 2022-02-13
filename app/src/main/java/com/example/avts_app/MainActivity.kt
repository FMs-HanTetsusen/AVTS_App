package com.example.avts_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)

        val enterButton: Button = findViewById(R.id.enterButton)

        enterButton.setOnClickListener {
            val privacyPolicy = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(privacyPolicy)
            finish()
        }

        if (sharedPref.getString(getString(R.string.username), null) != null) {
            val activeBluetooth = Intent(this, ActiveBluetoothActivity::class.java)
            startActivity(activeBluetooth)
            finish()
        }
    }
}