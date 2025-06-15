package com.example.mypetworld

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mypetworld.View.AdoptPetScreen
import com.example.mypetworld.View.EditPetScreen
import com.example.mypetworld.View.LoginScreen
import com.example.mypetworld.View.MainScreen
import com.example.mypetworld.View.PetDetailScreen
import com.example.mypetworld.View.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String) {
    object Main : Screen("main")
}

@Composable
fun Navigation(navController: NavHostController) {

    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

    NavHost(navController = navController, startDestination = if (isLoggedIn) "main" else "login") {
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }

        composable("adopt_pet_screen") {
            AdoptPetScreen(
                navController = navController,
                onAdoptClick = { pet ->
                    navController.popBackStack()
                }
            )
        }

        composable(
            "pet_detail_screen/{petId}"
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")
            PetDetailScreen(navController = navController, petId = petId)
        }

        composable(
            "login"
        ){
            LoginScreen(navController = navController)

        }

        composable(
            "register"
        ){
            RegisterScreen(navController = navController)
        }

        composable("edit_pet_screen/{petId}") { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId") ?: return@composable
            EditPetScreen(navController = navController, petId = petId)
        }
    }
}