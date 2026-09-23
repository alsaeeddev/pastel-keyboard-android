package com.example.model

sealed class KeyAction {
    data class InsertText(val text: String) : KeyAction()
    object Backspace : KeyAction()
    object Shift : KeyAction()
    object Enter : KeyAction()
    object Space : KeyAction()
    data class SwitchMode(val targetMode: KeyboardMode) : KeyAction()
    object SwitchIme : KeyAction()
    object OpenSettings : KeyAction()
}

data class KeyItem(
    val primaryText: String,
    val altText: String? = null,
    val action: KeyAction,
    val weight: Float = 1.0f,
    val isSpecial: Boolean = false,
    val isAction: Boolean = false
)
