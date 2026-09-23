package com.example

import android.view.inputmethod.EditorInfo
import com.example.ime.KeyboardInputMethodService
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.junit.Assert.assertNotNull

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ServiceCrashTest {

    @Test
    fun testKeyboardServiceLifecycleAndInputView() {
        val controller = Robolectric.buildService(KeyboardInputMethodService::class.java)
        val service = controller.create().get()
        assertNotNull(service)

        val inputView = service.onCreateInputView()
        assertNotNull(inputView)

        val editorInfo = EditorInfo().apply {
            inputType = android.text.InputType.TYPE_CLASS_TEXT
        }
        service.onStartInputView(editorInfo, false)
        service.onFinishInputView(false)
        controller.destroy()
    }
}
