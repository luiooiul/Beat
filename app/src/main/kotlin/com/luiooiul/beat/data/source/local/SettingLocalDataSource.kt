package com.luiooiul.beat.data.source.local

import androidx.datastore.core.DataStore
import com.luiooiul.beat.data.model.Setting
import com.luiooiul.beat.data.source.SettingDataSource
import com.luiooiul.beat.data.store.SettingPreferences
import com.luiooiul.beat.data.store.copy
import com.luiooiul.beat.di.IoDispatcher
import com.luiooiul.beat.util.BACKGROUND_MUSIC_FILE
import com.luiooiul.beat.util.SOUND_EFFECT_FILE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class SettingLocalDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val settingPreferences: DataStore<SettingPreferences>
) : SettingDataSource {

    override fun getSettingStream(): Flow<Setting> = settingPreferences.data
        .map { setting ->
            Setting(
                beatCount = setting.beatCount,
                beatIconId = setting.beatIconId,
                beatSoundEffectId = setting.beatSoundEffectId,
                beatFloatText = setting.beatFloatText,
                beatAutoClickEnabled = setting.beatAutoClickEnabled,
                beatBackgroundMusicEnabled = setting.beatBackgroundMusicEnabled
            )
        }

    override suspend fun addBeatCount() {
        settingPreferences.updateData {
            it.copy {
                beatCount += 1
            }
        }
    }

    override suspend fun selectBeatIcon(id: Int) {
        settingPreferences.updateData {
            it.copy {
                beatIconId = id
            }
        }
    }

    override suspend fun selectSoundEffect(id: Int) {
        settingPreferences.updateData {
            it.copy {
                beatSoundEffectId = id
            }
        }
    }

    override suspend fun saveCustomSoundEffect(filesDir: File, inputStream: InputStream) {
        withContext(ioDispatcher) {
            File(filesDir, SOUND_EFFECT_FILE).writeBytes(inputStream.readBytes())
        }
    }

    override suspend fun modifyFloatText(text: String) {
        settingPreferences.updateData {
            it.copy {
                beatFloatText = text
            }
        }
    }

    override suspend fun enabledAutoClick(isEnabled: Boolean) {
        settingPreferences.updateData {
            it.copy {
                beatAutoClickEnabled = isEnabled
            }
        }
    }

    override suspend fun enabledBackgroundMusic(isEnabled: Boolean) {
        settingPreferences.updateData {
            it.copy {
                beatBackgroundMusicEnabled = isEnabled
            }
        }
    }

    override suspend fun saveCustomBackgroundMusic(filesDir: File, inputStream: InputStream) {
        withContext(ioDispatcher) {
            File(filesDir, BACKGROUND_MUSIC_FILE).writeBytes(inputStream.readBytes())
        }
    }
}