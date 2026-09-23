package com.example.data

import com.example.model.KeyboardHeight
import com.example.model.KeyboardThemeModel

data class KeyboardPreferences(
    val themeId: String = KeyboardThemeModel.PinkDream.id,
    val soundEnabled: Boolean = false,
    val hapticEnabled: Boolean = true,
    val popupEnabled: Boolean = true,
    val height: KeyboardHeight = KeyboardHeight.NORMAL,
    val recentEmojis: List<String> = listOf("🌸", "💕", "✨", "🥰", "🎀", "💖", "🌷", "🍰")
) {
    val currentTheme: KeyboardThemeModel
        get() = KeyboardThemeModel.fromId(themeId)
}
