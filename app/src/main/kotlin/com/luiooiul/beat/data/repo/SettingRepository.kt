package com.luiooiul.beat.data.repo

import com.luiooiul.beat.data.model.Setting
import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun getSettingStream(): Flow<Setting>

    suspend fun addBeatCount()

    suspend fun selectBeatIcon(id: Int)

    suspend fun selectSoundEffect(id: Int)

    suspend fun changeFloatText(text: String)

    suspend fun enabledAutoClick(enabled: Boolean)

    suspend fun enabledBackgroundMusic(enabled: Boolean)
}