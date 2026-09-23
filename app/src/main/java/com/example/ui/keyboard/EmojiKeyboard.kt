package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeyboardMode
import com.example.model.KeyboardThemeModel

data class EmojiCategory(
    val title: String,
    val icon: String,
    val emojis: List<String>
)

@Composable
fun EmojiKeyboard(
    recentEmojis: List<String>,
    theme: KeyboardThemeModel,
    onEmojiSelected: (String) -> Unit,
    onBackspace: () -> Unit,
    onBackspaceContinuous: () -> Unit,
    onSpaceClick: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = remember(recentEmojis) {
        listOf(
            EmojiCategory("Recent", "🕒", recentEmojis),
            EmojiCategory(
                "Aesthetic",
                "🌸",
                listOf(
                    "🌸", "💕", "✨", "🎀", "💖", "🌷", "🍰", "🥰", "🍓", "🩰",
                    "💅", "💄", "👑", "🌹", "🌺", "🌼", "🌻", "🌙", "⭐", "🦄",
                    "💌", "💎", "💍", "🕊️", "🧁", "🧋", "🧸", "🤍", "🪄", "🪞",
                    "🥞", "🍑", "🍒", "🍨", "🍩", "🪻", "🫧", "💐", "🪽", "💓",
                    "☁️", "🪐", "🌿", "🦢", "🎐", "🐚", "🌌", "🐾", "🧦", "👒",
                    "🧸", "🎈", "🕯️", "💌", "🤍", "🩷", "🩵", "🤎", "🩶", "🖤"
                )
            ),
            EmojiCategory(
                "Smileys",
                "😊",
                listOf(
                    "😀", "😃", "😄", "😁", "😆", "🥹", "😅", "😂", "🤣", "🥲",
                    "☺️", "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘",
                    "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐",
                    "🤓", "😎", "🥸", "🤩", "🥳", "😏", "😒", "😞", "😔", "😟",
                    "😕", "🙁", "☹️", "😣", "😖", "😫", "😩", "🥺", "😢", "😭",
                    "🫠", "🫡", "🫣", "🫥", "🫤", "👽", "🤖", "👻", "💩", "💀"
                )
            ),
            EmojiCategory(
                "Hearts",
                "💖",
                listOf(
                    "❤️", "🩷", "🧡", "💛", "💚", "💙", "🩵", "💜", "🤎", "🖤",
                    "🩶", "🤍", "💔", "❤️‍🔥", "❤️‍🩹", "❣️", "💕", "💞", "💓", "💗",
                    "💖", "💘", "💝", "💟", "💌", "🫶", "💋", "👩‍❤️‍💋‍👨", "👩‍❤️‍👨", "💑",
                    "❤️‍🔥", "❤️‍🩹", "🫀", "💌", "💖", "💝", "💘", "💓", "💞", "💕"
                )
            ),
            EmojiCategory(
                "Animals",
                "🐱",
                listOf(
                    "🐱", "🐈", "🐈‍⬛", "🐶", "🐕", "🐩", "🐰", "🐇", "🦊", "🐻",
                    "🐼", "🐨", "🐯", "🦁", "🐮", "🐷", "🐸", "🐵", "🐥", "🐣",
                    "🦆", "🦉", "🦋", "🦄", "🐝", "🐞", "🐙", "🐬", "🐳", "🦭",
                    "🦩", "🦘", "🦥", "🦦", "🦢", "🪼", "🦈", "🦀", "🐌", "🦋"
                )
            ),
            EmojiCategory(
                "Food",
                "🍓",
                listOf(
                    "🍓", "🍒", "🍑", "🍎", "🍉", "🍇", "🍌", "🍋", "🥑", "🥐",
                    "🥞", "🧇", "🧀", "🍕", "🍔", "🍟", "🍣", "🍦", "🍧", "🍨",
                    "🍩", "🍪", "🎂", "🍰", "🧁", "🥧", "🍫", "🍬", "🍭", "🧋",
                    "☕", "🍵", "🥤", "🥂", "🍿", "🫕", "🫔", "🥪", "🌮", "🌯"
                )
            ),
            EmojiCategory(
                "Fun & Objects",
                "✨",
                listOf(
                    "🎉", "🎊", "🎈", "🎁", "🎨", "🎭", "🎪", "🎤", "🎧", "🎼",
                    "🎹", "🥁", "🎷", "🎸", "🎯", "🎮", "🕹️", "📷", "📸", "🔮",
                    "🪄", "🕯️", "💡", "📖", "✏️", "📌", "🔑", "🛍️", "🧴", "🧷",
                    "💎", "🔮", "🪄", "🪞", "🪮", "🧵", "🎨", "🎭", "🎫", "🎬"
                )
            )
        )
    }

    var selectedTabIndex by remember { mutableIntStateOf(1) }
    val currentCategory = categories.getOrElse(selectedTabIndex) { categories[1] }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Emoji Category Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = theme.headerBackground,
            contentColor = theme.keyActionBackground,
            edgePadding = 8.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 2.5.dp,
                    color = theme.keyActionBackground
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = category.icon,
                            fontSize = 18.sp
                        )
                    }
                )
            }
        }

        // Emoji Grid
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 42.dp),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 2.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(currentCategory.emojis) { emoji ->
                    Box(
                        modifier = Modifier
                            .height(44.dp)
                            .clip(CircleShape)
                            .clickable {
                                onEmojiSelected(emoji)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Bottom Navigation Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ABC button
            KeyboardKey(
                label = "ABC",
                theme = theme,
                isSpecial = true,
                showPopup = false,
                modifier = Modifier.weight(1.5f),
                onClick = { onSwitchMode(KeyboardMode.LETTERS) }
            )

            // Space key
            KeyboardKey(
                label = "🌸 space",
                theme = theme,
                showPopup = false,
                modifier = Modifier.weight(3.5f),
                onClick = onSpaceClick
            )

            // Backspace
            KeyboardKey(
                label = "Delete",
                icon = Icons.AutoMirrored.Filled.Backspace,
                theme = theme,
                isSpecial = true,
                showPopup = false,
                modifier = Modifier.weight(1.5f),
                onClick = onBackspace,
                onContinuousAction = onBackspaceContinuous
            )
        }
    }
}
