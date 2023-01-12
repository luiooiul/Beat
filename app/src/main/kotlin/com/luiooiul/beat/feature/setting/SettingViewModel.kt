package com.luiooiul.beat.feature.setting

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luiooiul.beat.data.repo.SettingRepository
import com.luiooiul.beat.util.AudioManager
import com.luiooiul.beat.domain.SaveFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val isLoading: Boolean = false,
    val beatIcon: Int = 0,
    val beatSoundEffect: Int = 0,
    val floatText: String = "",
    val autoClickEnabled: Boolean = false,
    val backgroundMusicEnabled: Boolean = false
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val saveFileUseCase: SaveFileUseCase,
    private val audioManager: AudioManager
) : ViewModel() {

    val uiState = settingRepository.getSettingStream().map { setting ->
        SettingUiState(
            beatIcon = setting.beatIcon,
            beatSoundEffect = setting.beatSoundEffect,
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

    fun pickedBeatSoundEffect(uri: Uri) = viewModelScope.launch {
        saveFileUseCase(uri, "sound_effect")
    }

    fun selectBeatSoundEffect(id: Int) = viewModelScope.launch {
        settingRepository.selectSoundEffect(id)
    }

    fun changeFloatText(text: String) = viewModelScope.launch {
        settingRepository.changeFloatText(text)
    }

    fun enabledAutoMode(enabled: Boolean) = viewModelScope.launch {
        settingRepository.enabledAutoClick(enabled)
    }

    fun pickedBackgroundMusic(uri: Uri) = viewModelScope.launch {
        saveFileUseCase(uri, "background_music")
    }

    fun enabledBackgroundMusic(enabled: Boolean) = viewModelScope.launch {
        settingRepository.enabledBackgroundMusic(enabled)

        if (enabled) {
            audioManager.loadBackgroundMusic()
        } else {
            audioManager.releaseMediaPlayer()
        }
    }
}