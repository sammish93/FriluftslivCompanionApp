package no.hiof.friluftslivcompanionapp.ui.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun Sensors() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var sensorManager: SensorManager =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        var deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        Text(
            text = "Sensors in Devices are : ",

        )

        var sensorsData = ""

        for (sens in deviceSensors) {
            sensorsData = sensorsData + sens.name + "  \n\n"
        }

        Text(
            text = sensorsData,
        )

    }
}