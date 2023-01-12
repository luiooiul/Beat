package com.luiooiul.beat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.luiooiul.beat.navigation.BeatNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeatNavGraph()
        }
    }
}