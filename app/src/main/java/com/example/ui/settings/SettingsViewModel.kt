package com.example.ui.settings

import android.app.Application
import android.content.Context
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.KeyboardDataStore
import com.example.data.KeyboardPreferences
import com.example.model.KeyboardHeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = KeyboardDataStore.getInstance(application)

    val preferences: StateFlow<KeyboardPreferences> = dataStore.preferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = KeyboardPreferences()
        )

    private val _isKeyboardEnabled = MutableStateFlow(false)
    val isKeyboardEnabled: StateFlow<Boolean> = _isKeyboardEnabled.asStateFlow()

    private val _isKeyboardDefault = MutableStateFlow(false)
    val isKeyboardDefault: StateFlow<Boolean> = _isKeyboardDefault.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            checkKeyboardStatus()
        }
    }

    fun checkKeyboardStatus() {
        val app = getApplication<Application>()
        val imm = app.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val packageName = app.packageName

        // Check if keyboard is enabled
        val enabledList = imm?.enabledInputMethodList ?: emptyList()
        val isEnabled = enabledList.any { it.packageName == packageName }
        _isKeyboardEnabled.value = isEnabled

        // Check if keyboard is default
        val defaultIme = Settings.Secure.getString(
            app.contentResolver,
            Settings.Secure.DEFAULT_INPUT_METHOD
        ) ?: ""
        val isDefault = defaultIme.contains(packageName)
        _isKeyboardDefault.value = isDefault
    }

    fun setTheme(themeId: String) {
        viewModelScope.launch {
            dataStore.setTheme(themeId)
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.setSoundEnabled(enabled)
        }
    }

    fun setHapticEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.setHapticEnabled(enabled)
        }
    }

    fun setPopupEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.setPopupEnabled(enabled)
        }
    }

    fun setHeight(height: KeyboardHeight) {
        viewModelScope.launch {
            dataStore.setHeight(height)
        }
    }
}
