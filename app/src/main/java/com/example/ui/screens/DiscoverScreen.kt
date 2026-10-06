package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.data.local.FoodieConstants
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
import com.example.ui.theme.KineticPrimaryForeground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiscoverScreen(
  restaurants: List<RestaurantEntity>,
  selectedCategories: Set<String>,
  searchQuery: String,
  onToggleCategory: (String) -> Unit,
  onClearCategories: () -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  isFavorite: (String) -> Boolean,
  onRestaurantClick: (String) -> Unit,
  onToggleFavorite: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedAreas by remember { mutableStateOf<Set<String>>(emptySet()) }
  var chipsVisible by remember { mutableStateOf(true) }

  val listState = rememberLazyListState()

  // Track finger movement direction:
  // When scrolling down the list (finger moves UP, available.y < 0): chips slide up & hide behind search bar.
  // When scrolling up towards top (finger moves DOWN, available.y > 0): chips slide down & reappear.
  val nestedScrollConnection = remember {
    object : NestedScrollConnection {
      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (available.y < -6f) {
          chipsVisible = false
        } else if (available.y > 6f) {
          chipsVisible = true
        }
        return Offset.Zero
      }
    }
  }

  // When user returns to the very top, always show chips
  val isAtTop by remember {
    derivedStateOf {
      listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
    }
  }
  LaunchedEffect(isAtTop) {
    if (isAtTop) {
      chipsVisible = true
    }
  }

  // Filter restaurants by multi-selected categories, multi-selected areas, and search query
  val filteredRestaurants = restaurants.filter { r ->
    val matchesCategory = selectedCategories.isEmpty() ||
      selectedCategories.any { cat -> r.getCategoryList().contains(cat) || r.category == cat }
    val matchesArea = selectedAreas.isEmpty() ||
      selectedAreas.any { a -> r.area.equals(a, ignoreCase = true) }
    val matchesSearch = searchQuery.isBlank() ||
      r.name.contains(searchQuery, ignoreCase = true) ||
      r.description.contains(searchQuery, ignoreCase = true) ||
      r.address.contains(searchQuery, ignoreCase = true) ||
      r.area.contains(searchQuery, ignoreCase = true)

    matchesCategory && matchesArea && matchesSearch
  }

  LazyColumn(
    state = listState,
    modifier = modifier
      .fillMaxSize()
      .background(KineticBackground)
      .nestedScroll(nestedScrollConnection),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // 1. NON-STICKY TOP HEADER: La partie supérieure au-dessus de la search bar qui défile normalement
    item(key = "discover_top_header") {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Découvrir",
            style = FoodieTypography.anton44,
            color = KineticForeground
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Les meilleures adresses d'Alger",
            style = FoodieTypography.inter13,
            color = KineticMutedForeground
          )
        }

        Text(
          text = "${filteredRestaurants.size} adresses",
          style = FoodieTypography.inter12,
          color = KineticPrimary
        )
      }
    }

    // 2. STICKY AT THE TOP: Uniquement la search bar et les puces de filtres
    stickyHeader(key = "discover_sticky_search_and_chips") {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(KineticBackground)
      ) {
        // Search Bar Container (zIndex 3f pour masquer les puces qui montent derrière)
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .zIndex(3f)
            .background(KineticBackground)
            .padding(horizontal = 22.dp, vertical = 4.dp)
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = {
              Text(
                text = "Rechercher ou manger...",
                style = FoodieTypography.inter15,
                color = KineticMutedForeground
              )
            },
            textStyle = FoodieTypography.inter16,
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Recherche",
                tint = KineticMutedForeground
              )
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged("") }) {
                  Icon(Icons.Default.Clear, contentDescription = "Effacer")
                }
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("discover_search_input"),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = KineticGlass,
              unfocusedContainerColor = KineticGlassSurface,
              focusedBorderColor = KineticPrimary,
              unfocusedBorderColor = KineticBorder
            )
          )
        }

        // Puces de filtres (zIndex 1f) : montent et disparaissent derrière la search bar au scroll
        this@Column.AnimatedVisibility(
          visible = chipsVisible,
          enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
          ) + expandVertically(animationSpec = tween(durationMillis = 280)),
          exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
          ) + shrinkVertically(animationSpec = tween(durationMillis = 280)),
          modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(KineticBackground)
              .padding(top = 8.dp, bottom = 6.dp)
          ) {
            // Categories filter row (13 px Inter, pilule entièrement arrondie, multi-sélection)
            LazyRow(
              contentPadding = PaddingValues(horizontal = 22.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              item {
                val isAll = selectedCategories.isEmpty()
                Box(
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isAll) KineticPrimary else KineticGlass)
                    .border(1.dp, if (isAll) KineticPrimary else KineticBorder, CircleShape)
                    .clickable { onClearCategories() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("filter_all_cats")
                ) {
                  Text(
                    text = "Tout",
                    color = if (isAll) KineticPrimaryForeground else KineticForeground,
                    style = FoodieTypography.inter13
                  )
                }
              }

              items(FoodieConstants.CATEGORIES) { cat ->
                val isSelected = selectedCategories.contains(cat.id)
                Box(
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) KineticPrimary else KineticGlass)
                    .border(1.dp, if (isSelected) KineticPrimary else KineticBorder, CircleShape)
                    .clickable { onToggleCategory(cat.id) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("filter_cat_${cat.id}")
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                  ) {
                    Text(text = cat.emoji, fontSize = 14.sp)
                    Text(
                      text = cat.label,
                      color = if (isSelected) KineticPrimaryForeground else KineticForeground,
                      style = FoodieTypography.inter13
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Areas filter row (12 px Inter, multi-sélection)
            LazyRow(
              contentPadding = PaddingValues(horizontal = 22.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              item {
                val isAllArea = selectedAreas.isEmpty()
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isAllArea) KineticForeground else KineticGlassSurface)
                    .border(1.dp, if (isAllArea) KineticForeground else KineticBorder, RoundedCornerShape(12.dp))
                    .clickable { selectedAreas = emptySet() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = "Tout Alger",
                    color = if (isAllArea) Color.White else KineticForeground,
                    style = FoodieTypography.inter12
                  )
                }
              }

              items(FoodieConstants.AREAS) { a ->
                val isSelected = selectedAreas.contains(a)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) KineticForeground else KineticGlassSurface)
                    .border(1.dp, if (isSelected) KineticForeground else KineticBorder, RoundedCornerShape(12.dp))
                    .clickable {
                      selectedAreas = if (isSelected) {
                        selectedAreas - a
                      } else {
                        selectedAreas + a
                      }
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = a,
                    color = if (isSelected) Color.White else KineticForeground,
                    style = FoodieTypography.inter12
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. RESTAURANTS OU ÉCRAN VIDE
    if (filteredRestaurants.isEmpty()) {
      item(key = "empty_restaurants_view") {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "404", style = FoodieTypography.inter72, color = KineticMutedForeground.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Aucun restaurant trouvé",
              style = FoodieTypography.anton24,
              color = KineticForeground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Essaie d'autres filtres ou efface la recherche",
              style = FoodieTypography.inter14,
              color = KineticMutedForeground
            )
          }
        }
      }
    } else {
      items(filteredRestaurants, key = { it.id }) { item ->
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 8.dp)
        ) {
          RestaurantCard(
            restaurant = item,
            isFavorite = isFavorite(item.id),
            onCardClick = { onRestaurantClick(item.id) },
            onToggleFavorite = { onToggleFavorite(item.id) },
            isWide = true
          )
        }
      }
    }
  }
}
