package com.luiooiul.beat.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luiooiul.beat.data.repo.SettingRepository
import com.luiooiul.beat.util.AudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val beatCount: Int = 0,
    val beatIcon: Int = 0,
    val floatText: String = "",
    val autoClickEnabled: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val audioManager: AudioManager
) : ViewModel() {

    private val _setting = settingRepository.getSettingStream()

    private val _beatSoundEffect = _setting.map { it.beatSoundEffect }
    private val _beatBackgroundMusicEnabled = _setting.map { it.beatBackgroundMusicEnabled }

    val uiState = _setting.map { setting ->
        HomeUiState(
            beatCount = setting.beatCount,
            beatIcon = setting.beatIcon,
            floatText = setting.beatFloatText,
            autoClickEnabled = setting.beatAutoClickEnabled,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState(isLoading = true)
    )

    suspend fun loadSoundEffect() {
        when (val beatSoundEffect = _beatSoundEffect.first()) {
            1 -> audioManager.loadSoundEffectFromFile()
            else -> audioManager.loadSoundEffectFromResId(beatSoundEffect)
        }
    }

    suspend fun loadBackgroundMusic() {
        if (_beatBackgroundMusicEnabled.first()) {
            audioManager.loadBackgroundMusic()
        } else {
            audioManager.releaseMediaPlayer()
        }
    }

    fun beat() = viewModelScope.launch {
        settingRepository.addBeatCount()
        audioManager.playSoundEffect()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}