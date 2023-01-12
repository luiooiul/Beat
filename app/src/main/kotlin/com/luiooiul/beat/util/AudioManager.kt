package com.luiooiul.beat.util

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.core.net.toUri
import java.io.File

class AudioManager(private val context: Context) {

    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(10).build()
    private var mediaPlayer: MediaPlayer? = null

    private var soundEffectId = 0

    fun playSoundEffect() {
        soundPool.play(soundEffectId, 1f, 1f, 1, 0, 1f)
    }

    fun loadBackgroundMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, File(context.filesDir, "background_music").toUri())?.apply {
                isLooping = true
                start()
            }
        }
    }

    fun loadSoundEffectFromResId(resId: Int) {
        soundPool.unload(soundEffectId)
        soundEffectId = soundPool.load(context, resId, 1)
    }

    fun loadSoundEffectFromFile(fileName: String = "sound_effect") {
        val file = File(context.filesDir, fileName)
        soundPool.unload(soundEffectId)
        soundEffectId = soundPool.load(file.absolutePath, 1)
    }

    fun release() {
        soundPool.release()
        releaseMediaPlayer()
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}