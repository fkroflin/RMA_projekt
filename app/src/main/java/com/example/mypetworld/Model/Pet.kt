package com.example.mypetworld.Model

data class Pet (
    var id: String = "",
    var hp: Int = 100,
    var hunger: Int = 100,
    var happiness: Int = 100,
    var energy: Int = 100,
    val type: String = "",
    val name: String = "",
    var variant: Int = 1,
    var userId: String = ""
)