package com.example.stepupalarmclock.presentation

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.stepupalarmclock.presentation.alarm.AlarmReceiver
import com.google.android.horologist.composables.TimePicker
import com.google.android.horologist.compose.layout.AppScaffold
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 1
    }

    // State to track whether the permission is granted
    private var isPermissionGranted = false

    override fun onStart() {
        println("In onStart")
        super.onStart()
        checkAndRequestActivityRecognitionPermission()
    }

    private fun checkAndRequestActivityRecognitionPermission() {
        println("In checkAndRequestActivityRecognitionPermission")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            println("Permission is not granted")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                REQUEST_ACTIVITY_RECOGNITION_PERMISSION
            )
        } else {
            println("Permission is already granted")
            isPermissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        println("In onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ACTIVITY_RECOGNITION_PERMISSION -> {
                isPermissionGranted =
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (!isPermissionGranted) {
                    println("Permission is required for this app to function")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun MainContent() {
        var time by remember { mutableStateOf(LocalTime.now()) }


        AppScaffold(
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp)
                .background(Color.Black),
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                TimePicker(
                    time = time,
                    showSeconds = false,
                    onTimeConfirm = { newTime ->
                        // This is where you handle the confirmed time
                        time = newTime
                        setAlarm(time)
                    })
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun setAlarm(time: LocalTime) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        } else {
            val intent = Intent(this, AlarmReceiver::class.java)
            val uniqueId = generateUniqueIdForAlarm()

            val pendingIntent = PendingIntent.getBroadcast(
                this,
                uniqueId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, time.hour)
                set(Calendar.MINUTE, time.minute)
                set(Calendar.SECOND, 0)
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            val format = getDateInstance(SimpleDateFormat.SHORT)
            println("Good bye, see you at ${format.format(calendar.time)}")
        }
    }

    private fun generateUniqueIdForAlarm(): Int {
        return Random.nextInt()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MainContent()
    }
}