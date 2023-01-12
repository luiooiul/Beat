package com.luiooiul.beat.data.source.local

import androidx.datastore.core.DataStore
import com.luiooiul.beat.R
import com.luiooiul.beat.data.model.Setting
import com.luiooiul.beat.data.source.SettingDataSource
import com.luiooiul.beat.data.store.SettingPreferences
import com.luiooiul.beat.data.store.copy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingLocalDataSource @Inject constructor(
    private val settingPreferences: DataStore<SettingPreferences>
) : SettingDataSource {

    override fun getSettingStream(): Flow<Setting> {
        return settingPreferences.data.map { setting ->
            Setting(
                beatCount = setting.beatCount,
                beatIcon = setting.beatIcon.takeUnless { it == 0 } ?: R.drawable.ic_temple_block_1,
                beatSoundEffect = setting.beatSoundEffect.takeUnless { it == 0 } ?: R.raw.sound_effect_1,
                beatFloatText = setting.beatFloatText,
                beatAutoClickEnabled = setting.beatAutoClickEnabled,
                beatBackgroundMusicEnabled = setting.beatBackgroundMusicEnabled
            )
        }
    }

    override suspend fun addBeatCount() {
        try {
            settingPreferences.updateData {
                it.copy {
                    beatCount += 1
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override suspend fun selectBeatIcon(id: Int) {
        try {
            settingPreferences.updateData {
                it.copy {
                    beatIcon = id
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override suspend fun selectSoundEffect(id: Int) {
        try {
            settingPreferences.updateData {
                it.copy {
                    beatSoundEffect = id
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override suspend fun changeFloatText(text: String) {
        try {
            settingPreferences.updateData {
                it.copy {
                    beatFloatText = text
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override suspend fun enabledAutoClick(enabled: Boolean) {
        try {
            settingPreferences.updateData {
                it.copy {
                    beatAutoClickEnabled = enabled
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override suspend fun enabledBackgroundMusic(enabled: Boolean) {
        try {
            settingPreferences.updateData {
                it.copy {
                    beatBackgroundMusicEnabled = enabled
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}