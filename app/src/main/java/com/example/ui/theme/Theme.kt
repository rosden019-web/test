package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KineticLightColorScheme = lightColorScheme(
  primary = KineticPrimary,
  onPrimary = KineticPrimaryForeground,
  primaryContainer = KineticPrimary.copy(alpha = 0.14f),
  onPrimaryContainer = KineticPrimary,
  secondary = KineticInk,
  onSecondary = KineticInkForeground,
  secondaryContainer = KineticGlass,
  onSecondaryContainer = KineticForeground,
  tertiary = KineticAccent,
  onTertiary = KineticAccentForeground,
  background = KineticBackground,
  onBackground = KineticForeground,
  surface = KineticGlass,
  onSurface = KineticForeground,
  surfaceVariant = KineticMuted,
  onSurfaceVariant = KineticMutedForeground,
  outline = KineticBorder,
  error = KineticDestructive,
  onError = KineticDestructiveForeground
)

private val KineticDarkColorScheme = darkColorScheme(
  primary = KineticPrimary,
  onPrimary = KineticPrimaryForeground,
  primaryContainer = KineticPrimary.copy(alpha = 0.2f),
  onPrimaryContainer = KineticPrimaryForeground,
  secondary = KineticAccent,
  onSecondary = KineticAccentForeground,
  secondaryContainer = Color(0xFF161E30),
  onSecondaryContainer = Color.White,
  tertiary = KineticSuccess,
  onTertiary = Color.White,
  background = KineticInk,
  onBackground = KineticInkForeground,
  surface = Color(0xFF131A2B),
  onSurface = KineticInkForeground,
  surfaceVariant = Color(0xFF1D263B),
  onSurfaceVariant = Color(0xFFA5B2CD),
  outline = Color(0x33A5B2CD),
  error = KineticDestructive,
  onError = KineticDestructiveForeground
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) KineticDarkColorScheme else KineticLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
