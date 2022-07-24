package com.example.seekexinternet.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.seekexinternet.MainActivity
import com.example.seekexinternet.R
import com.example.seekexinternet.constants.Constants.ACTION_PENDING_INTENT
import com.example.seekexinternet.constants.Constants.GET_INTERNET_SPEED_IN_EVERY_10
import com.example.seekexinternet.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.example.seekexinternet.constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.seekexinternet.constants.Constants.NOTIFICATION_ID
import com.example.seekexinternet.db.RoomDao
import com.example.seekexinternet.models.InternetSpeedModel
import com.example.seekexinternet.repository.SeekexRepository
import com.example.seekexinternet.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InternetService : LifecycleService() {

    companion object {
        val currInternetVal = MutableLiveData<String>()
    }

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var speedTestSocket: SpeedTestSocket

    @Inject
    lateinit var roomDao: RoomDao
    private lateinit var seekexRepository: SeekexRepository

    override fun onCreate() {
        super.onCreate()
        currInternetVal.postValue("0 mb/s")
        seekexRepository = SeekexRepository(roomDao)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            startForegroundService()
            speedTestSocket = SpeedTestSocket()
            speedTestSocket.addSpeedTestListener(iSpeedTestListener)
            updateValInEvery10Sec()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_network_check_24)
            .setContentTitle("Internet Speed")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        currInternetVal.observe(this) {
            notificationBuilder.setContentText(it.toString())
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_PENDING_INTENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateValInEvery10Sec() {
        getCurrentSpeedIn10Sec()
        handler = Handler(Looper.myLooper()!!)
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable, GET_INTERNET_SPEED_IN_EVERY_10)
            getCurrentSpeedIn10Sec()
        }.also {
            runnable = it
        }, GET_INTERNET_SPEED_IN_EVERY_10)
    }

    private fun getCurrentSpeedIn10Sec() {
        Thread { speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso") }
            .start()
    }

    private val iSpeedTestListener = object : ISpeedTestListener {
        override fun onCompletion(report: SpeedTestReport) {
            val speed = Utils.getFileSize(report.transferRateBit.toLong())
            updateNotificationAndVal(speed)
        }

        override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
            updateNotificationAndVal("0 mb/s")
        }

        override fun onProgress(percent: Float, report: SpeedTestReport) {
            Log.d("LAK", "onProgress")
        }
    }

    private fun updateNotificationAndVal(speed: String) {
        currInternetVal.postValue(speed)
        val internetSpeedModel = InternetSpeedModel(Utils.getCurrentTime(), speed)
        lifecycleScope.launch {
            seekexRepository.insertSpeed(internetSpeedModel)
        }
    }
}