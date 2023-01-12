package com.luiooiul.beat.data.store

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class SettingPrefsSerializer @Inject constructor() : Serializer<SettingPreferences> {

    override val defaultValue: SettingPreferences = SettingPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingPreferences {
        try {
            return SettingPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SettingPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}