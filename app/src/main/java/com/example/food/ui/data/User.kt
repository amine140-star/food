package com.example.food.data

import kotlinx.serialization.Serializable

/**
 * Représente un utilisateur avec :
 *  - un id unique (auto‐incrémenté)
 *  - un nom complet
 *  - un email
 *  - un mot de passe (en clair pour l’exemple ; en production, hachez‐le !)
 */
@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)
