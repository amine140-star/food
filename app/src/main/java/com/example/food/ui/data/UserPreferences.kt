package com.example.food.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.food.data.User


@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val address: String,
    val preferences: List<String>
)

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USERS = "key_users"
        private const val KEY_CURRENT_USER_ID = "key_current_user_id"
    }

    // Configure le JSON pour inclure les valeurs par défaut et ignorer les champs inconnus
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    /**
     * Retourne la liste des utilisateurs enregistrés (décodée depuis JSON),
     * ou une liste vide si aucune donnée n'est présente.
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
     * Sauvegarde la liste complète d'utilisateurs (encodée en JSON).
     * Appelé en interne chaque fois que la liste est modifiée.
     */
    private fun saveUsersList(users: List<User>) {
        val serialized = json.encodeToString(users)
        prefs.edit().putString(KEY_USERS, serialized).apply()
    }

    /**
     * Ajoute un nouvel utilisateur à la liste existante et ré‐sauvegarde.
     * - id est calculé comme +1 après l'id max actuel (ou 1 si liste vide).
     * - Les nouveaux champs phone, address, preferences sont pris en compte.
     */
    fun saveUser(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String,
        preferences: List<String>
    ) {
        val currentList = getUsers().toMutableList()
        val nextId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        val newUser = User(
            id = nextId,
            name = name,
            email = email,
            password = password,
            phone = phone,
            address = address,
            preferences = preferences
        )
        currentList.add(newUser)
        saveUsersList(currentList)
    }

    /**
     * Surcharge de saveUser pour ne stocker que les champs historiques
     * (id, name, email, password) si nécessaire. Les nouveaux champs deviennent vides.
     */
    fun saveUser(
        name: String,
        email: String,
        password: String
    ) {
        saveUser(name, email, password, phone = "", address = "", preferences = emptyList())
    }

    /**
     * Tente d'authentifier un couple (email, mot de passe).
     * Si valide, mémorise l'id dans KEY_CURRENT_USER_ID et retourne l'objet User.
     * Sinon, retourne null.
     */
    fun authenticate(email: String, password: String): User? {
        val found = getUsers().firstOrNull { it.email == email && it.password == password }
        return if (found != null) {
            prefs.edit().putInt(KEY_CURRENT_USER_ID, found.id).apply()
            found
        } else {
            null
        }
    }

    /**
     * Retourne l'utilisateur actuellement connecté (basé sur l'id stocké),
     * ou null s'il n'y a pas d'utilisateur connecté.
     */
    fun getCurrentUser(): User? {
        val id = prefs.getInt(KEY_CURRENT_USER_ID, -1)
        return if (id < 0) null else getUsers().firstOrNull { it.id == id }
    }

    /**
     * Déconnecte l'utilisateur en supprimant l'id courant de SharedPreferences.
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
