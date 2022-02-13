package com.example.avts_app

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.altbeacon.beacon.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ScanBeaconWorker(private val appContext: Context, workerParameters: WorkerParameters) : Worker(appContext, workerParameters), MonitorNotifier, RangeNotifier, Callback<UserInfo> {
    private val TAG1 = ":: Monitoring Activity ::"
    private val TAG2 = ":: Ranging Activity ::"
    private val baseURL = "http://localhost.com"
    private val sharedPref = appContext.getSharedPreferences(appContext.getString(R.string.user_info), Context.MODE_PRIVATE)
    private val beaconManager = BeaconManager.getInstanceForApplication(appContext)
    private val jsonPlaceHolderApi = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build().create(JsonPlaceHolderApi::class.java)
    private val userInfo = UserInfo()

    override fun doWork(): Result {
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))
        beaconManager.addMonitorNotifier(this)
        beaconManager.addRangeNotifier(this)
        return Result.success()
    }

    override fun didEnterRegion(region: Region?) {
        Log.d(TAG1, "::: More than 1 beacon is detected :::")
    }

    override fun didExitRegion(region: Region?) {
        Log.d(TAG1, "::: No beacon is detected :::")
    }

    override fun didDetermineStateForRegion(state: Int, region: Region?) {
        if (state == 0) {
            Log.d(TAG1, "::: Beacon can be detected, current state: 0 :::")
        } else {
            Log.d(TAG2, "::: Beacon cannot be detected, current state: $state :::")
        }
    }

    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
        runBlocking {
            if ((beacons != null) && beacons.isNotEmpty()) {
                userInfo.username = sharedPref.getString(appContext.getString(R.string.username), null)
                userInfo.phoneNumber = sharedPref.getString(appContext.getString(R.string.phone_number), null)
                userInfo.emailAddress = sharedPref.getString(appContext.getString(R.string.email_address), null)
                userInfo.UUID = beacons.iterator().next().id1.toString()
                userInfo.Major = beacons.iterator().next().id2.toString()
                userInfo.Minor = beacons.iterator().next().id3.toString()
                userInfo.timestamp = Calendar.getInstance().time.toString()

                createPost()

                launch {
                    delay(5000)
                    Log.d(":: Post ::", "send")
                }
            }
        }
    }

    override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
        if (!response.isSuccessful) {
            Log.e(":: Connection Error ::", "Error Code: " + response.code())
            return
        }
        val responseUserInfo = response.body()
        Log.d(":: Connection Success ::", "Info: $responseUserInfo")
    }

    override fun onFailure(call: Call<UserInfo>, t: Throwable) {
        Log.e(":: Connection Failure ::", t.message.toString())
    }

    private fun createPost() {
        val call = jsonPlaceHolderApi.createPost(userInfo)
        call.enqueue(this)
    }
}