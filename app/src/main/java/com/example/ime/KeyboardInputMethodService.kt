package com.example.ime

import android.content.Context
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.text.InputType
import android.text.TextUtils
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.MainActivity
import com.example.data.KeyboardDataStore
import com.example.data.KeyboardPreferences
import com.example.model.KeyboardMode
import com.example.model.KeyboardThemeModel
import com.example.model.ShiftState
import com.example.ui.keyboard.KeyboardScreen
import com.example.util.FeedbackManager
import com.example.util.InputTypeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KeyboardInputMethodService : InputMethodService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var dataStore: KeyboardDataStore
    private lateinit var feedbackManager: FeedbackManager
    private lateinit var lifecycleOwner: KeyboardLifecycleOwner
    private val keyboardInputConnection = KeyboardInputConnection(null)

    private val _currentPreferences = MutableStateFlow(KeyboardPreferences())
    private val currentPreferences = _currentPreferences.asStateFlow()

    private val _currentSuggestions = MutableStateFlow<List<String>>(emptyList())
    private val currentSuggestions = _currentSuggestions.asStateFlow()

    private var currentEditorInfo: EditorInfo? by mutableStateOf(null)
    private var currentMode by mutableStateOf(KeyboardMode.LETTERS)
    private var shiftState by mutableStateOf(ShiftState.OFF)

    private var composeView: ComposeView? = null

    override fun onCreate() {
        super.onCreate()
        lifecycleOwner = KeyboardLifecycleOwner()
        lifecycleOwner.onCreate()
        lifecycleOwner.onStart()
        lifecycleOwner.onResume()

        dataStore = KeyboardDataStore.getInstance(this)
        feedbackManager = FeedbackManager(this)

        serviceScope.launch {
            dataStore.preferencesFlow.collect { prefs ->
                _currentPreferences.value = prefs
            }
        }
    }

    override fun onCreateInputView(): View {
        composeView?.disposeComposition()

        val view = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        window.window?.decorView?.let { decorView ->
            lifecycleOwner.attachToView(decorView)
        }
        lifecycleOwner.attachToView(view)

        view.setContent {
            val prefs by currentPreferences.collectAsState()
            val suggestions by currentSuggestions.collectAsState()
            val actionType = EditorInfoHandler.getActionType(currentEditorInfo)
            val actionIcon = EditorInfoHandler.getActionIcon(actionType)
            val isEmail = InputTypeUtils.isEmail(currentEditorInfo)

            KeyboardScreen(
                mode = currentMode,
                shiftState = shiftState,
                theme = prefs.currentTheme,
                height = prefs.height,
                showPopup = prefs.popupEnabled,
                actionType = actionType,
                actionIcon = actionIcon,
                isEmailField = isEmail,
                recentEmojis = prefs.recentEmojis,
                suggestions = suggestions,
                onKeyClick = { text ->
                    handleCharacter(text, prefs)
                },
                onBackspace = {
                    handleBackspace(prefs)
                },
                onBackspaceContinuous = {
                    handleBackspace(prefs)
                },
                onShiftClick = {
                    handleShiftToggle(prefs)
                },
                onShiftDoubleClick = {
                    handleShiftDoubleClick(prefs)
                },
                onSpaceClick = {
                    handleSpace(prefs)
                },
                onActionClick = {
                    handleAction(prefs)
                },
                onSuggestionClick = { suggestion ->
                    handleSuggestionClick(suggestion)
                },
                onSwitchMode = { newMode ->
                    currentMode = newMode
                    if (prefs.hapticEnabled) feedbackManager.vibrateKey()
                },
                onSwitchIme = {
                    switchIme()
                },
                onDismissKeyboard = {
                    requestHideSelf(0)
                },
                onOpenSettings = {
                    openSettingsApp()
                },
                onEmojiSelected = { emoji ->
                    handleEmoji(emoji, prefs)
                },
                onNextTheme = {
                    cycleNextTheme(prefs)
                }
            )
        }

        composeView = view
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        currentEditorInfo = info
        keyboardInputConnection.connection = currentInputConnection

        // Auto-switch mode depending on input type
        currentMode = if (InputTypeUtils.isNumeric(info)) {
            KeyboardMode.NUMBERS
        } else {
            KeyboardMode.LETTERS
        }

        // Reset shift state unless caps lock is on
        if (shiftState != ShiftState.CAPS_LOCK) {
            checkAutoCaps()
        }
        updateSuggestions()
    }

    override fun onUpdateSelection(
        oldSelStart: Int, oldSelEnd: Int,
        newSelStart: Int, newSelEnd: Int,
        candidatesStart: Int, candidatesEnd: Int
    ) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)
        updateSuggestions()
        checkAutoCaps()
    }

    private fun updateSuggestions() {
        val conn = currentInputConnection ?: return
        val beforeCursor = conn.getTextBeforeCursor(50, 0)?.toString() ?: ""
        if (beforeCursor.isEmpty() || beforeCursor.last().isWhitespace()) {
            _currentSuggestions.value = emptyList()
            return
        }
        val lastWord = beforeCursor.split(Regex("\\s+")).lastOrNull() ?: ""
        if (lastWord.isNotEmpty() && lastWord.all { it.isLetter() }) {
            _currentSuggestions.value = SuggestionManager.getSuggestions(lastWord)
        } else {
            _currentSuggestions.value = emptyList()
        }
    }

    private fun handleSuggestionClick(suggestion: String) {
        val conn = currentInputConnection ?: return
        val beforeCursor = conn.getTextBeforeCursor(50, 0)?.toString() ?: ""
        val lastWord = beforeCursor.split(Regex("\\s+")).lastOrNull() ?: ""
        if (lastWord.isNotEmpty()) {
            conn.deleteSurroundingText(lastWord.length, 0)
            conn.commitText(suggestion + " ", 1)
        }
        _currentSuggestions.value = emptyList()
    }

    private fun checkAutoCaps() {
        if (shiftState == ShiftState.CAPS_LOCK) return
        val conn = currentInputConnection ?: return
        val info = currentEditorInfo ?: return

        if ((info.inputType and InputType.TYPE_MASK_CLASS) != InputType.TYPE_CLASS_TEXT) {
            shiftState = ShiftState.OFF
            return
        }

        var reqModes = 0
        if ((info.inputType and InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS) != 0) {
            reqModes = TextUtils.CAP_MODE_CHARACTERS
        } else if ((info.inputType and InputType.TYPE_TEXT_FLAG_CAP_WORDS) != 0) {
            reqModes = TextUtils.CAP_MODE_WORDS
        } else if ((info.inputType and InputType.TYPE_TEXT_FLAG_CAP_SENTENCES) != 0) {
            reqModes = TextUtils.CAP_MODE_SENTENCES
        }

        if (reqModes == 0) {
            shiftState = ShiftState.OFF
            return
        }

        val capsMode = conn.getCursorCapsMode(reqModes)
        shiftState = if (capsMode != 0) ShiftState.ON else ShiftState.OFF
    }

    private fun handleCharacter(rawText: String, prefs: KeyboardPreferences) {
        if (prefs.soundEnabled) feedbackManager.playClickSound()
        if (prefs.hapticEnabled) feedbackManager.vibrateKey()

        // Decide the case HERE, from the authoritative shiftState field, instead of
        // trusting a case that Compose already baked into the tapped label. During
        // fast typing, UI recomposition can lag a frame behind touches, so a key's
        // onClick closure can still be holding the previous (wrong) case.
        val isLetter = rawText.length == 1 && rawText[0].isLetter()
        val textToCommit = if (isLetter && shiftState.isUppercase) rawText.uppercase() else rawText

        currentInputConnection?.commitText(textToCommit, 1)

        // Single-shot Shift: once used for one letter, turn it off immediately and
        // deterministically — don't wait on the remote app's cursor caps mode to
        // decide this for us.
        if (isLetter && shiftState == ShiftState.ON) {
            shiftState = ShiftState.OFF
        }

        updateSuggestions()
        checkAutoCaps()
    }

    private fun handleBackspace(prefs: KeyboardPreferences) {
        if (prefs.soundEnabled) feedbackManager.playClickSound()
        if (prefs.hapticEnabled) feedbackManager.vibrateKey()

        keyboardInputConnection.connection = currentInputConnection
        keyboardInputConnection.deleteBackward()
        updateSuggestions()
        checkAutoCaps()
    }

    private fun handleSpace(prefs: KeyboardPreferences) {
        if (prefs.soundEnabled) feedbackManager.playClickSound()
        if (prefs.hapticEnabled) feedbackManager.vibrateKey()

        currentInputConnection?.commitText(" ", 1)
        updateSuggestions()
        checkAutoCaps()
    }

    private fun handleShiftToggle(prefs: KeyboardPreferences) {
        if (prefs.hapticEnabled) feedbackManager.vibrateKey()
        shiftState = shiftState.toggle()
    }

    private fun handleShiftDoubleClick(prefs: KeyboardPreferences) {
        if (prefs.hapticEnabled) feedbackManager.vibrateLongPress()
        shiftState = ShiftState.CAPS_LOCK
    }

    private fun handleAction(prefs: KeyboardPreferences) {
        if (prefs.soundEnabled) feedbackManager.playClickSound()
        if (prefs.hapticEnabled) feedbackManager.vibrateKey()

        EditorInfoHandler.executeAction(currentInputConnection, currentEditorInfo)
    }

    private fun handleEmoji(emoji: String, prefs: KeyboardPreferences) {
        if (prefs.soundEnabled) feedbackManager.playClickSound()
        if (prefs.hapticEnabled) feedbackManager.vibrateKey()

        currentInputConnection?.commitText(emoji, 1)
        serviceScope.launch {
            dataStore.addRecentEmoji(emoji)
        }
    }

    private fun cycleNextTheme(prefs: KeyboardPreferences) {
        if (prefs.hapticEnabled) feedbackManager.vibrateKey()
        val allThemes = KeyboardThemeModel.allThemes
        val currentIndex = allThemes.indexOfFirst { it.id == prefs.themeId }
        val nextIndex = if (currentIndex >= 0) (currentIndex + 1) % allThemes.size else 0
        val nextTheme = allThemes[nextIndex]
        serviceScope.launch {
            dataStore.setTheme(nextTheme.id)
        }
    }

    private fun switchIme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!switchToNextInputMethod(false)) {
                showImePicker()
            }
        } else {
            showImePicker()
        }
    }

    private fun showImePicker() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showInputMethodPicker()
    }

    private fun openSettingsApp() {
        try {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        } catch (_: Exception) {
        }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        lifecycleOwner.onPause()
    }

    override fun onDestroy() {
        composeView?.disposeComposition()
        composeView = null
        lifecycleOwner.onDestroy()
        serviceScope.cancel()
        super.onDestroy()
    }
}