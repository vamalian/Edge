package com.example.voice

import android.content.Context
import android.media.ToneGenerator
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceAssistantManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isTtsReady = false
    private val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)

    init {
        tts?.language = Locale.US
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            isTtsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        } else {
            Log.e("VoiceAssistant", "TTS initialization failed.")
        }
    }

    fun speak(text: String, enabled: Boolean = true) {
        if (!enabled) return
        if (isTtsReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "JARVIX_TTS")
        }
    }

    fun playBeep(tone: Int = ToneGenerator.TONE_PROP_BEEP, enabled: Boolean = true) {
        if (!enabled) return
        try {
            toneGen.startTone(tone, 150)
        } catch (e: Exception) {
            // Ignore audio exceptions
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            toneGen.release()
        } catch (e: Exception) {
            // ignore
        }
    }
}
