package com.example.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class KeyboardHeight(val label: String, val heightDp: Dp) {
    SMALL("Small", 225.dp),
    NORMAL("Normal", 260.dp),
    LARGE("Large", 295.dp);

    companion object {
        fun fromName(name: String?): KeyboardHeight {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: NORMAL
        }
    }
}
