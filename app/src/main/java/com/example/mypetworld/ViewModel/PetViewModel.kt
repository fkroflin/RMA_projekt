package com.example.mypetworld.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mypetworld.Model.Pet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.viewModelScope
import com.example.mypetworld.R
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth


class PetViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets
    private val auth = FirebaseAuth.getInstance()


    init {
        try {
            fetchPets()
        } catch (e: Exception) {
            Log.e("MyViewModel", "Initialization failed", e)
        }
    }
    private fun fetchPets() {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null) {
            Log.w("PetViewModel", "User not logged in — no pets to fetch")
            _pets.value = emptyList()
            return
        }
        db.collection("Pets")
            .whereEqualTo("userId", currentUserId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("PetViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val petList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Pet::class.java)?.apply {
                            id = doc.id
                        }
                    }
                    _pets.value = petList
                } else {
                    Log.d("PetViewModel", "Current data: null")
                }
            }
    }

    fun addPet(pet: Pet, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val docRef = db.collection("Pets").add(pet).await()
                Log.d("PetViewModel", "Pet added with ID: ${docRef.id}")
                onSuccess()
            } catch (e: Exception) {
                Log.e("PetViewModel", "Failed to add pet", e)
            }
        }
    }

    fun updatePet(pet: Pet) {
        viewModelScope.launch {
            try {
                pet.id.let { petId ->
                    db.collection("Pets").document(petId)
                        .set(pet)
                        .await()
                    Log.d("PetViewModel", "Pet updated with ID: $petId")
                }
            } catch (e: Exception) {
                Log.e("PetViewModel", "Failed to update pet", e)
            }
        }
    }

    private fun updatePetStatsBasedOnLight(pet: Pet, lightLevel: Float) {
        var newHp = pet.hp
        var newHunger = pet.hunger
        var newHappiness = pet.happiness
        var newEnergy = pet.energy

        if (lightLevel > 100) {
            newEnergy = (newEnergy - 5).coerceAtLeast(0)
            newHunger = (newHunger - 3).coerceAtLeast(0)
            newHappiness = (newHappiness + 3).coerceAtMost(100)
            if (newHunger < 30) newHappiness = (newHappiness - 2).coerceAtLeast(0)
            if (newHunger == 0) {
                newHappiness = (newHappiness - 4).coerceAtLeast(0)
                newHp = (newHp - 2).coerceAtLeast(0)
            }
            if (newHunger >= 30) {
                newHp = (newHp + 1).coerceAtMost(100)
                newHappiness = (newHappiness + 2).coerceAtMost(100)
            }
        } else {
            newEnergy = (newEnergy + 4).coerceAtMost(100)
            newHunger = (newHunger - 2).coerceAtLeast(0)
            if (newHunger < 30) newHappiness = (newHappiness - 2).coerceAtLeast(0)
            if (newHunger == 0) {
                newHappiness = (newHappiness - 4).coerceAtLeast(0)
                newHp = (newHp - 2).coerceAtLeast(0)
            }
            if (newHunger >= 30) {
                newHp = (newHp + 1).coerceAtMost(100)
                newHappiness = (newHappiness + 2).coerceAtMost(100)
            }
        }

        val updatedPet = pet.copy(
            hp = newHp,
            hunger = newHunger,
            happiness = newHappiness,
            energy = newEnergy
        )

        updatePet(updatedPet)
    }



    fun startUpdatingPetStats(petId: String, lightLevelProvider: () -> Float) {
        viewModelScope.launch {
            while (true) {
                delay(2000)
                val pet = pets.value.find { it.id == petId }
                if (pet != null) {
                    val lightLevel = lightLevelProvider()
                    updatePetStatsBasedOnLight(pet, lightLevel)
                }
            }
        }
    }

    fun getPetIconRes(type: String, variant: Int): Int {
        return when (type.lowercase()) {
            "dog" -> when (variant) {
                1 -> R.drawable.dog1
                2 -> R.drawable.dog2
                3 -> R.drawable.dog3
                else -> R.drawable.default_icon
            }
            "cat" -> when (variant) {
                1 -> R.drawable.cat1
                2 -> R.drawable.cat2
                3 -> R.drawable.cat3
                else -> R.drawable.default_icon
            }
            "bird" -> when (variant) {
                1 -> R.drawable.bird1
                2 -> R.drawable.bird2
                3 -> R.drawable.bird3
                else -> R.drawable.default_icon
            }
            "alien" -> when (variant) {
                1 -> R.drawable.alien1
                2 -> R.drawable.alien2
                3 -> R.drawable.alien3
                else -> R.drawable.default_icon
            }
            "dragon" -> when (variant) {
                1 -> R.drawable.dragon1
                2 -> R.drawable.dragon2
                3 -> R.drawable.dragon3
                else -> R.drawable.default_icon
            }
            else -> R.drawable.default_icon
        }
    }
}