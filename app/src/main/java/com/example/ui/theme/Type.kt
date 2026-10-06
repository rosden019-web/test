package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// Titres et affichage : Anton, avec Impact/SansSerif en solution de repli
val AntonFamily = FontFamily(
  Font(R.font.anton)
)

// Texte courant et interface : Inter, avec system-ui et sans-serif en repli
val InterFamily = FontFamily(
  Font(R.font.inter)
)

object FoodieTypography {
  // Anton — Titres et affichage (interlignages serrés)
  val anton96 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 96.sp,
    lineHeight = 90.sp,
    letterSpacing = (-1.0).sp
  )
  val anton80 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 80.sp,
    lineHeight = 76.sp,
    letterSpacing = (-0.5).sp
  )
  val anton64 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 64.sp,
    lineHeight = 60.sp,
    letterSpacing = 2.sp
  )
  val anton60 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 60.sp,
    lineHeight = 54.sp,
    letterSpacing = (-0.5).sp
  )
  val anton46 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 46.sp,
    lineHeight = 44.sp,
    letterSpacing = (-0.4).sp
  )
  val anton44 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 44.sp,
    lineHeight = 42.sp,
    letterSpacing = (-0.4).sp
  )
  val anton42 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 42.sp,
    lineHeight = 40.sp,
    letterSpacing = (-0.3).sp
  )
  val anton40 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 40.sp,
    lineHeight = 38.sp,
    letterSpacing = (-0.3).sp
  )
  val anton38 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 38.sp,
    lineHeight = 36.sp,
    letterSpacing = (-0.3).sp
  )
  val anton36 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 36.sp,
    lineHeight = 34.sp
  )
  val anton32 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 32.sp,
    lineHeight = 32.sp
  )
  val anton24 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 24.sp,
    lineHeight = 26.sp,
    letterSpacing = (-0.2).sp
  )
  val anton21 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 21.sp,
    lineHeight = 23.sp,
    letterSpacing = (-0.1).sp
  )
  val anton20 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 20.sp,
    lineHeight = 22.sp
  )
  val anton18 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 18.sp,
    lineHeight = 20.sp
  )
  val anton17 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 17.sp,
    lineHeight = 19.sp
  )
  val anton15 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 15.sp,
    lineHeight = 17.sp
  )
  val anton14 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 14.sp,
    lineHeight = 16.sp
  )
  val anton12 = TextStyle(
    fontFamily = AntonFamily,
    fontSize = 12.sp,
    lineHeight = 14.sp
  )

  // Inter — Texte et interface (hauteurs confortables, graisses 400, 500, 600, 700)
  val inter72 = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 72.sp,
    lineHeight = 72.sp
  )
  val inter16 = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal, // 400
    fontSize = 16.sp,
    lineHeight = 24.sp
  )
  val inter16Medium = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium, // 500
    fontSize = 16.sp,
    lineHeight = 24.sp
  )
  val inter15 = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal, // 400
    fontSize = 15.sp,
    lineHeight = 22.sp
  )
  val inter15Medium = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium, // 500
    fontSize = 15.sp,
    lineHeight = 22.sp
  )
  val inter14 = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal, // 400
    fontSize = 14.sp,
    lineHeight = 20.sp
  )
  val inter14Medium = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium, // 500
    fontSize = 14.sp,
    lineHeight = 20.sp
  )
  val inter14SemiBold = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.SemiBold, // 600
    fontSize = 14.sp,
    lineHeight = 20.sp
  )
  val inter13 = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.SemiBold, // 600
    fontSize = 13.sp,
    lineHeight = 18.sp
  )
  val inter13Medium = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium, // 500
    fontSize = 13.sp,
    lineHeight = 18.sp
  )
  val inter12 = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal, // 400
    fontSize = 12.sp,
    lineHeight = 16.sp
  )
  val inter12Medium = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium, // 500
    fontSize = 12.sp,
    lineHeight = 16.sp
  )
  val inter11 = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Bold, // 700
    fontSize = 11.sp,
    lineHeight = 15.sp,
    letterSpacing = 1.2.sp
  )
  val inter10 = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Bold, // 700
    fontSize = 10.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.5.sp
  )
}

val Typography = Typography(
  displayLarge = FoodieTypography.anton60,
  displayMedium = FoodieTypography.anton44,
  displaySmall = FoodieTypography.anton36,
  headlineLarge = FoodieTypography.anton40,
  headlineMedium = FoodieTypography.anton24,
  headlineSmall = FoodieTypography.anton20,
  titleLarge = FoodieTypography.anton21,
  titleMedium = FoodieTypography.anton18,
  titleSmall = FoodieTypography.anton17,
  bodyLarge = FoodieTypography.inter15,
  bodyMedium = FoodieTypography.inter14,
  bodySmall = FoodieTypography.inter12,
  labelLarge = FoodieTypography.inter13,
  labelMedium = FoodieTypography.inter11,
  labelSmall = FoodieTypography.inter10
)
