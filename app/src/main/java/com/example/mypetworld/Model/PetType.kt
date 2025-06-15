package com.example.mypetworld.Model

enum class PetType(val displayName: String, val description: String, val maxVariant: Int) {
    dog("Dog", "Dogs are loyal and playful companions, perfect for active owners!", 3),
    cat("Cat", "Cats are independent and curious, ideal for a cozy home!",3 ),
    bird("Bird", "Birds are cheerful and colorful, bringing song to your life!",3 ),
    alien("Alien", "Aliens are mysterious creatures from the cosmos!",3 ),
    dragon("Dragon", "Dragons are legendary beasts with immense power!",3 )
}
//val pet = petViewModel.pets.collectAsState().value.find { it.id == petId }