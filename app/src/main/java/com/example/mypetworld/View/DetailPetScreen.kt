package com.example.mypetworld.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mypetworld.ViewModel.PetViewModel
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.mypetworld.R


@Composable
fun PetDetailScreen(
    navController: NavController,
    petId: String?,
    petViewModel: PetViewModel = viewModel()
) {
    val petsState by petViewModel.pets.collectAsState()
    val pet = petsState.find { it.id == petId }
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    val currentLightLevel = remember { mutableFloatStateOf(0f) }

    if (pet == null) {
        Text("Pet not found.")
        return
    }

    val sensorListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                currentLightLevel.floatValue = event.values[0]
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    LaunchedEffect(pet.id) {
        pet.let {
            petViewModel.startUpdatingPetStats(
                petId = it.id,
                lightLevelProvider = { currentLightLevel.floatValue }
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = { navController.navigate("edit_pet_screen/${petId}") }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit pet",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = pet.name,
                fontSize = 32.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = petViewModel.getPetIconRes(pet.type, pet.variant)),
                contentDescription = "${pet.name}'s picture",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            StatRow("HP", pet.hp)
            StatRow("Hunger", pet.hunger)
            StatRow("Happiness", pet.happiness)
            StatRow("Energy", pet.energy)

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.food_bowl),
                contentDescription = "Food Bowl",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        pet.hunger = (pet.hunger + 20).coerceAtMost(100)
                        petViewModel.updatePet(pet)
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatRow(label: String, value: Int) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "$label: $value", fontSize = 16.sp, color = Color.Black)
        LinearProgressIndicator(
            progress = value / 100f,
            color = Color(0xFF4CC9F0),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}




