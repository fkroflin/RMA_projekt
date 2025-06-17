package com.example.mypetworld.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.mypetworld.Model.Pet
import com.example.mypetworld.Model.PetType
import com.example.mypetworld.R
import com.example.mypetworld.ViewModel.PetViewModel
import com.example.mypetworld.ui.theme.ChewyFont
import com.example.mypetworld.ui.theme.ComicNeueFont
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AdoptPetScreen(
    navController: NavController,
    petViewModel: PetViewModel = viewModel()
) {
    var petName by remember { mutableStateOf("") }
    var selectedPetType by remember { mutableStateOf(PetType.dog) }
    var expanded by remember { mutableStateOf(false) }
    val petTypes = PetType.values()
    var petVariant by remember { mutableStateOf(1) }
    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()

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
                .padding(10.dp),
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
                        text = "ADOPT A PET",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = ChewyFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = {
                    petVariant = if (petVariant == 1) selectedPetType.maxVariant else petVariant - 1
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous variant")
                }

                Image(
                    painter = painterResource(id = petViewModel.getPetIconRes(selectedPetType.displayName, petVariant)),
                    contentDescription = "${selectedPetType.displayName} icon",
                    modifier = Modifier
                        .size(170.dp)
                        .padding(16.dp)
                )

                IconButton(onClick = {
                    petVariant = if (petVariant == selectedPetType.maxVariant) 1 else petVariant + 1
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

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Pet Type",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ComicNeueFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box {
                        Text(
                            text = selectedPetType.displayName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFBDE0FE), shape = RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontFamily = ComicNeueFont
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            petTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        selectedPetType = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_more),
                                contentDescription = "Select Pet Type"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "About ${selectedPetType.displayName}s",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ComicNeueFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = selectedPetType.description,
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontFamily = ComicNeueFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Default Stats",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ComicNeueFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "HP: 100, Hunger: 100, Happiness: 100, Energy: 100",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontFamily = ComicNeueFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (petName.isNotBlank()) {
                        val newPet = Pet(
                            name = petName,
                            type = selectedPetType.displayName,
                            hp = 100,
                            hunger = 100,
                            happiness = 100,
                            energy = 100,
                            userId = currentUserId,
                            variant = petVariant
                        )

                        petViewModel.addPet(newPet) {
                            Toast.makeText(context, "You've adopted $petName!", Toast.LENGTH_SHORT).show()
                            petName = ""
                            selectedPetType = PetType.dog
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(context, "Please enter a name for your pet.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA725),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "ADOPT NOW",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = ChewyFont
                )
            }
        }
    }
}

