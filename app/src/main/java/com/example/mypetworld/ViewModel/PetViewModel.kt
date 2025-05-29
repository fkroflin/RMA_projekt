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

}