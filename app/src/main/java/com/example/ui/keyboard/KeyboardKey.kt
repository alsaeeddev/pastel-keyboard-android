package com.example.ui.keyboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.KeyboardThemeModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun KeyboardKey(
    label: String,
    modifier: Modifier = Modifier,
    altLabel: String? = null,
    icon: ImageVector? = null,
    theme: KeyboardThemeModel,
    isSpecial: Boolean = false,
    isAction: Boolean = false,
    showPopup: Boolean = true,
    activeHighlight: Boolean = false,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onContinuousAction: (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var repeatJob by remember { mutableStateOf<Job?>(null) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = tween(durationMillis = 60),
        label = "key_scale"
    )

    val keyBg = when {
        isAction -> theme.keyActionBackground
        activeHighlight -> theme.keyActionBackground.copy(alpha = 0.85f)
        isSpecial -> theme.keySpecialBackground
        else -> theme.keyBackground
    }

    val textColor = when {
        isAction || activeHighlight -> theme.keyActionText
        isSpecial -> theme.keySpecialText
        else -> theme.keyText
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 2.5.dp, vertical = 3.5.dp)
            .zIndex(if (isPressed) 10f else 1f)
            .testTag("key_$label"),
        contentAlignment = Alignment.Center
    ) {
        // Key Character Popup Bubble (enlarged preview above the key)
        if (showPopup && isPressed && !isSpecial && !isAction && icon == null && label.length == 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-46).dp)
                    .size(width = 46.dp, height = 44.dp)
                    .zIndex(99f)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp))
                    .background(theme.popupBg, RoundedCornerShape(12.dp))
                    .border(1.dp, theme.keyBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = theme.popupText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        // The actual Key Surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .shadow(
                    elevation = if (isPressed) 0.5.dp else 2.dp,
                    shape = RoundedCornerShape(9.dp),
                    ambientColor = theme.keyShadow,
                    spotColor = theme.keyShadow
                )
                .background(keyBg, RoundedCornerShape(9.dp))
                .border(
                    width = if (isAction || activeHighlight) 1.2.dp else 0.8.dp,
                    color = if (isAction || activeHighlight) Color.Transparent else theme.keyBorder,
                    shape = RoundedCornerShape(9.dp)
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            if (onContinuousAction != null) {
                                repeatJob = scope.launch {
                                    onContinuousAction()
                                    delay(400) // Initial delay before repeat
                                    while (isActive) {
                                        onContinuousAction()
                                        delay(50) // Fast repeat interval
                                    }
                                }
                            }
                            tryAwaitRelease()
                            repeatJob?.cancel()
                            repeatJob = null
                            isPressed = false
                        },
                        onTap = {
                            onClick()
                        },
                        onLongPress = {
                            if (onLongClick != null) {
                                onLongClick()
                            } else if (altLabel != null) {
                                // Default long-press inserts altLabel if present
                                onClick()
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Secondary / Alternate Character Hint (e.g. number above letter)
            if (altLabel != null && !isSpecial && !isAction) {
                Text(
                    text = altLabel,
                    color = textColor.copy(alpha = 0.55f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 4.dp)
                )
            }

            // Key Icon or Primary Text
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = label,
                    color = textColor,
                    fontSize = if (label.length > 2) 13.sp else 18.sp,
                    fontWeight = if (isSpecial || isAction) FontWeight.Bold else FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
