package com.luiooiul.beat.util

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.core.net.toUri
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AudioManager(private val context: Context) {

    companion object {
        private const val STATUS_SUCCESS = 0
        private const val SOUND_UNLOAD_ID = 0
    }

    private var soundPool: SoundPool? = null
    private var soundCacheId = SOUND_UNLOAD_ID

    private var mediaPlayer: MediaPlayer? = null

    init {
        soundPool = SoundPool.Builder().setMaxStreams(10).build()
    }

    fun playSound() {
        soundPool?.play(soundCacheId, 1f, 1f, 0, 0, 1f)
    }

    fun unloadSound() {
        if (soundPool != null) {
            soundPool!!.unload(soundCacheId)
            soundCacheId = SOUND_UNLOAD_ID
        }
    }

    suspend fun loadSoundByResId(resId: Int, priority: Int = 1) {
        if (soundPool != null && soundCacheId == SOUND_UNLOAD_ID) {
            soundPool!!.load(context, resId, priority)
            soundCacheId = getLoadCompletedSoundId()
        }
    }

    suspend fun loadSoundByFile(file: File, priority: Int = 1) {
        if (soundPool != null && soundCacheId == SOUND_UNLOAD_ID) {
            soundPool!!.load(file.absolutePath, priority)
            soundCacheId = getLoadCompletedSoundId()
        }
    }

    private suspend fun getLoadCompletedSoundId(): Int = suspendCoroutine {
        soundPool!!.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == STATUS_SUCCESS) {
                it.resume(sampleId)
            } else {
                it.resume(SOUND_UNLOAD_ID)
            }
        }
    }

    fun playMediaByFile(file: File) {
        if (mediaPlayer == null) {
            mediaPlayer = runCatching { MediaPlayer.create(context, file.toUri()) }.getOrNull()
        }
        mediaPlayer?.start()
        mediaPlayer?.isLooping = true
    }

    fun releaseSoundPool() {
        soundPool?.release()
        soundPool = null
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}