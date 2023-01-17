package com.luiooiul.beat.ui.component

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.math.max

@OptIn(ExperimentalTextApi::class)
@Composable
fun FloatText(
    text: String,
    animateList: List<FloatTextAnim>,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle()
) {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult by remember { mutableStateOf(textMeasurer.measure(AnnotatedString(text), style)) }

    Layout(
        modifier = modifier.drawBehind {
            animateList.forEach {
                with(it) { draw(textLayoutResult) }
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

class FloatTextAnim {

    private val anim = Animatable(1f)

    @OptIn(ExperimentalTextApi::class)
    fun DrawScope.draw(layoutResult: TextLayoutResult) {
        drawText(
            alpha = anim.value,
            topLeft = Offset(
                x = 0f,
                y = layoutResult.size.height * anim.value.dec()
            ),
            textLayoutResult = layoutResult
        )
    }

    suspend fun start() = anim.animateTo(
        targetValue = 0f,
        animationSpec = tween(600)
    )
}