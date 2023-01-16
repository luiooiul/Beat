package com.luiooiul.beat.data.store

import androidx.datastore.core.Serializer
import com.luiooiul.beat.util.DEFAULT_BEAT_ICON_ID
import com.luiooiul.beat.util.DEFAULT_BEAT_SOUND_EFFECT_ID
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class SettingPrefsSerializer @Inject constructor() : Serializer<SettingPreferences> {

    override val defaultValue: SettingPreferences = SettingPreferences.newBuilder()
        .setBeatIconId(DEFAULT_BEAT_ICON_ID)
        .setBeatSoundEffectId(DEFAULT_BEAT_SOUND_EFFECT_ID)
        .build()

    override suspend fun readFrom(input: InputStream): SettingPreferences {
        return SettingPreferences.parseFrom(input)
    }

    override suspend fun writeTo(t: SettingPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}