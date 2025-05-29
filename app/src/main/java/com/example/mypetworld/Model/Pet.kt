package com.example.mypetworld.Model

data class Pet (
    var id: String = "",
    val hp: Int = 100,
    val hunger: Int = 100,
    val happiness: Int = 100,
    val energy: Int = 100,
    val type: String = "",
    val name: String = ""
)