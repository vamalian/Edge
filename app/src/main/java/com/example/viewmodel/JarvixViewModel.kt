package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiRepository
import com.example.data.ChatMessageEntity
import com.example.data.JarvixDatabase
import com.example.data.SystemLogEntity
import com.example.device.DeviceControlManager
import com.example.voice.VoiceAssistantManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class JarvixCoreState {
    IDLE, LISTENING, PROCESSING, SPEAKING, SLEEP
}

enum class StartupStage {
    BOOTING, INITIALIZING, SYSTEM_CHECK, AI_READY, VOICE_READY, ONLINE
}

class JarvixViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = JarvixDatabase.getDatabase(application).jarvixDao()
    private val geminiRepo = GeminiRepository()
    val deviceManager = DeviceControlManager(application)
    val voiceManager = VoiceAssistantManager(application)

    val chatMessages: StateFlow<List<ChatMessageEntity>> = dao.getAllMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val systemLogs: StateFlow<List<SystemLogEntity>> = dao.getRecentLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _startupStage = MutableStateFlow(StartupStage.BOOTING)
    val startupStage: StateFlow<StartupStage> = _startupStage.asStateFlow()

    private val _coreState = MutableStateFlow(JarvixCoreState.IDLE)
    val coreState: StateFlow<JarvixCoreState> = _coreState.asStateFlow()

    private val _isOwnerAuthenticated = MutableStateFlow(false)
    val isOwnerAuthenticated: StateFlow<Boolean> = _isOwnerAuthenticated.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _batteryInfo = MutableStateFlow(deviceManager.getBatteryLevel())
    val batteryInfo: StateFlow<String> = _batteryInfo.asStateFlow()

    private val _networkInfo = MutableStateFlow(deviceManager.getNetworkStatus())
    val networkInfo: StateFlow<String> = _networkInfo.asStateFlow()

    init {
        runStartupSequence()
        logSystem("JARVIX Core", "System boot initialized.")
    }

    private fun runStartupSequence() {
        viewModelScope.launch {
            _startupStage.value = StartupStage.INITIALIZING
            kotlinx.coroutines.delay(600)
            _startupStage.value = StartupStage.SYSTEM_CHECK
            kotlinx.coroutines.delay(600)
            _startupStage.value = StartupStage.AI_READY
            kotlinx.coroutines.delay(600)
            _startupStage.value = StartupStage.VOICE_READY
            kotlinx.coroutines.delay(600)
            _startupStage.value = StartupStage.ONLINE
            voiceManager.speak("JARVIX online. Systems operational.", _soundEnabled.value)
        }
    }

    fun logSystem(category: String, message: String) {
        viewModelScope.launch {
            dao.insertLog(SystemLogEntity(category = category, message = message))
        }
    }

    fun toggleSound() {
        _soundEnabled.value = !_soundEnabled.value
    }

    fun setCoreState(state: JarvixCoreState) {
        _coreState.value = state
        if (state == JarvixCoreState.LISTENING) {
            voiceManager.playBeep(enabled = _soundEnabled.value)
        }
    }

    fun sendPrompt(userText: String) {
        if (userText.isBlank()) return
        val trimmed = userText.trim()

        // Check for specific system voice commands
        when {
            trimmed.contains("go to sleep", ignoreCase = true) || trimmed.contains("sleep", ignoreCase = true) -> {
                setCoreState(JarvixCoreState.SLEEP)
                addMessageAndSpeak("user", trimmed)
                addMessageAndSpeak("jarvix", "Entering sleep mode. Tap the Core to wake me.")
                return
            }
            trimmed.contains("wake up", ignoreCase = true) || trimmed.contains("hey jarvix", ignoreCase = true) -> {
                setCoreState(JarvixCoreState.IDLE)
                addMessageAndSpeak("user", trimmed)
                addMessageAndSpeak("jarvix", "Systems online. I'm listening.")
                return
            }
            trimmed.contains("flashlight", ignoreCase = true) -> {
                val isOn = deviceManager.toggleFlashlight()
                addMessageAndSpeak("user", trimmed)
                addMessageAndSpeak("jarvix", if (isOn) "Flashlight activated." else "Flashlight deactivated.")
                return
            }
            trimmed.contains("settings", ignoreCase = true) -> {
                deviceManager.openSettings()
                addMessageAndSpeak("user", trimmed)
                addMessageAndSpeak("jarvix", "Launching Android settings.")
                return
            }
        }

        addMessageAndSpeak("user", trimmed)
        setCoreState(JarvixCoreState.PROCESSING)
        logSystem("AI Query", "Processing query: $trimmed")

        viewModelScope.launch {
            val history = chatMessages.value.takeLast(6).map { Pair(it.role, it.text) }
            val reply = geminiRepo.askJarvix(trimmed, history)
            setCoreState(JarvixCoreState.SPEAKING)
            addMessageAndSpeak("jarvix", reply)
            voiceManager.speak(reply, _soundEnabled.value)
            setCoreState(JarvixCoreState.IDLE)
            logSystem("AI Response", "Replied successfully.")
        }
    }

    private fun addMessageAndSpeak(role: String, text: String) {
        viewModelScope.launch {
            dao.insertMessage(ChatMessageEntity(role = role, text = text))
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            dao.clearChat()
            logSystem("Memory", "Chat history purged.")
        }
    }

    fun verifyMasterKey(key: String): Boolean {
        val authorized = key.trim() == "4000"
        _isOwnerAuthenticated.value = authorized
        if (authorized) {
            logSystem("Security", "Master Key verified successfully.")
            voiceManager.playBeep(enabled = _soundEnabled.value)
        } else {
            logSystem("Security", "Access denied: Invalid Master Key.")
        }
        return authorized
    }

    fun logoutOwner() {
        _isOwnerAuthenticated.value = false
        logSystem("Security", "Owner session terminated.")
    }
}
