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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.mypetworld.R
import com.example.mypetworld.ui.theme.ChewyFont
import com.example.mypetworld.ui.theme.ComicNeueFont


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
            petViewModel.startPetStatSimulation(
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
            Spacer(modifier = Modifier.height(24.dp))

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
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = ChewyFont
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

            StatRow("❤️ (HP)", pet.hp)
            StatRow("🍔 (Hunger)", pet.hunger)
            StatRow("😄 (Happiness)", pet.happiness)
            StatRow("⚡ (Energy)", pet.energy)


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
fun StatRow(stat: String, value: Int) {
    val clampedValue = value.coerceIn(0, 100)
    val progress = clampedValue / 100f

    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stat,
                fontSize = 22.sp,
                fontFamily = ComicNeueFont

            )
            Text(
                text = "$clampedValue/100",
                fontSize = 16.sp,
                color = Color.DarkGray,
                fontFamily = ComicNeueFont

            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF4CC9F0), Color(0xFF3A86FF))
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}





