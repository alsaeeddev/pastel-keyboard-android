package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ime.ImeActionType
import com.example.model.KeyboardHeight
import com.example.model.KeyboardMode
import com.example.model.KeyboardThemeModel
import com.example.model.ShiftState

@Composable
fun KeyboardScreen(
    mode: KeyboardMode,
    shiftState: ShiftState,
    theme: KeyboardThemeModel,
    height: KeyboardHeight,
    showPopup: Boolean,
    actionType: ImeActionType,
    actionIcon: ImageVector,
    isEmailField: Boolean,
    recentEmojis: List<String>,
    suggestions: List<String> = emptyList(),
    onKeyClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onBackspaceContinuous: () -> Unit,
    onShiftClick: () -> Unit,
    onShiftDoubleClick: () -> Unit,
    onSpaceClick: () -> Unit,
    onActionClick: () -> Unit,
    onSuggestionClick: (String) -> Unit = {},
    onSwitchMode: (KeyboardMode) -> Unit,
    onSwitchIme: () -> Unit,
    onDismissKeyboard: () -> Unit,
    onOpenSettings: () -> Unit,
    onEmojiSelected: (String) -> Unit,
    onNextTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height.heightDp + 36.dp),
        color = androidx.compose.ui.graphics.Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(theme.backgroundBrush)
        ) {
            // Quick Toolbar / Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(theme.headerBackground)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Switch IME icon
                IconButton(
                    onClick = onSwitchIme,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Switch Input Method",
                        tint = theme.keySpecialText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Quick aesthetic emojis bar or Word Suggestions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (suggestions.isNotEmpty()) {
                        suggestions.forEach { suggestion ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(theme.keySpecialBackground.copy(alpha = 0.5f))
                                    .clickable { onSuggestionClick(suggestion) }
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = suggestion,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = theme.keyText
                                )
                            }
                        }
                    } else {
                        val quickEmojis = listOf("🌸", "💕", "✨", "🥰", "🎀")
                        quickEmojis.forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .clickable { onKeyClick(emoji) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 16.sp)
                            }
                        }
                    }
                }

                // Right controls: Switch theme, Settings, Hide keyboard
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNextTheme,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Switch Theme",
                            tint = theme.accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Keyboard Settings",
                            tint = theme.keySpecialText,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismissKeyboard,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardHide,
                            contentDescription = "Hide Keyboard",
                            tint = theme.keySpecialText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Keyboard Body Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 2.dp, vertical = 2.dp)
            ) {
                when (mode) {
                    KeyboardMode.LETTERS -> {
                        LetterKeyboard(
                            shiftState = shiftState,
                            theme = theme,
                            showPopup = showPopup,
                            actionType = actionType,
                            actionIcon = actionIcon,
                            isEmailField = isEmailField,
                            onKeyClick = onKeyClick,
                            onBackspace = onBackspace,
                            onBackspaceContinuous = onBackspaceContinuous,
                            onShiftClick = onShiftClick,
                            onShiftDoubleClick = onShiftDoubleClick,
                            onSpaceClick = onSpaceClick,
                            onActionClick = onActionClick,
                            onSwitchMode = onSwitchMode,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    KeyboardMode.NUMBERS -> {
                        NumberKeyboard(
                            theme = theme,
                            showPopup = showPopup,
                            actionType = actionType,
                            actionIcon = actionIcon,
                            onKeyClick = onKeyClick,
                            onBackspace = onBackspace,
                            onBackspaceContinuous = onBackspaceContinuous,
                            onSpaceClick = onSpaceClick,
                            onActionClick = onActionClick,
                            onSwitchMode = onSwitchMode,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    KeyboardMode.SYMBOLS -> {
                        SymbolKeyboard(
                            theme = theme,
                            showPopup = showPopup,
                            actionType = actionType,
                            actionIcon = actionIcon,
                            onKeyClick = onKeyClick,
                            onBackspace = onBackspace,
                            onBackspaceContinuous = onBackspaceContinuous,
                            onSpaceClick = onSpaceClick,
                            onActionClick = onActionClick,
                            onSwitchMode = onSwitchMode,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    KeyboardMode.EMOJI -> {
                        EmojiKeyboard(
                            recentEmojis = recentEmojis,
                            theme = theme,
                            onEmojiSelected = onEmojiSelected,
                            onBackspace = onBackspace,
                            onBackspaceContinuous = onBackspaceContinuous,
                            onSpaceClick = onSpaceClick,
                            onSwitchMode = onSwitchMode,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
