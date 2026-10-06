package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Kinetic Glass Palette (OKLCH mapping)
val KineticBackground = Color(0xFFEEF3FC) // oklch(0.962 0.014 265) - Fond clair bleuté
val KineticForeground = Color(0xFF090F1C) // oklch(0.17 0.03 265) - Texte foncé
val KineticPrimary = Color(0xFFFA402D) // oklch(0.65 0.225 30) - Orange corail signature
val KineticPrimaryForeground = Color(0xFFF8FAFE) // oklch(0.985 0.006 265) - Texte clair sur primaire
val KineticAccent = Color(0xFF00A6F9) // oklch(0.69 0.17 240) - Bleu deuxième choix / "Pourquoi pas"
val KineticAccentForeground = Color(0xFFF8FAFE) // oklch(0.985 0.006 265)
val KineticMuted = Color(0xFFE3E8F2) // oklch(0.93 0.015 265) - Fond atténué
val KineticMutedForeground = Color(0x94090F1C) // oklch(0.17 0.03 265 / 0.58) - Descriptions & aides
val KineticDestructive = Color(0xFFE7000B) // oklch(0.577 0.245 27.325) - Rouge destructif / "Non"
val KineticDestructiveForeground = Color(0xFFFAFAFA) // oklch(0.985 0 0)
val KineticBorder = Color(0x1A090F1C) // oklch(0.17 0.03 265 / 0.1) - Contours discrets
val KineticInput = Color(0x1F090F1C) // oklch(0.17 0.03 265 / 0.12) - Contours des champs
val KineticRing = Color(0xFFFA402D) // oklch(0.65 0.225 30) - Anneau de focus

// Kinetic Glass Surfaces
val KineticGlass = Color(0xFFFFFFFF) // oklch(1 0 0) - Blanc plein
val KineticGlassSurface = Color(0xE0FFFFFF) // Translucide glass
val KineticGlassMuted = Color(0x8AFFFFFF) // Very sheer glass

// Deep Ink Night Mode (Screen de vote)
val KineticInk = Color(0xFF090F1C) // oklch(0.17 0.03 265) - Fond sombre de vote
val KineticInkForeground = Color(0xFFF8FAFE) // oklch(0.985 0.006 265)
val KineticSuccess = Color(0xFF37BB62) // oklch(0.7 0.17 150) - Indicateur de confirmation

// Compatibility aliases
val FoodiePrimary = KineticPrimary
val FoodieInk = KineticInk
val FoodieAccent = KineticAccent
val FoodieSuccess = KineticSuccess
val FoodieLove = KineticPrimary
val FoodieMaybe = KineticAccent
val FoodieNo = Color(0x2EFFFFFF)
