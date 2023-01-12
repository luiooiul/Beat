package com.luiooiul.beat.feature.home

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luiooiul.beat.R
import com.luiooiul.beat.ui.component.FloatText
import com.luiooiul.beat.ui.component.IconPressButton
import kotlinx.coroutines.delay

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    onSettingClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadSoundEffect()
        viewModel.loadBackgroundMusic()
    }

    HomeScreen(
        uiState = uiState,
        onBeat = viewModel::beat,
        onSettingClick = onSettingClick
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onBeat: () -> Unit,
    onSettingClick: () -> Unit
) {
    val currentConfiguration = LocalConfiguration.current

    val screenWidth = currentConfiguration.screenWidthDp.dp
    val screenHeight = currentConfiguration.screenHeightDp.dp

    val size = min(screenWidth, screenHeight) / 2

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 4.dp)
    ) {
        if (!uiState.isLoading) {
            val defaultFloatText = stringResource(R.string.float_text_default)
            val floatText by remember {
                mutableStateOf(uiState.floatText.ifEmpty { defaultFloatText })
            }
            // SettingButton
            IconPressButton(
                onClick = onSettingClick,
                painter = painterResource(R.drawable.ic_setting)
            )
            // BeatFloatText
            FloatText(
                text = floatText,
                times = uiState.beatCount,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(size.unaryMinus() / 10, size.unaryMinus() * 3 / 5),
                style = TextStyle(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )
            // BeatButton
            IconPressButton(
                painter = painterResource(uiState.beatIcon),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(size),
                enabled = !uiState.autoClickEnabled,
                interactionSource = interactionSource,
                onClick = onBeat
            )
            // BeatCountText
            BasicText(
                text = stringResource(R.string.beat_count_text, uiState.beatCount),
                modifier = Modifier.align(Alignment.BottomCenter),
                style = TextStyle(color = Color.DarkGray),
                maxLines = 1
            )
            // AutoClick
            LaunchedEffect(Unit) {
                val press = PressInteraction.Press(Offset.Zero)
                while (uiState.autoClickEnabled) {
                    interactionSource.emit(press)
                    delay(250)
                    interactionSource.emit(PressInteraction.Release(press))
                    onBeat()
                    delay(1000)
                }
            }
        }
    }
}