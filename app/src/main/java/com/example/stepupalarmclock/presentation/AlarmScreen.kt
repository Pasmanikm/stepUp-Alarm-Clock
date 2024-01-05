package com.example.stepupalarmclock.presentation

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.layout.AppScaffold


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AlarmScreen() {
    println("Inside AlarmScreen")
    val context = LocalContext.current
    val vibratorManager =
        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator

    // Start vibrating when the screen is shown
    LaunchedEffect(key1 = true) {
        startVibrating(vibrator)
    }

    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp)
            .background(Color.Black),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = {
                println("Stop vibrating")
                stopVibrating(vibrator)
                (context as Activity).finish()
            }) {
                Text("Stop vibrating")
            }
        }
    }
}

fun startVibrating(vibrator: Vibrator) {
    val timings: LongArray = longArrayOf(50, 50, 100, 50, 50)
    val amplitudes: IntArray = intArrayOf(64, 128, 255, 128, 64)
    val repeat = 1 // Repeat from the second entry, index = 1.
    val repeatingEffect = VibrationEffect.createWaveform(timings, amplitudes, repeat)
    vibrator.vibrate(repeatingEffect)
}

fun stopVibrating(vibrator: Vibrator) {
    vibrator.cancel()
}