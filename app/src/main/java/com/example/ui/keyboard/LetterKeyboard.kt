package com.example.ui.keyboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Lock
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
import com.example.model.ShiftState

@Composable
fun LetterKeyboard(
    shiftState: ShiftState,
    theme: KeyboardThemeModel,
    showPopup: Boolean,
    actionType: ImeActionType,
    actionIcon: ImageVector,
    isEmailField: Boolean,
    onKeyClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onBackspaceContinuous: () -> Unit,
    onShiftClick: () -> Unit,
    onShiftDoubleClick: () -> Unit,
    onSpaceClick: () -> Unit,
    onActionClick: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val row1Keys = remember {
        listOf(
            "q" to "1", "w" to "2", "e" to "3", "r" to "4", "t" to "5",
            "y" to "6", "u" to "7", "i" to "8", "o" to "9", "p" to "0"
        )
    }

    val row2Keys = remember {
        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
    }

    val row3Keys = remember {
        listOf("z", "x", "c", "v", "b", "n", "m")
    }

    val isUpper = shiftState.isUppercase

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Row 1: Q W E R T Y U I O P
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            row1Keys.forEach { (char, alt) ->
                val displayChar = if (isUpper) char.uppercase() else char
                KeyboardKey(
                    label = displayChar,
                    altLabel = alt,
                    theme = theme,
                    showPopup = showPopup,
                    modifier = Modifier.weight(1f),
                    onClick = { onKeyClick(char) },
                    onLongClick = { onKeyClick(alt) }
                )
            }
        }

        // Row 2: A S D F G H J K L (with subtle padding indent for comfortable typing)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            row2Keys.forEach { char ->
                val displayChar = if (isUpper) char.uppercase() else char
                KeyboardKey(
                    label = displayChar,
                    theme = theme,
                    showPopup = showPopup,
                    modifier = Modifier.weight(1f),
                    onClick = { onKeyClick(char) }
                )
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }

        // Row 3: Shift, Z X C V B N M, Backspace
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shift Key
            val shiftIcon = if (shiftState == ShiftState.CAPS_LOCK) Icons.Default.Lock else Icons.Default.ArrowUpward
            KeyboardKey(
                label = "Shift",
                icon = shiftIcon,
                theme = theme,
                isSpecial = true,
                activeHighlight = shiftState != ShiftState.OFF,
                showPopup = false,
                modifier = Modifier.weight(1.4f),
                onClick = onShiftClick,
                onLongClick = onShiftDoubleClick
            )

            row3Keys.forEach { char ->
                val displayChar = if (isUpper) char.uppercase() else char
                KeyboardKey(
                    label = displayChar,
                    theme = theme,
                    showPopup = showPopup,
                    modifier = Modifier.weight(1f),
                    onClick = { onKeyClick(char) }
                )
            }

            // Backspace Key
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

        // Row 4: ?123, Emoji, Space bar, Comma/Period, Enter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Numbers switch
            KeyboardKey(
                label = "?123",
                theme = theme,
                isSpecial = true,
                showPopup = false,
                modifier = Modifier.weight(1.3f),
                onClick = { onSwitchMode(KeyboardMode.NUMBERS) }
            )

            // Emoji button
            KeyboardKey(
                label = "Emoji",
                icon = Icons.Default.Mood,
                theme = theme,
                isSpecial = true,
                showPopup = false,
                modifier = Modifier.weight(1.0f),
                onClick = { onSwitchMode(KeyboardMode.EMOJI) }
            )

            // Space Bar
            KeyboardKey(
                label = "🌸 space",
                theme = theme,
                showPopup = false,
                modifier = Modifier.weight(4.0f),
                onClick = onSpaceClick
            )

            // Comma / Period / @ for email
            if (isEmailField) {
                KeyboardKey(
                    label = "@",
                    theme = theme,
                    showPopup = showPopup,
                    modifier = Modifier.weight(0.9f),
                    onClick = { onKeyClick("@") }
                )
            } else {
                KeyboardKey(
                    label = ",",
                    theme = theme,
                    showPopup = showPopup,
                    modifier = Modifier.weight(0.85f),
                    onClick = { onKeyClick(",") }
                )
            }

            KeyboardKey(
                label = ".",
                theme = theme,
                showPopup = showPopup,
                modifier = Modifier.weight(0.85f),
                onClick = { onKeyClick(".") }
            )

            // Enter / Action Key
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