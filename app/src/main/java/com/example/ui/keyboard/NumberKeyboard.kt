package com.example.ui.keyboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Mood
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ime.EditorInfoHandler
import com.example.ime.ImeActionType
import com.example.model.KeyboardMode
import com.example.model.KeyboardThemeModel

@Composable
fun NumberKeyboard(
    theme: KeyboardThemeModel,
    showPopup: Boolean,
    actionType: ImeActionType,
    actionIcon: ImageVector,
    onKeyClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onBackspaceContinuous: () -> Unit,
    onSpaceClick: () -> Unit,
    onActionClick: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val row1Keys = remember { listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0") }
    val row2Keys = remember { listOf("@", "#", "$", "%", "&", "-", "+", "(", ")", "/") }
    val row3Keys = remember { listOf("*", "\"", "'", ":", ";", "!", "?") }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Row 1: 1 2 3 4 5 6 7 8 9 0
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            row1Keys.forEach { num ->
                KeyboardKey(
                    label = num,
                    theme = theme,
                    showPopup = showPopup,
                    modifier = Modifier.weight(1f),
                    onClick = { onKeyClick(num) }
                )
            }
        }

        // Row 2: @ # $ % & - + ( ) /
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            row2Keys.forEach { sym ->
                KeyboardKey(
                    label = sym,
                    theme = theme,
                    showPopup = showPopup,
                    modifier = Modifier.weight(1f),
                    onClick = { onKeyClick(sym) }
                )
            }
        }

        // Row 3: =\< to Symbols, * " ' : ; ! ?, Backspace
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Switch to Symbols
            KeyboardKey(
                label = "=/<",
                theme = theme,
                isSpecial = true,
                showPopup = false,
                modifier = Modifier.weight(1.4f),
                onClick = { onSwitchMode(KeyboardMode.SYMBOLS) }
            )

            row3Keys.forEach { sym ->
                KeyboardKey(
                    label = sym,
                    theme = theme,
                    showPopup = showPopup,
                    modifier = Modifier.weight(1f),
                    onClick = { onKeyClick(sym) }
                )
            }

            // Backspace
            KeyboardKey(
                label = "Delete",
                icon = Icons.AutoMirrored.Filled.Backspace,
                theme = theme,
                isSpecial = true,
                showPopup = false,
                modifier = Modifier.weight(1.4f),
                onClick = onBackspace,
                onContinuousAction = onBackspaceContinuous
            )
        }

        // Row 4: ABC, Emoji, Space, , , ., Enter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KeyboardKey(
                label = "ABC",
                theme = theme,
                isSpecial = true,
                showPopup = false,
                modifier = Modifier.weight(1.3f),
                onClick = { onSwitchMode(KeyboardMode.LETTERS) }
            )

            KeyboardKey(
                label = "Emoji",
                icon = Icons.Default.Mood,
                theme = theme,
                isSpecial = true,
                showPopup = false,
                modifier = Modifier.weight(1.0f),
                onClick = { onSwitchMode(KeyboardMode.EMOJI) }
            )

            KeyboardKey(
                label = "🌸 space",
                theme = theme,
                showPopup = false,
                modifier = Modifier.weight(4.0f),
                onClick = onSpaceClick
            )

            KeyboardKey(
                label = ",",
                theme = theme,
                showPopup = showPopup,
                modifier = Modifier.weight(0.85f),
                onClick = { onKeyClick(",") }
            )

            KeyboardKey(
                label = ".",
                theme = theme,
                showPopup = showPopup,
                modifier = Modifier.weight(0.85f),
                onClick = { onKeyClick(".") }
            )

            KeyboardKey(
                label = EditorInfoHandler.getActionLabel(actionType),
                icon = actionIcon,
                theme = theme,
                isAction = true,
                showPopup = false,
                modifier = Modifier.weight(1.4f),
                onClick = onActionClick
            )
        }
    }
}
