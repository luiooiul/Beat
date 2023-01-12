package com.luiooiul.beat.feature.setting

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luiooiul.beat.R
import com.luiooiul.beat.ui.component.IconPressButton
import com.luiooiul.beat.ui.component.TextPressButton
import com.luiooiul.beat.ui.component.TitlePanel
import com.luiooiul.beat.ui.theme.backgroundColor
import com.luiooiul.beat.ui.theme.smallRoundShape

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onBeatIconSelected = viewModel::selectBeatIcon,
        onBeatSoundEffectPicked = viewModel::pickedBeatSoundEffect,
        onBeatSoundEffectSelected = viewModel::selectBeatSoundEffect,
        onFloatTextChange = viewModel::changeFloatText,
        onAutoClickEnabled = viewModel::enabledAutoMode,
        onBackgroundMusicPicked = viewModel::pickedBackgroundMusic,
        onBackgroundMusicEnabled = viewModel::enabledBackgroundMusic
    )
}

@Composable
fun SettingScreen(
    uiState: SettingUiState,
    onBackClick: () -> Unit,
    onBeatIconSelected: (Int) -> Unit,
    onBeatSoundEffectPicked: (Uri) -> Unit,
    onBeatSoundEffectSelected: (Int) -> Unit,
    onFloatTextChange: String.() -> Unit,
    onAutoClickEnabled: (Boolean) -> Unit,
    onBackgroundMusicPicked: (Uri) -> Unit,
    onBackgroundMusicEnabled: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
    ) {
        if (!uiState.isLoading) {
            IconPressButton(
                painter = painterResource(R.drawable.ic_back),
                onClick = onBackClick
            )
            Column(
                modifier = Modifier
                    .padding(top = 36.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                FloatTextPanel(
                    text = uiState.floatText,
                    onFloatTextChange = onFloatTextChange
                )
                BeatIconPanel(
                    beatIcon = uiState.beatIcon,
                    beatIconList = listOf(
                        R.drawable.ic_temple_block_1,
                        R.drawable.ic_temple_block_2,
                        R.drawable.ic_star,
                        R.drawable.ic_love,
                        R.drawable.ic_coin
                    ),
                    onBeatIconSelected = onBeatIconSelected
                )
                BeatSoundEffectPanel(
                    beatSoundEffect = uiState.beatSoundEffect,
                    beatSoundEffectList = listOf(
                        R.raw.sound_effect_1,
                        R.raw.sound_effect_2,
                        R.raw.sound_effect_3,
                        R.raw.sound_effect_4,
                        R.raw.sound_effect_5,
                        R.raw.sound_effect_6,
                        R.raw.sound_effect_7,
                        R.raw.sound_effect_8,
                        R.raw.sound_effect_9
                    ),
                    onBeatSoundEffectPicked = onBeatSoundEffectPicked,
                    onBeatSoundEffectSelected = onBeatSoundEffectSelected
                )
                AutoClickPanel(
                    autoClickEnabled = uiState.autoClickEnabled,
                    onAutoClickEnabled = onAutoClickEnabled
                )
                BackgroundMusicPanel(
                    backgroundMusicEnabled = uiState.backgroundMusicEnabled,
                    onBackgroundMusicPicked = onBackgroundMusicPicked,
                    onBackgroundMusicEnabled = onBackgroundMusicEnabled
                )
            }
        }
    }
}

@Composable
fun FloatTextPanel(
    text: String,
    onFloatTextChange: String.() -> Unit,
    defaultFloatText: String = stringResource(R.string.float_text_default)
) {
    var floatText by remember {
        mutableStateOf(text.ifEmpty { defaultFloatText })
    }

    TitlePanel(title = stringResource(R.string.float_text_title)) {
        BasicTextField(
            value = floatText,
            onValueChange = {
                if (it.length <= 10) {
                    floatText = it
                    onFloatTextChange(it)
                }
            },
            modifier = Modifier
                .background(backgroundColor, smallRoundShape)
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
            cursorBrush = SolidColor(Color.White)
        )
    }
}

@Composable
fun BeatIconPanel(
    beatIcon: Int,
    beatIconList: List<Int>,
    onBeatIconSelected: (Int) -> Unit
) {
    TitlePanel(title = stringResource(R.string.beat_icon_title)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.height(64.dp),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(beatIconList) {
                IconPressButton(
                    painter = painterResource(id = it),
                    modifier = Modifier
                        .background(backgroundColor, smallRoundShape)
                        .alpha(if (it == beatIcon) 1f else 0.5f)
                        .size(56.dp)
                        .padding(16.dp),
                    onClick = { onBeatIconSelected(it) }
                )
            }
        }
    }
}

@Composable
fun BeatSoundEffectPanel(
    beatSoundEffect: Int,
    beatSoundEffectList: List<Int>,
    onBeatSoundEffectPicked: (Uri) -> Unit,
    onBeatSoundEffectSelected: (Int) -> Unit
) {
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            onBeatSoundEffectPicked(uri)
            onBeatSoundEffectSelected(1)
        }
    }

    TitlePanel(title = stringResource(id = R.string.beat_sound_effect_title)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.height(130.dp),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(beatSoundEffectList) { index, id ->
                TextPressButton(
                    text = (index + 1).toString(),
                    modifier = Modifier
                        .background(backgroundColor, smallRoundShape)
                        .alpha(if (beatSoundEffect == id) 1f else 0.5f)
                        .size(56.dp),
                    style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    onClick = { onBeatSoundEffectSelected(id) }
                )
            }
            item {
                IconPressButton(
                    painter = painterResource(R.drawable.ic_add),
                    modifier = Modifier
                        .background(backgroundColor, smallRoundShape)
                        .alpha(if (beatSoundEffect == 1) 1f else 0.5f)
                        .size(56.dp)
                        .padding(18.dp),
                    onClick = { picker.launch("audio/*") }
                )
            }
        }
    }
}

@Composable
fun AutoClickPanel(
    autoClickEnabled: Boolean,
    onAutoClickEnabled: (Boolean) -> Unit
) {
    TitlePanel(title = stringResource(R.string.auto_click_title)) {
        Row {
            TextPressButton(
                text = stringResource(R.string.auto_click_enabled_text),
                modifier = Modifier
                    .background(backgroundColor, smallRoundShape)
                    .alpha(if (autoClickEnabled) 1f else 0.5f)
                    .weight(1f)
                    .height(56.dp),
                style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                onClick = { onAutoClickEnabled(true) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            TextPressButton(
                text = stringResource(R.string.auto_click_disabled_text),
                modifier = Modifier
                    .background(backgroundColor, smallRoundShape)
                    .alpha(if (!autoClickEnabled) 1f else 0.5f)
                    .weight(1f)
                    .height(56.dp),
                style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                onClick = { onAutoClickEnabled(false) }
            )
        }
    }
}

@Composable
fun BackgroundMusicPanel(
    backgroundMusicEnabled: Boolean,
    onBackgroundMusicPicked: (Uri) -> Unit,
    onBackgroundMusicEnabled: (Boolean) -> Unit
) {
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            onBackgroundMusicPicked(uri)
            onBackgroundMusicEnabled(true)
        }
    }

    TitlePanel(title = stringResource(R.string.background_music_title)) {
        Row {
            TextPressButton(
                text = stringResource(R.string.background_music_selected_text),
                modifier = Modifier
                    .background(backgroundColor, smallRoundShape)
                    .alpha(if (backgroundMusicEnabled) 1f else 0.5f)
                    .weight(1f)
                    .height(56.dp),
                style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                onClick = { picker.launch("audio/*") }
            )

            Spacer(modifier = Modifier.width(16.dp))

            TextPressButton(
                text = stringResource(R.string.background_music_none_text),
                modifier = Modifier
                    .background(backgroundColor, smallRoundShape)
                    .alpha(if (!backgroundMusicEnabled) 1f else 0.5f)
                    .weight(1f)
                    .height(56.dp),
                style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                onClick = { onBackgroundMusicEnabled(false) }
            )
        }
    }
}