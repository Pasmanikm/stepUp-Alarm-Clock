package com.example.stepupalarmclock.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.mutableStateListOf
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class AlarmInfo(val id: Int, val time: String, var isEnabled: Boolean)

class AlarmManagerHelper(private val context: Context) {
    private val alarms = mutableStateListOf<AlarmInfo>()

    fun setAlarm(id: Int, timeInMillis: Long, pendingIntent: PendingIntent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            // Intent to take the user to the system settings
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                .withZone(ZoneId.systemDefault())
            val formattedTime = formatter.format(Instant.ofEpochMilli(timeInMillis))

            alarms.add(AlarmInfo(id, formattedTime, true))
        }
    }

    fun cancelAlarm(id: Int, pendingIntent: PendingIntent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        alarms.removeAll { it.id == id }
    }

    fun getAlarms(): List<AlarmInfo> {
        return alarms
    }
}