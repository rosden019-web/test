package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FavoriteEntity
import com.example.data.model.RestaurantEntity
import com.example.ui.components.RestaurantCard
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticBackground
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticForeground
import com.example.ui.theme.KineticGlass
import com.example.ui.theme.KineticGlassSurface
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary

@Composable
fun ProfileScreen(
  userName: String,
  onSaveUserName: (String) -> Unit,
  favorites: List<FavoriteEntity>,
  restaurants: List<RestaurantEntity>,
  onRestaurantClick: (String) -> Unit,
  onToggleFavorite: (String) -> Unit,
  sessionsCount: Int,
  modifier: Modifier = Modifier
) {
  var nameInput by remember(userName) { mutableStateOf(userName) }
  val favRestaurants = restaurants.filter { r -> favorites.any { it.restaurantId == r.id } }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(KineticBackground)
      .padding(horizontal = 22.dp, vertical = 22.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 44 px Anton
    item {
      Text(
        text = "Profil",
        style = FoodieTypography.anton44,
        color = KineticForeground
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Ton prénom et tes restaurants favoris.",
        style = FoodieTypography.inter15,
        color = KineticMutedForeground
      )
    }

    // Name Input Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, KineticBorder, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = KineticGlass),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Ton prénom",
            style = FoodieTypography.inter13,
            color = KineticMutedForeground
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            placeholder = { Text("ex. Amine, Ryad...", style = FoodieTypography.inter15) },
            textStyle = FoodieTypography.inter16,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("profile_name_input"),
            trailingIcon = {
              if (nameInput != userName && nameInput.isNotBlank()) {
                IconButton(
                  onClick = { onSaveUserName(nameInput.trim()) },
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(KineticPrimary)
                    .size(36.dp)
                ) {
                  Icon(Icons.Default.Check, contentDescription = "Enregistrer", tint = Color.White)
                }
              }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = KineticGlass,
              unfocusedContainerColor = KineticGlassSurface,
              focusedBorderColor = KineticPrimary,
              unfocusedBorderColor = KineticBorder
            )
          )
        }
      }
    }

    // Quick Stats
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Card(
          modifier = Modifier
            .weight(1f)
            .border(1.dp, KineticBorder, RoundedCornerShape(18.dp)),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = KineticGlass)
        ) {
          Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "❤️", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${favRestaurants.size}",
              style = FoodieTypography.anton24,
              color = KineticPrimary
            )
            Text(text = "Favoris", style = FoodieTypography.inter12, color = KineticMutedForeground)
          }
        }

        Card(
          modifier = Modifier
            .weight(1f)
            .border(1.dp, KineticBorder, RoundedCornerShape(18.dp)),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = KineticGlass)
        ) {
          Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🎲", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "$sessionsCount",
              style = FoodieTypography.anton24,
              color = KineticForeground
            )
            Text(text = "Sessions", style = FoodieTypography.inter12, color = KineticMutedForeground)
          }
        }

        Card(
          modifier = Modifier
            .weight(1f)
            .border(1.dp, KineticBorder, RoundedCornerShape(18.dp)),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = KineticGlass)
        ) {
          Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "📍", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${restaurants.size}",
              style = FoodieTypography.anton24,
              color = KineticForeground
            )
            Text(text = "Restos Alger", style = FoodieTypography.inter12, color = KineticMutedForeground)
          }
        }
      }
    }

    // 24 px Anton for Favoris section title
    item {
      Text(
        text = "❤️ Favoris",
        style = FoodieTypography.anton24,
        color = KineticForeground
      )
    }

    if (favRestaurants.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, KineticBorder, RoundedCornerShape(18.dp)),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = KineticGlass)
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Ajoute des restos en favoris depuis leur fiche.",
              style = FoodieTypography.inter14,
              color = KineticMutedForeground
            )
          }
        }
      }
    } else {
      items(favRestaurants, key = { it.id }) { r ->
        RestaurantCard(
          restaurant = r,
          isFavorite = true,
          onCardClick = { onRestaurantClick(r.id) },
          onToggleFavorite = { onToggleFavorite(r.id) },
          isWide = true
        )
      }
    }
  }
}
