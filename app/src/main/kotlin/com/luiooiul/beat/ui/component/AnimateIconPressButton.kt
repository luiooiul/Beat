package com.luiooiul.beat.ui.component

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout

@Composable
fun AnimateIconPressButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    animationSpec: AnimationSpec<Int> = spring(),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    var width by remember { mutableStateOf(0) }
    val animateWidth by animateIntAsState(
        targetValue = if (isPressed) width * 9 / 10 else width,
        animationSpec = animationSpec
    )

    var height by remember { mutableStateOf(0) }
    val animateHeight by animateIntAsState(
        targetValue = if (isPressed) height * 9 / 10 else height,
        animationSpec = animationSpec
    )

    Layout(
        content = {},
        modifier = modifier
            .paint(painter = painter, contentScale = ContentScale.Fit)
            .clickable(indication = null, interactionSource = interactionSource, enabled = enabled, onClick = onClick)
    ) { _, constraints ->
        with(constraints) {
            width = minWidth
            height = minHeight
            layout(animateWidth, animateHeight) {}
        }
    }
}