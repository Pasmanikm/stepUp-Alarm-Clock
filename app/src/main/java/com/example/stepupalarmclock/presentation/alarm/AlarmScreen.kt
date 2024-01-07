package com.example.stepupalarmclock.presentation.alarm

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material.Text


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AlarmScreen(
    textToShow: String, onStepDetected: () -> Unit
) {
    val context = LocalContext.current
    val vibratorManager =
        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    LaunchedEffect(key1 = Unit) {
        startVibrating(vibrator)
    }

    DisposableEffect(key1 = Unit) {
        val stepListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                println("In onSensorChanged")
                if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                    println("Detected a step")
                    stopVibrating(vibrator)
                    sensorManager.unregisterListener(this)
                    onStepDetected()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                println("onAccuracyChanged but IDGAF")
            }
        }

        stepSensor?.let {
            println("Registering step listener")
            sensorManager.registerListener(stepListener, it, SensorManager.SENSOR_DELAY_FASTEST)
        }

        onDispose {
            println("Disposing of step listener")
            stepSensor?.let {
                sensorManager.unregisterListener(stepListener)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = textToShow,
            color = Color.White
        )
    }
}

fun stopVibrating(vibrator: Vibrator) {
    vibrator.cancel()
}

fun startVibrating(vibrator: Vibrator) {
    val timings: LongArray = longArrayOf(50, 50, 100, 50, 50)
    val amplitudes: IntArray = intArrayOf(64, 128, 255, 128, 64)
    val repeat = 1 // Repeat from the second entry, index = 1.
    val repeatingEffect = VibrationEffect.createWaveform(timings, amplitudes, repeat)
    vibrator.vibrate(repeatingEffect)
}

