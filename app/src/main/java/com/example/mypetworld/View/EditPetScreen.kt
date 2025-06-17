package com.example.mypetworld.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mypetworld.Model.PetType
import com.example.mypetworld.R
import com.example.mypetworld.ViewModel.PetViewModel
import com.example.mypetworld.ui.theme.ChewyFont
import com.example.mypetworld.ui.theme.ComicNeueFont

@Composable
fun EditPetScreen(
    navController: NavController,
    petId: String,
    petViewModel: PetViewModel = viewModel()
) {
    val pets by petViewModel.pets.collectAsState()
    val petToEdit = pets.find { it.id == petId }

    if (petToEdit == null) {
        Text("Pet not found!")
        return
    }

    var petName by remember { mutableStateOf(petToEdit.name) }
    var petVariant by remember { mutableStateOf(petToEdit.variant) }
    val context = LocalContext.current
    val petType = PetType.values().find { it.displayName == petToEdit.type } ?: PetType.dog

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
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF4CC9F0), shape = RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "EDIT PET",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = ChewyFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    petVariant = if (petVariant == 1) petType.maxVariant else petVariant - 1
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous variant")
                }

                Image(
                    painter = painterResource(id = petViewModel.getPetIconRes(petType.displayName, petVariant)),
                    contentDescription = "${petType.displayName} icon",
                    modifier = Modifier
                        .size(180.dp)
                        .padding(16.dp)
                )

                IconButton(onClick = {
                    petVariant = if (petVariant == petType.maxVariant) 1 else petVariant + 1
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next variant")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = petName,
                onValueChange = { petName = it },
                label = { Text("Pet Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (petName.isNotBlank()) {
                        val updatedPet = petToEdit.copy(
                            name = petName,
                            variant = petVariant
                        )

                        petViewModel.updatePet(updatedPet)
                        Toast.makeText(context, "${petName}'s details updated!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Pet name can't be empty!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA725),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "SAVE CHANGES",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = ChewyFont
                )
            }
        }
    }
}
