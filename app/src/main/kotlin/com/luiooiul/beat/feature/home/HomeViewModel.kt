package com.luiooiul.beat.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luiooiul.beat.data.repo.SettingRepository
import com.luiooiul.beat.util.AudioManager
import com.luiooiul.beat.util.BACKGROUND_MUSIC_FILE
import com.luiooiul.beat.util.CUSTOM_FILE_ID
import com.luiooiul.beat.util.SOUND_EFFECT_FILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val beatCount: Int = 0,
    val beatIconId: Int = 0,
    val floatText: String = "",
    val autoClickEnabled: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val audioManager: AudioManager,
    private val settingRepository: SettingRepository
) : ViewModel() {

    private val _setting = settingRepository.getSettingStream()

    private val _beatSoundEffectId = _setting.map { it.beatSoundEffectId }
    private val _beatBackgroundMusicEnabled = _setting.map { it.beatBackgroundMusicEnabled }

    val uiState = _setting.map { setting ->
        HomeUiState(
            beatCount = setting.beatCount,
            beatIconId = setting.beatIconId,
            floatText = setting.beatFloatText,
            autoClickEnabled = setting.beatAutoClickEnabled,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState(isLoading = true)
    )

    fun loadSoundEffect(filesDir: File) = viewModelScope.launch {
        val id = _beatSoundEffectId.first()
        if (id == CUSTOM_FILE_ID) {
            val file = File(filesDir, SOUND_EFFECT_FILE)
            audioManager.loadSoundByFile(file)
        } else {
            audioManager.loadSoundByResId(id)
        }
    }

    fun playBackgroundMusic(filesDir: File) = viewModelScope.launch {
        val isEnabled = _beatBackgroundMusicEnabled.first()
        if (isEnabled) {
            val file = File(filesDir, BACKGROUND_MUSIC_FILE)
            audioManager.playMediaByFile(file)
        }
    }

    fun beat() = viewModelScope.launch {
        audioManager.playSound()
        settingRepository.addBeatCount()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.releaseSoundPool()
        audioManager.releaseMediaPlayer()
    }
}