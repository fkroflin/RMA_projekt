package com.example.mypetworld.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mypetworld.R
import com.example.mypetworld.ViewModel.AuthenticationViewModel
import com.example.mypetworld.ui.theme.ChewyFont
import com.example.mypetworld.ui.theme.ComicNeueFont

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthenticationViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .offset(y = -45.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .height(280.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(
                    text = "Email",
                    fontFamily = ComicNeueFont
                ) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(
                    text = "Password",
                    fontFamily = ComicNeueFont
                ) },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        email.isBlank() && password.isBlank() -> {
                            errorMessage = "Please enter both email and password"
                        }
                        email.isBlank() -> {
                            errorMessage = "Please enter an email"
                        }
                        password.isBlank() -> {
                            errorMessage = "Please enter a password"
                        }
                        else -> {
                            authViewModel.login(email, password) { success, error ->
                                if (success) {
                                    navController.navigate("main")
                                } else {
                                    errorMessage = error
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CC9F0))
            ) {
                Text(
                    text = "Login",
                    fontFamily = ChewyFont
                )
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "No account? Register",
                color = Color(0xFF4CC9F0),
                modifier = Modifier.clickable { navController.navigate("register") }
            )
        }
    }
}