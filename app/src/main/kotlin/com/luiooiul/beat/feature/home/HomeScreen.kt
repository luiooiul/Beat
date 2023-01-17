package com.luiooiul.beat.feature.home

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luiooiul.beat.R
import com.luiooiul.beat.ui.component.AnimateIconPressButton
import com.luiooiul.beat.ui.component.FloatText
import com.luiooiul.beat.ui.component.FloatTextAnim
import com.luiooiul.beat.ui.component.IconPressButton
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.math.min

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    onSettingClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentContext = LocalContext.current

    val filesDir = remember { currentContext.filesDir }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        viewModel.loadSoundEffect(filesDir)
        viewModel.playBackgroundMusic(filesDir)
        viewModel.startAutoClick(interactionSource)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { viewModel.floatTextAnimList.lastOrNull() }
            .filterNotNull()
            .collect { anim ->
                launch {
                    anim.start()
                    viewModel.finishFloatTextAnim(anim)
                }
            }
    }

    HomeScreen(
        uiState = uiState,
        interactionSource = interactionSource,
        animateList = viewModel.floatTextAnimList,
        onBeat = viewModel::beat,
        onSettingClick = onSettingClick
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    interactionSource: MutableInteractionSource,
    animateList: List<FloatTextAnim>,
    onBeat: () -> Unit,
    onSettingClick: () -> Unit
) {
    val currentConfiguration = LocalConfiguration.current

    val screenWidth = currentConfiguration.screenWidthDp
    val screenHeight = currentConfiguration.screenHeightDp

    val beatButtonSize = remember {
        min(screenWidth, screenHeight).div(2).dp
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 8.dp)
    ) {
        if (!uiState.isLoading) {
            // SettingButton
            IconPressButton(
                painter = painterResource(R.drawable.ic_setting),
                modifier = Modifier.size(24.dp),
                onClick = onSettingClick
            )
            // BeatFloatText
            if (uiState.floatText.isNotEmpty()) {
                FloatText(
                    text = uiState.floatText,
                    animateList = animateList,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(beatButtonSize.unaryMinus() / 10, beatButtonSize.unaryMinus() * 3 / 5),
                    style = TextStyle(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            }
            // BeatButton
            AnimateIconPressButton(
                painter = painterResource(uiState.beatIconId),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(beatButtonSize),
                enabled = !uiState.autoClickEnabled,
                interactionSource = interactionSource,
                onClick = onBeat
            )
            // BeatCountText
            BasicText(
                text = stringResource(R.string.beat_count_text, uiState.beatCount),
                modifier = Modifier.align(Alignment.BottomCenter),
                style = TextStyle(color = Color.DarkGray),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}