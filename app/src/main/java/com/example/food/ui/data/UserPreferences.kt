package com.example.food.data

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Classe pour gérer le stockage local des utilisateurs dans SharedPreferences.
 * On sauvegarde la liste des utilisateurs au format JSON sous la clé "key_users".
 * On sauvegarde également l'id du dernier utilisateur connecté dans "key_current_user_id".
 */
class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USERS = "key_users"
        private const val KEY_CURRENT_USER_ID = "key_current_user_id"
    }

    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    /**
     * Retourne la liste des utilisateurs enregistrés, ou une liste vide.
     */
    fun getUsers(): List<User> {
        val usersJson = prefs.getString(KEY_USERS, null) ?: return emptyList()
        return try {
            json.decodeFromString(usersJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Sauvegarde la liste complète d'utilisateurs.
     * (Utilisé en interne après modification de la liste.)
     */
    private fun saveUsersList(users: List<User>) {
        val serialized = json.encodeToString(users)
        prefs.edit().putString(KEY_USERS, serialized).apply()
    }

    /**
     * Ajoute un nouvel utilisateur à la liste et ré‐sauvegarde.
     * L'id est calculé comme +1 après le max existant, ou 1 si liste vide.
     */
    fun saveUser(name: String, email: String, password: String) {
        val currentList = getUsers().toMutableList()
        val nextId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        val newUser = User(id = nextId, name = name, email = email, password = password)
        currentList.add(newUser)
        saveUsersList(currentList)
    }

    /**
     * Tente d'authentifier un couple (email, mot de passe).
     * Si OK, sauvegarde l'id de l'utilisateur courant et retourne cet utilisateur.
     * Sinon, retourne null.
     */
    fun authenticate(email: String, password: String): User? {
        val found = getUsers().firstOrNull { it.email == email && it.password == password }
        return if (found != null) {
            // Mémorise l'id de l'utilisateur actuellement connecté
            prefs.edit().putInt(KEY_CURRENT_USER_ID, found.id).apply()
            found
        } else {
            null
        }
    }

    /**
     * Retourne l'utilisateur actuellement connecté (basé sur last ID sauvegardé), ou null.
     */
    fun getCurrentUser(): User? {
        val id = prefs.getInt(KEY_CURRENT_USER_ID, -1)
        return if (id < 0) null else getUsers().firstOrNull { it.id == id }
    }

    /**
     * Déconnecte en supprimant l'id courant (optionnel).
     */
    fun clearCurrentUser() {
        prefs.edit().remove(KEY_CURRENT_USER_ID).apply()
    }

    /**
     * Pour débogage : supprime tous les utilisateurs et l'id courant.
     */
    fun clearAllData() {
        prefs.edit().remove(KEY_USERS).remove(KEY_CURRENT_USER_ID).apply()
    }
}
