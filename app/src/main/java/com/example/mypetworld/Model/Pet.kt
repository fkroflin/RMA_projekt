package com.example.mypetworld.Model

data class Pet (
    var id: String = "",
    val HP: Int = 100,
    val Hunger: Int = 100,
    val Happiness: Int = 100,
    val Energy: Int = 100,
    val Type: String = "",
    val Name: String = ""
)