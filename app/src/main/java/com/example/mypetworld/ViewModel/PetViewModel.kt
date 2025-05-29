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


class PetViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets
    init {
        try {
            fetchPets()
        } catch (e: Exception) {
            Log.e("MyViewModel", "Initialization failed", e)
        }
    }
    private fun fetchPets() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("Pets").get().await()
                val petList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Pet::class.java)?.apply {
                        id = doc.id
                    }
                }
                _pets.value = petList
            } catch (e: Exception) {
            }
        }
    }

//    private fun fetchPets() {
//        viewModelScope.launch {
//            try {
//                val snapshot = db.collection("Pets").get().await()
//                val petList = snapshot.documents.mapNotNull { doc ->
//                    doc.toObject(Pet::class.java)?.let { pet ->
//                        Log.d("PetViewModel", "Fetched pet: ${pet.name}, ${pet.type}, hp: ${pet.hp}")
//                        pet.copy(
//                            id = doc.id,
//                            name = pet.name.lowercase(),
//                            type = pet.type.lowercase()
//                        )
//                    }
//                }
//                _pets.value = petList
//            } catch (e: Exception) {
//                Log.e("PetViewModel", "Failed to fetch pets", e)
//            }
//        }
//    }


//    private fun fetchPets() {
//        viewModelScope.launch {
//            try {
//                val snapshot = db.collection("Pets").get().await()
//                val petList = snapshot.documents.mapNotNull { doc ->
//                    val name = doc.getString("name")?.lowercase()
//                    val type = doc.getString("type")?.lowercase()
//                    val hp = doc.getLong("hp")?.toInt() ?: 0
//                    val hunger = doc.getLong("hunger")?.toInt() ?: 0
//                    val happiness = doc.getLong("happiness")?.toInt() ?: 0
//                    val energy = doc.getLong("energy")?.toInt() ?: 0
//
//                    if (name != null && type != null) {
//                        Pet(
//                            id = doc.id,
//                            name = name,
//                            type = type,
//                            hp = hp,
//                            hunger = hunger,
//                            happiness = happiness,
//                            energy = energy
//                        )
//                    } else {
//                        null
//                    }
//                }
//                _pets.value = petList
//            } catch (e: Exception) {
//                Log.e("PetViewModel", "Failed to fetch pets", e)
//            }
//        }
//    }


}