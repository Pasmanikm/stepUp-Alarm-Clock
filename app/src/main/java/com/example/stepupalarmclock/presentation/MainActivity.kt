package com.example.stepupalarmclock.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.google.android.horologist.composables.TimePicker
import com.google.android.horologist.compose.layout.AppScaffold
import java.time.LocalTime
import java.util.Calendar


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        println("Start on create")
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
        if (!getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MainContent()
    }
}