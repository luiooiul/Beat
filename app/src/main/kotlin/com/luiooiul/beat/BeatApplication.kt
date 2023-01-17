package com.luiooiul.beat

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BeatApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .components {
            if (SDK_INT < 28) {
                add(GifDecoder.Factory())
            } else {
                add(ImageDecoderDecoder.Factory())
            }
        }
        .build()
}