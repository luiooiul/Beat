package com.luiooiul.beat.feature.home

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luiooiul.beat.data.repo.SettingRepository
import com.luiooiul.beat.ui.component.FloatTextAnim
import com.luiooiul.beat.util.AudioManager
import com.luiooiul.beat.util.BACKGROUND_MUSIC_FILE
import com.luiooiul.beat.util.CUSTOM_FILE_ID
import com.luiooiul.beat.util.SOUND_EFFECT_FILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val _beatAutoClickEnabled = _setting.map { it.beatAutoClickEnabled }
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

    private val _floatTextAnimList = mutableStateListOf<FloatTextAnim>()
    val floatTextAnimList: List<FloatTextAnim> = _floatTextAnimList

    fun beat() = viewModelScope.launch {
        audioManager.playSound()
        settingRepository.addBeatCount()
        _floatTextAnimList.add(FloatTextAnim())
    }

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

    suspend fun startAutoClick(interactionSource: MutableInteractionSource) {
        val isEnabled = _beatAutoClickEnabled.first()
        val pressInteraction = PressInteraction.Press(Offset.Zero)
        val releaseInteraction = PressInteraction.Release(pressInteraction)
        while (isEnabled) {
            interactionSource.emit(pressInteraction)
            delay(250)
            interactionSource.emit(releaseInteraction)
            beat()
            delay(1000)
        }
    }

    fun finishFloatTextAnim(anim: FloatTextAnim) {
        _floatTextAnimList.remove(anim)
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.releaseSoundPool()
        audioManager.releaseMediaPlayer()
    }
}