package com.example.food.data

/**
 * Représente un plat avec un id, un nom, une description, un prix et un visuel.
 */
data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageRes: Int
)


