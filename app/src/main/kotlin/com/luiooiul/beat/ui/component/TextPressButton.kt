package com.luiooiul.beat.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.*

@OptIn(ExperimentalTextApi::class)
@Composable
fun TextPressButton(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult by remember(isPressed) {
        mutableStateOf(
            textMeasurer.measure(
                text = AnnotatedString(text),
                style = style.copy(fontSize = style.fontSize.let { if (isPressed) it * 0.9 else it })
            )
        )
    }

    Canvas(
        modifier = modifier
            .clickable(indication = null, interactionSource = interactionSource, enabled = enabled, onClick = onClick)
    ) {
        drawText(
            topLeft = Offset(
                x = (size.width - textLayoutResult.size.width) / 2,
                y = (size.height - textLayoutResult.size.height) / 2
            ),
            textLayoutResult = textLayoutResult
        )
    }
}