package com.luiooiul.beat.data.repo

import com.luiooiul.beat.data.model.Setting
import com.luiooiul.beat.data.source.SettingDataSource
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class DefaultSettingRepository @Inject constructor(
    private val settingLocalDataSource: SettingDataSource
) : SettingRepository {

    override fun getSettingStream(): Flow<Setting> {
        return settingLocalDataSource.getSettingStream()
    }

    override suspend fun addBeatCount() {
        settingLocalDataSource.addBeatCount()
    }

    override suspend fun selectBeatIcon(id: Int) {
        settingLocalDataSource.selectBeatIcon(id)
    }

    override suspend fun saveCustomBeatIcon(filesDir: File, inputStream: InputStream) {
        settingLocalDataSource.saveCustomBeatIcon(filesDir,inputStream)
    }

    override suspend fun selectSoundEffect(id: Int) {
        settingLocalDataSource.selectSoundEffect(id)
    }

    override suspend fun saveCustomSoundEffect(filesDir: File, inputStream: InputStream) {
        settingLocalDataSource.saveCustomSoundEffect(filesDir, inputStream)
    }

    override suspend fun modifyFloatText(text: String) {
        settingLocalDataSource.modifyFloatText(text)
    }

    override suspend fun enabledAutoClick(isEnabled: Boolean) {
        settingLocalDataSource.enabledAutoClick(isEnabled)
    }

    override suspend fun enabledBackgroundMusic(isEnabled: Boolean) {
        settingLocalDataSource.enabledBackgroundMusic(isEnabled)
    }

    override suspend fun saveCustomBackgroundMusic(filesDir: File, inputStream: InputStream) {
        settingLocalDataSource.saveCustomBackgroundMusic(filesDir, inputStream)
    }
}