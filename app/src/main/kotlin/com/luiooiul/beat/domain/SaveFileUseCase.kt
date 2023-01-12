package com.luiooiul.beat.domain

import android.content.Context
import android.net.Uri
import com.luiooiul.beat.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class SaveFileUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(uri: Uri, fileName: String) = withContext(ioDispatcher) {
        context.contentResolver.openInputStream(uri)?.use {
            File(context.filesDir, fileName).writeBytes(it.readBytes())
        }
    }
}