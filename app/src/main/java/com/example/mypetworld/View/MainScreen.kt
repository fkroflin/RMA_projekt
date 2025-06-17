package com.example.mypetworld.View

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypetworld.Model.Pet
import com.example.mypetworld.ViewModel.PetViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import com.example.mypetworld.R
import com.example.mypetworld.ViewModel.AuthenticationViewModel
import com.example.mypetworld.ui.theme.ChewyFont
import com.example.mypetworld.ui.theme.ComicNeueFont
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


@Composable
fun MainScreen(petViewModel: PetViewModel = viewModel(), navController: NavController) {
    val pets = petViewModel.pets.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }
    val authViewModel: AuthenticationViewModel = viewModel()

    val petTypes = pets.map { it.type }.distinct()
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<String?>(null) }

    val filteredPets = pets.filter {
        (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true)) &&
                (selectedType == null || it.type.equals(selectedType, ignoreCase = true))
    }
    val userDisplayName = authViewModel.getDisplayName()

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
                .padding(16.dp)
                .height(750.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("login") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                )
                Text(
                    text = userDisplayName,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = ComicNeueFont
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CC9F0), shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MY PET WORLD",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = ChewyFont
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {searchQuery = it },
                    placeholder = { Text(text = "Search by name...") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier.wrapContentSize(Alignment.TopStart)
                ){
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { isDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = {isDropdownExpanded = false},
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(vertical = 4.dp)
                    ){
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("All Types")
                                    if (selectedType == null) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color(0xFF4CC9F0),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                selectedType = null
                                isDropdownExpanded = false
                            }
                        )
                        petTypes.forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(type)
                                        if (selectedType == type) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = Color(0xFF4CC9F0),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    selectedType = type
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFBDE0FE), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {

                LazyColumn {
                    items(filteredPets){pet ->
                        PetItem(pet = pet, navController, petViewModel)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("adopt_pet_screen") },
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
                    text = "ADOPT",
                    fontWeight = FontWeight.Bold,
                    fontFamily = ChewyFont
                )
            }

        }
    }
}


@Composable
fun PetItem(pet: Pet, navController: NavController, viewModel: PetViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("pet_detail_screen/${pet.id}") }
    ) {
        Image(
            painter = painterResource(id = viewModel.getPetIconRes(pet.type,pet.variant)),
            contentDescription = "${pet.name} icon",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(8.dp)
//                .clickable { navController.navigate("pet_detail_screen/${pet.id}") }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(1f)
        ) {
            Column {
                Text(
                    text = "${pet.name} (${pet.type})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = ComicNeueFont
                )
                Text(
                    text = "HP: ${pet.hp}, Hunger: ${pet.hunger}, Happiness: ${pet.happiness}, Energy: ${pet.energy}",
                    fontSize = 14.sp,
                    fontFamily = ComicNeueFont
                )
            }
        }
    }
}



