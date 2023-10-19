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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun Sensors(userViewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current
    val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
    val userState by userViewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(5.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = "User logged in: ${userState.currentUser}",
        )
        Text(
            text = "Is the logged in user anonymous? ${userState.currentUser?.isAnonymous}",
        )
        Text(
            text = "User email: ${userState.currentUser?.email}",
        )
        Text(
            text = "User display name: ${userState.currentUser?.displayName}",
        )
        Text(
            text = "User position: Lon: ${userState.lastKnownLocation?.longitude}, Lat: ${userState.lastKnownLocation?.latitude}",
        )

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