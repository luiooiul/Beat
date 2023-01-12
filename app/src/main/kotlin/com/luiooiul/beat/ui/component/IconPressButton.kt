package com.luiooiul.beat.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout

@Composable
fun IconPressButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    Layout(
        content = {},
        modifier = modifier
            .paint(painter = painter, contentScale = ContentScale.Fit)
            .clickable(indication = null, interactionSource = interactionSource, enabled = enabled, onClick = onClick)
    ) { _, constraints ->
        with(constraints) {
            val width = if (isPressed) minWidth * 9 / 10 else minWidth
            val height = if (isPressed) minHeight * 9 / 10 else minHeight
            layout(width, height) {}
        }
    }
}