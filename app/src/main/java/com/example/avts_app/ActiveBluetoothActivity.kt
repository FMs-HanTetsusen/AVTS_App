package com.example.avts_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest

class ActiveBluetoothActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_bluetooth)
        sharedPref = getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)

        val username: TextView = findViewById(R.id.textViewUsername)
        val phoneNumber: TextView = findViewById(R.id.textViewPhone)
        val emailAddress: TextView = findViewById(R.id.textViewEmailAddress)
        val updateButton: Button = findViewById(R.id.updateButton)

        username.text = sharedPref.getString(getString(R.string.username), null)
        phoneNumber.text = sharedPref.getString(getString(R.string.phone_number), null)
        emailAddress.text = sharedPref.getString(getString(R.string.email_address), null)
        updateButton.setOnClickListener {
            WorkManager.getInstance(this).cancelAllWork()
            val inputForm = Intent(this, InputFormActivity::class.java)
            startActivity(inputForm)
            finish()
        }

        val scanBeaconRequest: WorkRequest = OneTimeWorkRequestBuilder<ScanBeaconWorker>().build()
        WorkManager.getInstance(this).enqueue(scanBeaconRequest)
    }
}