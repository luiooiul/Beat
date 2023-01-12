package com.luiooiul.beat.data.repo

import com.luiooiul.beat.data.model.Setting
import com.luiooiul.beat.data.source.SettingDataSource
import kotlinx.coroutines.flow.Flow
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

    override suspend fun selectSoundEffect(id: Int) {
        settingLocalDataSource.selectSoundEffect(id)
    }

    override suspend fun changeFloatText(text: String) {
        settingLocalDataSource.changeFloatText(text)
    }

    override suspend fun enabledAutoClick(enabled: Boolean) {
        settingLocalDataSource.enabledAutoClick(enabled)
    }

    override suspend fun enabledBackgroundMusic(enabled: Boolean) {
        settingLocalDataSource.enabledBackgroundMusic(enabled)
    }
}