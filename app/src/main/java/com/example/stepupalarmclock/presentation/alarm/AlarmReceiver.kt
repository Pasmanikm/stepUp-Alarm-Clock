package com.example.stepupalarmclock.presentation.alarm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.stepupalarmclock.presentation.AlarmActivity

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("WearRecents")
    override fun onReceive(context: Context, intent: Intent) {
        println("Alarm received")

        val alarmIntent = Intent(context, AlarmActivity::class.java)

        alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(alarmIntent)
    }
}