package com.luiooiul.beat.data.model

data class Setting(
    val beatCount: Int,
    val beatIconId: Int,
    val beatSoundEffectId: Int,
    val beatFloatText: String,
    val beatAutoClickEnabled: Boolean,
    val beatBackgroundMusicEnabled: Boolean
)