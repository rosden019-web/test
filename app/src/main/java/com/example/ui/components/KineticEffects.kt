package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Animated sheen reflection over primary action buttons (blanc à 50%)
 */
@Composable
fun Modifier.kineticSheen(): Modifier {
  val transition = rememberInfiniteTransition(label = "kinetic_sheen_transition")
  val progress by transition.animateFloat(
    initialValue = -1.2f,
    targetValue = 2.2f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "sheen_offset"
  )

  return this.drawWithContent {
    drawContent()
    val width = size.width
    val height = size.height
    val sheenWidth = width * 0.45f
    val xStart = width * progress

    val brush = Brush.linearGradient(
      colors = listOf(
        Color.Transparent,
        Color.White.copy(alpha = 0.50f), // blanc à 50% selon la spec
        Color.Transparent
      ),
      start = Offset(xStart, 0f),
      end = Offset(xStart + sheenWidth, height)
    )

    drawRect(brush = brush)
  }
}
