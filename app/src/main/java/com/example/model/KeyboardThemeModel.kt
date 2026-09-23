package com.example.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class KeyboardThemeModel(
    val id: String,
    val displayName: String,
    val emojiIcon: String,
    val isDark: Boolean,
    val backgroundColors: List<Color>,
    val headerBackground: Color,
    val keyBackground: Color,
    val keySpecialBackground: Color,
    val keyActionBackground: Color,
    val keyText: Color,
    val keySpecialText: Color,
    val keyActionText: Color,
    val keyBorder: Color,
    val keyShadow: Color,
    val popupBg: Color,
    val popupText: Color,
    val accentColor: Color
) {
    val backgroundBrush: Brush
        get() = Brush.verticalGradient(backgroundColors)

    companion object {
        val PinkDream = KeyboardThemeModel(
            id = "pink_dream",
            displayName = "Pink Dream",
            emojiIcon = "🌸",
            isDark = false,
            backgroundColors = listOf(Color(0xFFFFF0F5), Color(0xFFFCE4EC), Color(0xFFF8BBD0)),
            headerBackground = Color(0x33F48FB1),
            keyBackground = Color(0xFFFFFFFF),
            keySpecialBackground = Color(0xFFF8D7E3),
            keyActionBackground = Color(0xFFEC407A),
            keyText = Color(0xFF4A154B),
            keySpecialText = Color(0xFF6A1B9A),
            keyActionText = Color(0xFFFFFFFF),
            keyBorder = Color(0x33EC407A),
            keyShadow = Color(0x22880E4F),
            popupBg = Color(0xFFFFFFFF),
            popupText = Color(0xFFD81B60),
            accentColor = Color(0xFFEC407A)
        )

        val Lavender = KeyboardThemeModel(
            id = "lavender",
            displayName = "Lavender",
            emojiIcon = "💜",
            isDark = false,
            backgroundColors = listOf(Color(0xFFF3E5F5), Color(0xFFEDE7F6), Color(0xFFD1C4E9)),
            headerBackground = Color(0x33B39DDB),
            keyBackground = Color(0xFFFFFFFF),
            keySpecialBackground = Color(0xFFE1BEE7),
            keyActionBackground = Color(0xFF8E24AA),
            keyText = Color(0xFF311B92),
            keySpecialText = Color(0xFF4A148C),
            keyActionText = Color(0xFFFFFFFF),
            keyBorder = Color(0x337E57C2),
            keyShadow = Color(0x22311B92),
            popupBg = Color(0xFFFFFFFF),
            popupText = Color(0xFF7B1FA2),
            accentColor = Color(0xFF8E24AA)
        )

        val Peach = KeyboardThemeModel(
            id = "peach",
            displayName = "Peach",
            emojiIcon = "🍑",
            isDark = false,
            backgroundColors = listOf(Color(0xFFFFF8E7), Color(0xFFFFE0B2), Color(0xFFFFCCBC)),
            headerBackground = Color(0x33FFAB91),
            keyBackground = Color(0xFFFFFFFF),
            keySpecialBackground = Color(0xFFFFE0B2),
            keyActionBackground = Color(0xFFFF7043),
            keyText = Color(0xFF4E342E),
            keySpecialText = Color(0xFFBF360C),
            keyActionText = Color(0xFFFFFFFF),
            keyBorder = Color(0x33FF7043),
            keyShadow = Color(0x22D84315),
            popupBg = Color(0xFFFFFFFF),
            popupText = Color(0xFFE64A19),
            accentColor = Color(0xFFFF7043)
        )

        val Rose = KeyboardThemeModel(
            id = "rose",
            displayName = "Rose",
            emojiIcon = "🌹",
            isDark = false,
            backgroundColors = listOf(Color(0xFFFFF0F3), Color(0xFFFFCCD5), Color(0xFFFFB3C1)),
            headerBackground = Color(0x33FB6F92),
            keyBackground = Color(0xFFFFFFFF),
            keySpecialBackground = Color(0xFFFFCAD4),
            keyActionBackground = Color(0xFFC2185B),
            keyText = Color(0xFF590D22),
            keySpecialText = Color(0xFF800F2F),
            keyActionText = Color(0xFFFFFFFF),
            keyBorder = Color(0x33C2185B),
            keyShadow = Color(0x22590D22),
            popupBg = Color(0xFFFFFFFF),
            popupText = Color(0xFFC2185B),
            accentColor = Color(0xFFC2185B)
        )

        val Midnight = KeyboardThemeModel(
            id = "midnight",
            displayName = "Midnight",
            emojiIcon = "🌙",
            isDark = true,
            backgroundColors = listOf(Color(0xFF1A1128), Color(0xFF26143E), Color(0xFF13091E)),
            headerBackground = Color(0x339C27B0),
            keyBackground = Color(0xFF33204E),
            keySpecialBackground = Color(0xFF45276D),
            keyActionBackground = Color(0xFFE040FB),
            keyText = Color(0xFFF3E5F5),
            keySpecialText = Color(0xFFE1BEE7),
            keyActionText = Color(0xFFFFFFFF),
            keyBorder = Color(0x33E040FB),
            keyShadow = Color(0x44000000),
            popupBg = Color(0xFF2A1643),
            popupText = Color(0xFFFF80AB),
            accentColor = Color(0xFFE040FB)
        )

        val allThemes = listOf(PinkDream, Lavender, Peach, Rose, Midnight)

        fun fromId(id: String?): KeyboardThemeModel {
            return allThemes.firstOrNull { it.id == id } ?: PinkDream
        }
    }
}
