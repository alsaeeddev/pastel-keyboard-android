package com.example.model

enum class ShiftState {
    OFF,
    ON,
    CAPS_LOCK;

    val isUppercase: Boolean
        get() = this == ON || this == CAPS_LOCK

    fun toggle(): ShiftState {
        return when (this) {
            OFF -> ON
            ON -> OFF
            CAPS_LOCK -> OFF
        }
    }
}
