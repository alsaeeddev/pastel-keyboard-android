package com.example.ime

import android.view.KeyEvent
import android.view.inputmethod.InputConnection

class KeyboardInputConnection(var connection: InputConnection?) {

    fun commitText(text: String) {
        connection?.commitText(text, 1)
    }

    fun deleteBackward() {
        val conn = connection ?: return
        // Check if there is selected text first
        val selected = conn.getSelectedText(0)
        if (!selected.isNullOrEmpty()) {
            conn.commitText("", 1)
        } else {
            // Delete one character before cursor
            val deleted = conn.deleteSurroundingText(1, 0)
            if (!deleted) {
                // Fallback to sending standard Backspace key event
                conn.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                conn.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
            }
        }
    }

    fun deleteWord() {
        val conn = connection ?: return
        val beforeText = conn.getTextBeforeCursor(50, 0)?.toString() ?: ""
        if (beforeText.isEmpty()) {
            deleteBackward()
            return
        }

        var i = beforeText.length - 1
        // Skip trailing spaces
        while (i >= 0 && beforeText[i].isWhitespace()) {
            i--
        }
        // Delete backward until next whitespace or start
        while (i >= 0 && !beforeText[i].isWhitespace()) {
            i--
        }
        val deleteCount = beforeText.length - 1 - i
        if (deleteCount > 0) {
            conn.deleteSurroundingText(deleteCount, 0)
        } else {
            deleteBackward()
        }
    }
}
