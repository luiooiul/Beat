package com.luiooiul.beat.feature.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luiooiul.beat.data.repo.SettingRepository
import com.luiooiul.beat.util.AudioManager
import com.luiooiul.beat.util.BACKGROUND_MUSIC_FILE
import com.luiooiul.beat.util.CUSTOM_FILE_ID
import com.luiooiul.beat.util.SOUND_EFFECT_FILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject

data class SettingUiState(
    val isLoading: Boolean = false,
    val beatIconId: Int = 0,
    val beatSoundEffectId: Int = 0,
    val autoClickEnabled: Boolean = false,
    val backgroundMusicEnabled: Boolean = false
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val audioManager: AudioManager,
    private val settingRepository: SettingRepository
) : ViewModel() {

    var floatText by mutableStateOf("")
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    private val saveFloatText = snapshotFlow { floatText }
        .mapLatest { settingRepository.modifyFloatText(it) }

    private val _setting = settingRepository.getSettingStream()

    private val _beatFloatText = _setting.map { it.beatFloatText }

    val uiState = _setting.map { setting ->
        SettingUiState(
            beatIconId = setting.beatIconId,
            beatSoundEffectId = setting.beatSoundEffectId,
            autoClickEnabled = setting.beatAutoClickEnabled,
            backgroundMusicEnabled = setting.beatBackgroundMusicEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            // Init float text from setting
            floatText = _beatFloatText.first()
            // Start Save Flow
            saveFloatText.collect()
        }
    }

    fun updateFloatText(text: String) {
        if (text.length <= 10) {
            floatText = text
        }
    }

    fun selectBeatIcon(id: Int) = viewModelScope.launch {
        settingRepository.selectBeatIcon(id)
    }

    fun selectBeatSoundEffect(filesDir: File, id: Int) = viewModelScope.launch {
        settingRepository.selectSoundEffect(id)
        // Preview
        audioManager.unloadSound()
        if (id == CUSTOM_FILE_ID) {
            val file = File(filesDir, SOUND_EFFECT_FILE)
            audioManager.loadSoundByFile(file)
        } else {
            audioManager.loadSoundByResId(id)
        }
        audioManager.playSound()
    }

    fun selectCustomSoundEffect(filesDir: File, inputStream: InputStream) = viewModelScope.launch {
        settingRepository.saveCustomSoundEffect(filesDir, inputStream)
        selectBeatSoundEffect(filesDir, CUSTOM_FILE_ID)
    }

    fun enabledAutoClick(enabled: Boolean) = viewModelScope.launch {
        settingRepository.enabledAutoClick(enabled)
    }

    fun enabledBackgroundMusic(filesDir: File, isEnabled: Boolean) = viewModelScope.launch {
        settingRepository.enabledBackgroundMusic(isEnabled)
        // Play
        if (isEnabled) {
            val file = File(filesDir, BACKGROUND_MUSIC_FILE)
            audioManager.playMediaByFile(file)
        } else {
            audioManager.releaseMediaPlayer()
        }
    }

    fun selectCustomBackgroundMusic(filesDir: File, inputStream: InputStream) = viewModelScope.launch {
        settingRepository.saveCustomBackgroundMusic(filesDir, inputStream)
        enabledBackgroundMusic(filesDir, true)
    }
}