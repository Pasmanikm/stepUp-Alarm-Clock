package com.example.stepupalarmclock.presentation

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.stepupalarmclock.presentation.alarm.AlarmScreen
import kotlinx.coroutines.time.delay

class AlarmActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    public override fun onCreate(savedInstanceState: Bundle?) {
        println("Start on create of AlarmActivity")

        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            AlarmScreen(textToShow = "Make your first steps") {
                println("In onStepDetected, finish activity")
                finish()
            }
        }
    }
}