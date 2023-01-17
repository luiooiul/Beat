package com.luiooiul.beat.feature.setting

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luiooiul.beat.R
import com.luiooiul.beat.ui.component.IconPressButton
import com.luiooiul.beat.ui.component.TextPressButton
import com.luiooiul.beat.ui.component.TitlePanel
import com.luiooiul.beat.ui.theme.backgroundColor
import com.luiooiul.beat.ui.theme.smallRoundShape
import com.luiooiul.beat.util.AUDIO_TYPE
import com.luiooiul.beat.util.CUSTOM_FILE_ID
import com.luiooiul.beat.util.IMAGE_TYPE
import java.io.InputStream

@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val currentContext = LocalContext.current

    val filesDir = remember { currentContext.filesDir }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingScreen(
        uiState = uiState,
        floatText = viewModel.floatText,
        onBackClick = onBackClick,
        onBeatIconPicked = { viewModel.selectCustomBeatIcon(filesDir, it) },
        onBeatIconSelected = viewModel::selectBeatIcon,
        onBeatSoundEffectPicked = { viewModel.selectCustomSoundEffect(filesDir, it) },
        onBeatSoundEffectSelected = { viewModel.selectBeatSoundEffect(filesDir, it) },
        onFloatTextChange = viewModel::updateFloatText,
        onAutoClickEnabled = viewModel::enabledAutoClick,
        onBackgroundMusicPicked = { viewModel.selectCustomBackgroundMusic(filesDir, it) },
        onBackgroundMusicEnabled = { viewModel.enabledBackgroundMusic(filesDir, it) }
    )
}

@Composable
fun SettingScreen(
    uiState: SettingUiState,
    floatText: String,
    onBackClick: () -> Unit,
    onBeatIconPicked: (InputStream) -> Unit,
    onBeatIconSelected: (Int) -> Unit,
    onBeatSoundEffectPicked: (InputStream) -> Unit,
    onBeatSoundEffectSelected: (Int) -> Unit,
    onFloatTextChange: String.() -> Unit,
    onAutoClickEnabled: (Boolean) -> Unit,
    onBackgroundMusicPicked: (InputStream) -> Unit,
    onBackgroundMusicEnabled: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
    ) {
        if (!uiState.isLoading) {
            // BackButton
            IconPressButton(
                painter = painterResource(R.drawable.ic_back),
                modifier = Modifier.size(24.dp),
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // TopSpacer
                Spacer(modifier = Modifier)
                // FloatText
                FloatTextPanel(
                    text = floatText,
                    onFloatTextChange = onFloatTextChange
                )
                // BeatIcon
                BeatIconPanel(
                    beatIconId = uiState.beatIconId,
                    beatIconIdList = listOf(
                        R.drawable.ic_temple_block,
                        R.drawable.ic_fire,
                        R.drawable.ic_star,
                        R.drawable.ic_coin
                    ),
                    onBeatIconPicked = onBeatIconPicked,
                    onBeatIconSelected = onBeatIconSelected
                )
                // BeatSoundEffect
                BeatSoundEffectPanel(
                    beatSoundEffectId = uiState.beatSoundEffectId,
                    beatSoundEffectIdList = listOf(
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
                // AutoClick
                AutoClickPanel(
                    autoClickEnabled = uiState.autoClickEnabled,
                    onAutoClickEnabled = onAutoClickEnabled
                )
                // BackgroundMusic
                BackgroundMusicPanel(
                    backgroundMusicEnabled = uiState.backgroundMusicEnabled,
                    onBackgroundMusicPicked = onBackgroundMusicPicked,
                    onBackgroundMusicEnabled = onBackgroundMusicEnabled
                )
                // BottomSpacer
                Spacer(modifier = Modifier)
            }
        }
    }
}

@Composable
fun FloatTextPanel(
    text: String,
    onFloatTextChange: String.() -> Unit
) {
    TitlePanel(title = stringResource(R.string.float_text_title)) {
        BasicTextField(
            value = text,
            onValueChange = onFloatTextChange,
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
            cursorBrush = SolidColor(Color.White)
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .background(backgroundColor, smallRoundShape)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (text.isEmpty()) {
                    BasicText(
                        text = stringResource(R.string.float_text_hint),
                        modifier = Modifier.alpha(0.5f),
                        style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    innerTextField()
                }
            }
        }
    }
}

@Composable
fun BeatIconPanel(
    beatIconId: Int,
    beatIconIdList: List<Int>,
    onBeatIconPicked: (InputStream) -> Unit,
    onBeatIconSelected: (Int) -> Unit
) {
    val currentContext = LocalContext.current

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val fileStream = currentContext.contentResolver.openInputStream(uri)
            if (fileStream != null) {
                onBeatIconPicked(fileStream)
            }
        }
    }

    TitlePanel(title = stringResource(R.string.beat_icon_title)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.height(56.dp),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(beatIconIdList) {
                IconPressButton(
                    painter = painterResource(id = it),
                    modifier = Modifier
                        .background(backgroundColor, smallRoundShape)
                        .alpha(if (it == beatIconId) 1f else 0.5f)
                        .height(56.dp)
                        .padding(16.dp),
                    onClick = { onBeatIconSelected(it) }
                )
            }
            item {
                IconPressButton(
                    painter = painterResource(R.drawable.ic_add),
                    modifier = Modifier
                        .background(backgroundColor, smallRoundShape)
                        .alpha(if (beatIconId == CUSTOM_FILE_ID) 1f else 0.5f)
                        .height(56.dp)
                        .padding(16.dp),
                    onClick = { picker.launch(IMAGE_TYPE) }
                )
            }
        }
    }
}

@Composable
fun BeatSoundEffectPanel(
    beatSoundEffectId: Int,
    beatSoundEffectIdList: List<Int>,
    onBeatSoundEffectPicked: (InputStream) -> Unit,
    onBeatSoundEffectSelected: (Int) -> Unit
) {
    val currentContext = LocalContext.current

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val fileStream = currentContext.contentResolver.openInputStream(uri)
            if (fileStream != null) {
                onBeatSoundEffectPicked(fileStream)
            }
        }
    }

    TitlePanel(title = stringResource(id = R.string.beat_sound_effect_title)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.height(128.dp),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(beatSoundEffectIdList) { index, id ->
                TextPressButton(
                    text = (index + 1).toString(),
                    modifier = Modifier
                        .background(backgroundColor, smallRoundShape)
                        .alpha(if (beatSoundEffectId == id) 1f else 0.5f)
                        .height(56.dp),
                    style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    onClick = { onBeatSoundEffectSelected(id) }
                )
            }
            item {
                IconPressButton(
                    painter = painterResource(R.drawable.ic_add),
                    modifier = Modifier
                        .background(backgroundColor, smallRoundShape)
                        .alpha(if (beatSoundEffectId == CUSTOM_FILE_ID) 1f else 0.5f)
                        .height(56.dp)
                        .padding(16.dp),
                    onClick = { picker.launch(AUDIO_TYPE) }
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
    onBackgroundMusicPicked: (InputStream) -> Unit,
    onBackgroundMusicEnabled: (Boolean) -> Unit
) {
    val currentContext = LocalContext.current

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val fileStream = currentContext.contentResolver.openInputStream(uri)
            if (fileStream != null) {
                onBackgroundMusicPicked(fileStream)
            }
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
                onClick = { picker.launch(AUDIO_TYPE) }
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