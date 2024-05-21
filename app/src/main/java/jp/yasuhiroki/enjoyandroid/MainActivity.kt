package jp.yasuhiroki.enjoyandroid

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import jp.yasuhiroki.enjoyandroid.ui.theme.EnjoyAndroidTheme

class MainActivity : ComponentActivity() {
    private var sensorManager: SensorManager? = null
    private var stepCounterSensor: Sensor? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission was granted
            } else {
                // Permission was denied
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        // センサー取得
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        setContent {
            EnjoyAndroidTheme {
                var stepCount by remember { mutableStateOf(0) }

                // センサーリスナー登録
                stepCounterSensor?.let {
                    sensorManager?.registerListener(
                        object : SensorEventListener {
                            override fun onSensorChanged(event: SensorEvent?) {
                                if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
                                    stepCount = event.values[0].toInt()
                                    Log.d("StepCounterJobService", "Step count: $stepCount")
                                    // 画面に歩数を表示する処理
                                }
                            }

                            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                                // Not used
                            }
                        },
                        it,
                        SensorManager.SENSOR_DELAY_FASTEST
                    )
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text("Current steps $stepCount")
                }
            }
        }
    }
}