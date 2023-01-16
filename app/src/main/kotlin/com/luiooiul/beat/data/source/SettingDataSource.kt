package com.luiooiul.beat.data.source

import com.luiooiul.beat.data.model.Setting
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

interface SettingDataSource {

    fun getSettingStream(): Flow<Setting>

    suspend fun addBeatCount()

    suspend fun selectBeatIcon(id: Int)

    suspend fun selectSoundEffect(id: Int)

    suspend fun saveCustomSoundEffect(filesDir: File, inputStream: InputStream)

    suspend fun modifyFloatText(text: String)

    suspend fun enabledAutoClick(isEnabled: Boolean)

    suspend fun enabledBackgroundMusic(isEnabled: Boolean)

    suspend fun saveCustomBackgroundMusic(filesDir: File, inputStream: InputStream)
}