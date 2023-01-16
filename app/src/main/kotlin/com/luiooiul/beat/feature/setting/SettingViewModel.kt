package com.luiooiul.beat.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luiooiul.beat.data.repo.SettingRepository
import com.luiooiul.beat.util.AudioManager
import com.luiooiul.beat.util.BACKGROUND_MUSIC_FILE
import com.luiooiul.beat.util.CUSTOM_FILE_ID
import com.luiooiul.beat.util.SOUND_EFFECT_FILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject

data class SettingUiState(
    val isLoading: Boolean = false,
    val beatIconId: Int = 0,
    val beatSoundEffectId: Int = 0,
    val floatText: String = "",
    val autoClickEnabled: Boolean = false,
    val backgroundMusicEnabled: Boolean = false
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val audioManager: AudioManager,
    private val settingRepository: SettingRepository
) : ViewModel() {

    val uiState = settingRepository.getSettingStream().map { setting ->
        SettingUiState(
            beatIconId = setting.beatIconId,
            beatSoundEffectId = setting.beatSoundEffectId,
            floatText = setting.beatFloatText,
            autoClickEnabled = setting.beatAutoClickEnabled,
            backgroundMusicEnabled = setting.beatBackgroundMusicEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingUiState(isLoading = true)
    )

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

    fun saveCustomEffectSound(filesDir: File, inputStream: InputStream) = viewModelScope.launch {
        settingRepository.saveCustomSoundEffect(filesDir, inputStream)
    }

    fun modifyFloatText(text: String) = viewModelScope.launch {
        settingRepository.modifyFloatText(text)
    }

    fun enabledAutoMode(enabled: Boolean) = viewModelScope.launch {
        settingRepository.enabledAutoClick(enabled)
    }

    fun enabledBackgroundMusic(filesDir: File, isEnabled: Boolean) = viewModelScope.launch {
        settingRepository.enabledBackgroundMusic(isEnabled)

        if (isEnabled) {
            val file = File(filesDir, BACKGROUND_MUSIC_FILE)
            audioManager.playMediaByFile(file)
        } else {
            audioManager.releaseMediaPlayer()
        }
    }

    fun saveCustomBackgroundMusic(filesDir: File, inputStream: InputStream) = viewModelScope.launch {
        settingRepository.saveCustomBackgroundMusic(filesDir, inputStream)
    }
}