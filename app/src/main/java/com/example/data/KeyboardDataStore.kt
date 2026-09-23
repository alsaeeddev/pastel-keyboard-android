package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.KeyboardHeight
import com.example.model.KeyboardThemeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "keyboard_preferences")

class KeyboardDataStore(private val context: Context) {

    companion object {
        val KEY_THEME_ID = stringPreferencesKey("theme_id")
        val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val KEY_HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val KEY_POPUP_ENABLED = booleanPreferencesKey("popup_enabled")
        val KEY_HEIGHT = stringPreferencesKey("keyboard_height")
        val KEY_RECENT_EMOJIS = stringPreferencesKey("recent_emojis")

        @Volatile
        private var INSTANCE: KeyboardDataStore? = null

        fun getInstance(context: Context): KeyboardDataStore {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: KeyboardDataStore(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    val preferencesFlow: Flow<KeyboardPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val themeId = prefs[KEY_THEME_ID] ?: KeyboardThemeModel.PinkDream.id
            val sound = prefs[KEY_SOUND_ENABLED] ?: false
            val haptic = prefs[KEY_HAPTIC_ENABLED] ?: true
            val popup = prefs[KEY_POPUP_ENABLED] ?: true
            val heightName = prefs[KEY_HEIGHT] ?: KeyboardHeight.NORMAL.name
            val emojisRaw = prefs[KEY_RECENT_EMOJIS] ?: "🌸,💕,✨,🥰,🎀,💖,🌷,🍰"

            val emojisList = emojisRaw.split(",").filter { it.isNotBlank() }

            KeyboardPreferences(
                themeId = themeId,
                soundEnabled = sound,
                hapticEnabled = haptic,
                popupEnabled = popup,
                height = KeyboardHeight.fromName(heightName),
                recentEmojis = if (emojisList.isNotEmpty()) emojisList else listOf("🌸", "💕", "✨", "🥰", "🎀", "💖")
            )
        }

    suspend fun setTheme(themeId: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME_ID] = themeId
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SOUND_ENABLED] = enabled
        }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HAPTIC_ENABLED] = enabled
        }
    }

    suspend fun setPopupEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_POPUP_ENABLED] = enabled
        }
    }

    suspend fun setHeight(height: KeyboardHeight) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HEIGHT] = height.name
        }
    }

    suspend fun addRecentEmoji(emoji: String) {
        context.dataStore.edit { prefs ->
            val current = (prefs[KEY_RECENT_EMOJIS] ?: "🌸,💕,✨,🥰,🎀,💖,🌷,🍰")
                .split(",")
                .filter { it.isNotBlank() && it != emoji }
                .toMutableList()
            current.add(0, emoji)
            val trimmed = current.take(24).joinToString(",")
            prefs[KEY_RECENT_EMOJIS] = trimmed
        }
    }
}
