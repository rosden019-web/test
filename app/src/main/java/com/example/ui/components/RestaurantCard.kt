package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FoodieConstants
import com.example.data.model.RestaurantEntity
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticDestructive
import com.example.ui.theme.KineticForeground
import com.example.ui.theme.KineticGlass
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary
import com.example.ui.theme.KineticPrimaryForeground

@Composable
fun RestaurantCard(
  restaurant: RestaurantEntity,
  isFavorite: Boolean,
  onCardClick: () -> Unit,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier,
  isWide: Boolean = false
) {
  val catInfo = FoodieConstants.CATEGORIES.find { it.id == restaurant.category }
  val cardShape = RoundedCornerShape(20.dp)

  Card(
    modifier = modifier
      .then(if (isWide) Modifier.fillMaxWidth() else Modifier.width(220.dp))
      .border(1.dp, KineticBorder, cardShape)
      .clip(cardShape)
      .clickable(onClick = onCardClick)
      .testTag("restaurant_card_${restaurant.id}"),
    shape = cardShape,
    colors = CardDefaults.cardColors(
      containerColor = KineticGlass
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column {
      // Photo with Badge & Favorite overlay
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(if (isWide) 165.dp else 135.dp)
      ) {
        Image(
          painter = painterResource(id = restaurant.drawableRes),
          contentDescription = restaurant.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Glass reflection / shadow gradient
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(
                  Color.Black.copy(alpha = 0.3f),
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.55f)
                )
              )
            )
        )

        // Badge de prix :
        // Police : Anton, Taille : 12 px, Couleur texte : blanc cassé (KineticPrimaryForeground)
        // Fond : orange corail (KineticPrimary), Forme : pilule entièrement arrondie (CircleShape)
        // Espacement intérieur : 8 px horizontalement et 2 px verticalement, aucune bordure
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
            .clip(CircleShape)
            .background(KineticPrimary)
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(
            text = restaurant.priceRangeFormatted,
            color = KineticPrimaryForeground,
            style = FoodieTypography.anton12
          )
        }

        // Favorite Button bottom right
        IconButton(
          onClick = onToggleFavorite,
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp)
            .size(34.dp)
            .background(Color.Black.copy(alpha = 0.45f), CircleShape)
            .testTag("favorite_btn_${restaurant.id}")
        ) {
          Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favori",
            tint = if (isFavorite) KineticPrimary else Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      // Details sous la photo :
      // Le nom du restaurant sous la photo est en Anton, 17 px
      // La zone ainsi que la note sont en Inter, 12 px, couleur secondaire.
      Column(modifier = Modifier.padding(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = restaurant.name,
            style = FoodieTypography.anton17,
            color = KineticForeground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = KineticPrimary,
              modifier = Modifier.size(13.dp)
            )
            Text(
              text = String.format("%.1f", restaurant.rating),
              style = FoodieTypography.inter12,
              color = KineticMutedForeground
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
          Text(
            text = "${catInfo?.emoji ?: "🍽️"} ${catInfo?.label ?: restaurant.category}",
            style = FoodieTypography.inter12,
            color = KineticMutedForeground
          )
          Text(
            text = "·",
            style = FoodieTypography.inter12,
            color = KineticMutedForeground
          )
          Text(
            text = "📍 ${restaurant.area}",
            style = FoodieTypography.inter12,
            color = KineticMutedForeground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}
