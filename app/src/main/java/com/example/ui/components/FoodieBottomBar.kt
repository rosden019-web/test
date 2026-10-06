package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticGlassSurface
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary
import com.example.ui.viewmodel.Screen

data class NavTabItem(
  val screen: Screen,
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val tag: String
)

@Composable
fun FoodieBottomBar(
  currentScreen: Screen,
  onTabSelected: (Screen) -> Unit,
  modifier: Modifier = Modifier
) {
  val tabs = listOf(
    NavTabItem(
      screen = Screen.Home,
      label = "Accueil",
      selectedIcon = Icons.Filled.Home,
      unselectedIcon = Icons.Outlined.Home,
      tag = "tab_home"
    ),
    NavTabItem(
      screen = Screen.Discover,
      label = "Découvrir",
      selectedIcon = Icons.Filled.Explore,
      unselectedIcon = Icons.Outlined.Explore,
      tag = "tab_discover"
    ),
    NavTabItem(
      screen = Screen.Sessions,
      label = "Sessions",
      selectedIcon = Icons.Filled.Group,
      unselectedIcon = Icons.Outlined.Group,
      tag = "tab_sessions"
    ),
    NavTabItem(
      screen = Screen.Profile,
      label = "Profil",
      selectedIcon = Icons.Filled.Person,
      unselectedIcon = Icons.Outlined.Person,
      tag = "tab_profile"
    )
  )

  NavigationBar(
    modifier = modifier
      .border(1.dp, KineticBorder)
      .testTag("foodie_bottom_navigation"),
    containerColor = KineticGlassSurface,
    tonalElevation = 6.dp
  ) {
    tabs.forEach { tab ->
      val isSelected = currentScreen == tab.screen
      NavigationBarItem(
        modifier = Modifier.testTag(tab.tag),
        selected = isSelected,
        onClick = { onTabSelected(tab.screen) },
        icon = {
          Icon(
            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
            contentDescription = tab.label,
            modifier = Modifier.size(24.dp)
          )
        },
        label = {
          Text(text = tab.label, style = FoodieTypography.inter11)
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = KineticPrimary,
          selectedTextColor = KineticPrimary,
          unselectedIconColor = KineticMutedForeground,
          unselectedTextColor = KineticMutedForeground,
          indicatorColor = KineticPrimary.copy(alpha = 0.12f)
        )
      )
    }
  }
}
