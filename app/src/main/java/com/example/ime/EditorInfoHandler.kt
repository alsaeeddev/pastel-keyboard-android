package com.example.ime

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.util.InputTypeUtils

enum class ImeActionType {
    SEARCH,
    SEND,
    GO,
    NEXT,
    DONE,
    ENTER
}

object EditorInfoHandler {

    fun getActionType(editorInfo: EditorInfo?): ImeActionType {
        if (editorInfo == null) return ImeActionType.ENTER

        val imeOptions = editorInfo.imeOptions
        val action = imeOptions and EditorInfo.IME_MASK_ACTION
        val isMultiLine = InputTypeUtils.isMultiLine(editorInfo)
        val noEnterAction = (imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0

        // IMPORTANT: per the Android docs, IME_FLAG_NO_ENTER_ACTION means the action
        // must NOT replace the Enter key (Android's own TextView sets this flag
        // automatically on every multi-line field, e.g. WhatsApp's message box).
        // So when this flag is set on a multiline field, Enter must stay Enter
        // (insert a newline) no matter what action (SEND/DONE/etc.) is also set.
        if (isMultiLine && noEnterAction) {
            return ImeActionType.ENTER
        }

        // Multiline field with no explicit action at all -> also just Enter
        if (isMultiLine && (action == EditorInfo.IME_ACTION_NONE || action == EditorInfo.IME_ACTION_UNSPECIFIED)) {
            return ImeActionType.ENTER
        }

        return when (action) {
            EditorInfo.IME_ACTION_SEARCH -> ImeActionType.SEARCH
            EditorInfo.IME_ACTION_SEND -> ImeActionType.SEND
            EditorInfo.IME_ACTION_GO -> ImeActionType.GO
            EditorInfo.IME_ACTION_NEXT -> ImeActionType.NEXT
            EditorInfo.IME_ACTION_DONE -> ImeActionType.DONE
            else -> if (isMultiLine) ImeActionType.ENTER else ImeActionType.DONE
        }
    }

    fun getActionIcon(actionType: ImeActionType): ImageVector {
        return when (actionType) {
            ImeActionType.SEARCH -> Icons.Default.Search
            ImeActionType.SEND -> Icons.AutoMirrored.Filled.Send
            ImeActionType.GO -> Icons.AutoMirrored.Filled.ArrowForward
            ImeActionType.NEXT -> Icons.AutoMirrored.Filled.ArrowForward
            ImeActionType.DONE -> Icons.Default.Check
            ImeActionType.ENTER -> Icons.AutoMirrored.Filled.KeyboardReturn
        }
    }

    fun getActionLabel(actionType: ImeActionType): String {
        return when (actionType) {
            ImeActionType.SEARCH -> "Search"
            ImeActionType.SEND -> "Send"
            ImeActionType.GO -> "Go"
            ImeActionType.NEXT -> "Next"
            ImeActionType.DONE -> "Done"
            ImeActionType.ENTER -> "Enter"
        }
    }

    fun executeAction(inputConnection: InputConnection?, editorInfo: EditorInfo?) {
        if (inputConnection == null) return

        val actionType = getActionType(editorInfo)

        if (actionType == ImeActionType.ENTER) {
            // Insert a literal newline. commitText("\n") is honored by every
            // EditText-based widget (including WhatsApp's), whereas a synthetic
            // KEYCODE_ENTER KeyEvent is not guaranteed to be handled the same way.
            inputConnection.commitText("\n", 1)
            return
        }

        val action = (editorInfo?.imeOptions ?: 0) and EditorInfo.IME_MASK_ACTION
        if (action != EditorInfo.IME_ACTION_NONE && action != EditorInfo.IME_ACTION_UNSPECIFIED) {
            inputConnection.performEditorAction(action)
        } else {
            inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
        }
    }
}