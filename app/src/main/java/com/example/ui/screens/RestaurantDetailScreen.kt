package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FoodieConstants
import com.example.data.model.RestaurantEntity
import com.example.ui.components.kineticSheen
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticBackground
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticForeground
import com.example.ui.theme.KineticGlass
import com.example.ui.theme.KineticGlassSurface
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary
import com.example.ui.theme.KineticPrimaryForeground

@Composable
fun RestaurantDetailScreen(
  restaurant: RestaurantEntity?,
  isFavorite: Boolean,
  onBack: () -> Unit,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val context = LocalContext.current

  if (restaurant == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(KineticBackground),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "Restaurant introuvable",
        style = FoodieTypography.anton36,
        color = KineticForeground
      )
    }
    return
  }

  val catInfo = FoodieConstants.CATEGORIES.find { it.id == restaurant.category }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KineticBackground)
      .verticalScroll(rememberScrollState())
      .padding(bottom = 36.dp)
  ) {
    // Top Hero Photo
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(340.dp)
    ) {
      Image(
        painter = painterResource(id = restaurant.drawableRes),
        contentDescription = restaurant.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      // Gradient overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              listOf(
                Color.Black.copy(alpha = 0.5f),
                Color.Transparent,
                KineticBackground
              )
            )
          )
      )

      // Back button
      IconButton(
        onClick = onBack,
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(start = 16.dp, top = 20.dp)
          .size(44.dp)
          .background(KineticGlassSurface, CircleShape)
          .border(1.dp, KineticBorder, CircleShape)
          .testTag("detail_back_btn")
      ) {
        Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "Retour",
          tint = KineticForeground
        )
      }

      // Favorite button
      IconButton(
        onClick = onToggleFavorite,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(end = 16.dp, top = 20.dp)
          .size(44.dp)
          .background(KineticGlassSurface, CircleShape)
          .border(1.dp, KineticBorder, CircleShape)
          .testTag("detail_favorite_btn")
      ) {
        Icon(
          imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = "Favori",
          tint = if (isFavorite) KineticPrimary else KineticForeground
        )
      }
    }

    // Body content
    Column(
      modifier = Modifier
        .padding(horizontal = 22.dp)
        .offset(y = (-16).dp)
    ) {
      // Category & Rating Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .background(KineticPrimary.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .border(1.dp, KineticPrimary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(
            text = "${catInfo?.emoji ?: "🍽️"} ${catInfo?.label?.uppercase() ?: restaurant.category.uppercase()}",
            color = KineticPrimary,
            style = FoodieTypography.inter11
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFF59E0B),
            modifier = Modifier.size(20.dp)
          )
          Text(
            text = String.format("%.1f", restaurant.rating),
            style = FoodieTypography.anton20,
            color = KineticForeground
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 42 px Anton for restaurant title
      Text(
        text = restaurant.name,
        style = FoodieTypography.anton42,
        color = KineticForeground
      )

      Spacer(modifier = Modifier.height(8.dp))

      // 15 px Inter for description
      Text(
        text = restaurant.description,
        style = FoodieTypography.inter15,
        color = KineticMutedForeground
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Kinetic Glass Info Card (14 px Inter)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, KineticBorder, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = KineticGlass),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Address & Area
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Map,
              contentDescription = null,
              tint = KineticPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = restaurant.address,
                style = FoodieTypography.inter14,
                fontWeight = FontWeight.Bold,
                color = KineticForeground
              )
              Text(
                text = restaurant.area,
                style = FoodieTypography.inter12,
                color = KineticMutedForeground
              )
            }
          }

          // Price Range
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "💰", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = restaurant.priceRangeFormatted,
                style = FoodieTypography.anton17,
                color = KineticPrimary
              )
              Text(
                text = "Fourchette moyenne par personne",
                style = FoodieTypography.inter12,
                color = KineticMutedForeground
              )
            }
          }

          // Opening Hours
          if (restaurant.openingHours != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = KineticPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = "Ouvert : ${restaurant.openingHours}",
                style = FoodieTypography.inter14,
                color = KineticForeground
              )
            }
          }

          // Phone Call
          if (restaurant.phone != null) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${restaurant.phone}"))
                  try { context.startActivity(dialIntent) } catch (_: Exception) {}
                }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = KineticPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = restaurant.phone,
                style = FoodieTypography.inter14,
                fontWeight = FontWeight.Bold,
                color = KineticPrimary
              )
              Spacer(modifier = Modifier.weight(1f))
              Text(
                text = "Appeler",
                style = FoodieTypography.inter11,
                color = KineticMutedForeground
              )
            }
          }

          // Instagram link
          if (restaurant.instagramUrl != null) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  val instaIntent = Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.instagramUrl))
                  try { context.startActivity(instaIntent) } catch (_: Exception) {}
                }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                tint = KineticPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = "Page Instagram",
                style = FoodieTypography.inter14,
                color = KineticForeground
              )
              Spacer(modifier = Modifier.weight(1f))
              Text(
                text = "Ouvrir",
                style = FoodieTypography.inter11,
                color = KineticPrimary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Navigation Google Maps CTA (20 px Anton on KineticPrimary)
      Button(
        onClick = {
          val query = Uri.encode("${restaurant.name} ${restaurant.address} Alger")
          val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$query"))
          try { context.startActivity(mapIntent) } catch (_: Exception) {}
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(58.dp)
          .testTag("detail_directions_btn"),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = KineticPrimary)
      ) {
        Box(modifier = Modifier.fillMaxWidth().kineticSheen(), contentAlignment = Alignment.Center) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.NearMe,
              contentDescription = null,
              tint = KineticPrimaryForeground
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Itinéraire Maps",
              style = FoodieTypography.anton20,
              color = KineticPrimaryForeground
            )
          }
        }
      }
    }
  }
}
