package ru.pro.beatrate.data.data_store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val USERNAME_KEY = stringPreferencesKey("username")
    }

    val usernameFlow: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[USERNAME_KEY] }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
        }
    }

    private val dataStore = context.dataStore

    suspend fun clearUsername() {
        context.dataStore.edit { prefs ->
            prefs.remove(USERNAME_KEY)
        }
    }
    val tokenFlow: Flow<String> = dataStore.data
        .map { it[stringPreferencesKey("jwt_token")] ?: "" }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[stringPreferencesKey("jwt_token")] = token }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("user_role")] = role
        }
    }

    val roleFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[stringPreferencesKey("user_role")] }

    val THEME_KEY = booleanPreferencesKey("is_dark_theme")

    val isDarkThemeFlow: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[THEME_KEY] ?: false }

    suspend fun saveTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }


}