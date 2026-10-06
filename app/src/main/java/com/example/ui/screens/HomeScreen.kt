package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FoodieConstants
import com.example.data.model.RestaurantEntity
import com.example.ui.components.RestaurantCard
import com.example.ui.components.kineticSheen
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticAccent
import com.example.ui.theme.KineticBackground
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticForeground
import com.example.ui.theme.KineticGlass
import com.example.ui.theme.KineticGlassSurface
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary
import com.example.ui.theme.KineticPrimaryForeground

@Composable
fun HomeScreen(
  restaurants: List<RestaurantEntity>,
  isFavorite: (String) -> Boolean,
  onRestaurantClick: (String) -> Unit,
  onToggleFavorite: (String) -> Unit,
  onCreateSessionClick: () -> Unit,
  onJoinSessionClick: () -> Unit,
  onViewAllTrending: () -> Unit,
  onCategoryClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val trendingList = restaurants.filter { it.isTrending }

  // Subtle decorative drifting of ambient glowing halos in the background
  val infiniteTransition = rememberInfiniteTransition(label = "ambient_halo_motion")
  val driftY by infiniteTransition.animateFloat(
    initialValue = -12f,
    targetValue = 16f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 6000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "halo_drift_y"
  )
  val driftX by infiniteTransition.animateFloat(
    initialValue = -10f,
    targetValue = 14f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 7500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "halo_drift_x"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(KineticBackground)
  ) {
    // Halo 1 : Bleu accent en haut à gauche
    Box(
      modifier = Modifier
        .size(280.dp)
        .offset(x = (driftX - 40).dp, y = (driftY - 50).dp)
        .background(
          Brush.radialGradient(
            listOf(KineticAccent.copy(alpha = 0.20f), Color.Transparent)
          ),
          CircleShape
        )
    )

    // Halo 2 : Corail primaire en haut à droite
    Box(
      modifier = Modifier
        .size(300.dp)
        .align(Alignment.TopEnd)
        .offset(x = (-driftX + 60).dp, y = (-driftY + 180).dp)
        .background(
          Brush.radialGradient(
            listOf(KineticPrimary.copy(alpha = 0.18f), Color.Transparent)
          ),
          CircleShape
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 28.dp)
    ) {
      // Top Header & Hero section
      Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp)) {
        // Logo (18 px Anton)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "FOODIE",
              style = FoodieTypography.anton18,
              color = KineticForeground
            )
            Text(
              text = ".",
              style = FoodieTypography.anton18,
              color = KineticPrimary
            )
            Text(
              text = "ALGER",
              style = FoodieTypography.anton18,
              color = KineticForeground
            )
          }

          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(KineticGlassSurface)
              .border(1.dp, KineticBorder, CircleShape)
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "📍 ALGER",
              style = FoodieTypography.inter11,
              color = KineticPrimary
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Titre principal de l'accueil (60 px Anton avec 8 px d'espacement fixé entre 'On mange' et 'OÙ ?')
        Text(
          text = "On mange",
          style = FoodieTypography.anton60.copy(
            platformStyle = PlatformTextStyle(includeFontPadding = false),
            lineHeightStyle = LineHeightStyle(
              alignment = LineHeightStyle.Alignment.Center,
              trim = LineHeightStyle.Trim.Both
            )
          ),
          color = KineticForeground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "OÙ ?",
          style = FoodieTypography.anton60.copy(
            platformStyle = PlatformTextStyle(includeFontPadding = false),
            lineHeightStyle = LineHeightStyle(
              alignment = LineHeightStyle.Alignment.Center,
              trim = LineHeightStyle.Trim.Both
            )
          ),
          color = KineticPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Description et présentation (15 px Inter)
        Text(
          text = "Décidez ensemble en quelques secondes. Chacun vote depuis son téléphone.",
          style = FoodieTypography.inter15,
          color = KineticMutedForeground
        )
      }

      // Actions principales (21–20 px Anton, arrondis 22 px)
      Column(
        modifier = Modifier.padding(horizontal = 22.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Bouton principal corail avec reflet sheen animé à 50%
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onCreateSessionClick)
            .testTag("home_create_session_btn"),
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = KineticPrimary),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .kineticSheen()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Créer une session",
                style = FoodieTypography.anton21,
                color = KineticPrimaryForeground
              )

              Box(
                modifier = Modifier
                  .size(38.dp)
                  .background(Color.White.copy(alpha = 0.24f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(22.dp)
                )
              }
            }
          }
        }

        // Bouton secondaire surface blanche avec contour fin
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, KineticBorder, RoundedCornerShape(22.dp))
            .clickable(onClick = onJoinSessionClick)
            .testTag("home_join_session_btn"),
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(
            containerColor = KineticGlassSurface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.Transparent)
              .padding(horizontal = 22.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Rejoindre une session",
              style = FoodieTypography.anton21,
              color = KineticForeground,
              modifier = Modifier.background(Color.Transparent)
            )

            Box(
              modifier = Modifier
                .clip(CircleShape)
                .border(1.dp, KineticBorder, CircleShape)
                .background(Color.Transparent)
                .padding(horizontal = 12.dp, vertical = 5.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "CODE",
                style = FoodieTypography.anton15,
                color = KineticPrimary,
                modifier = Modifier.background(Color.Transparent)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Filtres par envie (13–12 px Inter, pastilles horizontales)
      Column(modifier = Modifier.padding(start = 22.dp)) {
        Text(
          text = "PAR ENVIE",
          style = FoodieTypography.inter11,
          color = KineticMutedForeground
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(end = 22.dp)
        ) {
          items(FoodieConstants.CATEGORIES) { cat ->
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(KineticGlass)
                .border(1.dp, KineticBorder, CircleShape)
                .clickable { onCategoryClick(cat.id) }
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .testTag("cat_chip_${cat.id}")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(text = cat.emoji, fontSize = 16.sp)
                Text(
                  text = cat.label,
                  style = FoodieTypography.inter13,
                  color = KineticForeground
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))

      // Section : Les adresses à Alger (24 px Anton)
      Column {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Les adresses à Alger",
            style = FoodieTypography.anton24,
            color = KineticForeground
          )

          TextButton(
            onClick = onViewAllTrending,
            modifier = Modifier.testTag("view_all_trending_btn")
          ) {
            Text(
              text = "Tout voir",
              style = FoodieTypography.inter13,
              color = KineticPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.ArrowForward,
              contentDescription = null,
              tint = KineticPrimary,
              modifier = Modifier.size(14.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 22.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(trendingList) { item ->
            RestaurantCard(
              restaurant = item,
              isFavorite = isFavorite(item.id),
              onCardClick = { onRestaurantClick(item.id) },
              onToggleFavorite = { onToggleFavorite(item.id) }
            )
          }
        }
      }
    }
  }
}
