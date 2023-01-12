package com.luiooiul.beat.ui.component

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.math.max

@OptIn(ExperimentalTextApi::class)
@Composable
fun FloatText(
    text: String,
    times: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle()
) {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult by remember { mutableStateOf(textMeasurer.measure(AnnotatedString(text), style)) }

    val rememberTimes by rememberUpdatedState(times)
    val animatedFloatTextList = remember { mutableStateListOf<Animatable<Float, AnimationVector1D>>() }

    LaunchedEffect(Unit) {
        snapshotFlow { rememberTimes }.drop(1).collect {
            launch {
                with(Animatable(1f)) {
                    animatedFloatTextList.add(this)
                    this.animateTo(0f, tween(800))
                    animatedFloatTextList.remove(this)
                }
            }
        }
    }

    Layout(
        modifier = modifier.drawBehind {
            animatedFloatTextList.forEach {
                drawText(
                    alpha = it.value,
                    topLeft = Offset(0f, textLayoutResult.size.height * it.value.dec()),
                    textLayoutResult = textLayoutResult
                )
            }
        }
    ) { _, constraints ->
        with(constraints) {
            val width = max(minWidth, textLayoutResult.size.width)
            val height = max(minHeight, textLayoutResult.size.height)
            layout(width, height) {}
        }
    }
}