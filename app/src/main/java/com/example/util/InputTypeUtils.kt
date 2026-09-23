package com.example.util

import android.text.InputType
import android.view.inputmethod.EditorInfo

object InputTypeUtils {

    fun isPassword(editorInfo: EditorInfo?): Boolean {
        if (editorInfo == null) return false
        val inputType = editorInfo.inputType
        val variation = inputType and InputType.TYPE_MASK_VARIATION
        val isTextPassword = variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD ||
                variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        val isNumberPassword = variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD
        return isTextPassword || isNumberPassword
    }

    fun isNumeric(editorInfo: EditorInfo?): Boolean {
        if (editorInfo == null) return false
        val inputClass = editorInfo.inputType and InputType.TYPE_MASK_CLASS
        return inputClass == InputType.TYPE_CLASS_NUMBER ||
                inputClass == InputType.TYPE_CLASS_PHONE ||
                inputClass == InputType.TYPE_CLASS_DATETIME
    }

    fun isEmail(editorInfo: EditorInfo?): Boolean {
        if (editorInfo == null) return false
        val variation = editorInfo.inputType and InputType.TYPE_MASK_VARIATION
        return variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS ||
                variation == InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
    }

    fun isUri(editorInfo: EditorInfo?): Boolean {
        if (editorInfo == null) return false
        val variation = editorInfo.inputType and InputType.TYPE_MASK_VARIATION
        return variation == InputType.TYPE_TEXT_VARIATION_URI
    }

    fun isMultiLine(editorInfo: EditorInfo?): Boolean {
        if (editorInfo == null) return false
        val inputType = editorInfo.inputType
        return (inputType and InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0 ||
                (inputType and InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE) != 0
    }
}
