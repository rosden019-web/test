package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.FoodieBottomBar
import com.example.ui.screens.CreateSessionScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JoinSessionScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RestaurantDetailScreen
import com.example.ui.screens.SessionScreen
import com.example.ui.screens.SessionsHistoryScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FoodieViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val viewModel: FoodieViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        val context = LocalContext.current
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val activeScreen by viewModel.activeScreen.collectAsState()
        val restaurants by viewModel.restaurants.collectAsState()
        val favorites by viewModel.favorites.collectAsState()
        val sessions by viewModel.sessions.collectAsState()
        val userName by viewModel.userName.collectAsState()

        val selectedCategories by viewModel.selectedCategoriesFilter.collectAsState()
        val searchQuery by viewModel.searchQuery.collectAsState()
        val createDraft by viewModel.createDraft.collectAsState()

        val activeSession by viewModel.activeSession.collectAsState()
        val sessionParticipants by viewModel.sessionParticipants.collectAsState()
        val sessionVotes by viewModel.sessionVotes.collectAsState()
        val sessionRestaurants by viewModel.sessionRestaurants.collectAsState()
        val myParticipantId by viewModel.myParticipantId.collectAsState()

        // Listen for Toast messages
        LaunchedEffect(Unit) {
          viewModel.messageEvents.collectLatest { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          }
        }

        // Determine if bottom bar should be visible
        val showBottomBar = activeScreen is Screen.Home ||
          activeScreen is Screen.Discover ||
          activeScreen is Screen.Sessions ||
          activeScreen is Screen.Profile

        // Hardware Back Handling
        BackHandler(enabled = !showBottomBar) {
          viewModel.navigateBack()
        }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          snackbarHost = { SnackbarHost(snackbarHostState) },
          bottomBar = {
            if (showBottomBar) {
              FoodieBottomBar(
                currentScreen = activeScreen,
                onTabSelected = { tab -> viewModel.switchTab(tab) }
              )
            }
          }
        ) { innerPadding ->
          val contentModifier = Modifier.padding(innerPadding)

          when (val screen = activeScreen) {
            is Screen.Home -> {
              HomeScreen(
                restaurants = restaurants,
                isFavorite = { id -> viewModel.isRestaurantFavorite(id) },
                onRestaurantClick = { id -> viewModel.navigateTo(Screen.RestaurantDetail(id)) },
                onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                onCreateSessionClick = {
                  viewModel.resetCreateDraft()
                  viewModel.navigateTo(Screen.CreateSession)
                },
                onJoinSessionClick = { viewModel.navigateTo(Screen.JoinSession) },
                onViewAllTrending = {
                  viewModel.setCategoryFilter(null)
                  viewModel.switchTab(Screen.Discover)
                },
                onCategoryClick = { catId ->
                  viewModel.setCategoryFilter(catId)
                  viewModel.switchTab(Screen.Discover)
                },
                modifier = contentModifier
              )
            }

            is Screen.Discover -> {
              DiscoverScreen(
                restaurants = restaurants,
                selectedCategories = selectedCategories,
                searchQuery = searchQuery,
                onToggleCategory = { cat -> viewModel.toggleCategoryFilter(cat) },
                onClearCategories = { viewModel.clearCategoryFilter() },
                onSearchQueryChanged = { query -> viewModel.setSearchQuery(query) },
                isFavorite = { id -> viewModel.isRestaurantFavorite(id) },
                onRestaurantClick = { id -> viewModel.navigateTo(Screen.RestaurantDetail(id)) },
                onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                modifier = contentModifier
              )
            }

            is Screen.Sessions -> {
              SessionsHistoryScreen(
                sessions = sessions,
                onSessionClick = { code ->
                  viewModel.openSession(code)
                  viewModel.navigateTo(Screen.SessionView(code))
                },
                onCreateNewSession = {
                  viewModel.resetCreateDraft()
                  viewModel.navigateTo(Screen.CreateSession)
                },
                modifier = contentModifier
              )
            }

            is Screen.Profile -> {
              ProfileScreen(
                userName = userName,
                onSaveUserName = { name -> viewModel.saveUserName(name) },
                favorites = favorites,
                restaurants = restaurants,
                onRestaurantClick = { id -> viewModel.navigateTo(Screen.RestaurantDetail(id)) },
                onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                sessionsCount = sessions.size,
                modifier = contentModifier
              )
            }

            is Screen.RestaurantDetail -> {
              val currentRestaurant = restaurants.find { it.id == screen.restaurantId }
              RestaurantDetailScreen(
                restaurant = currentRestaurant,
                isFavorite = viewModel.isRestaurantFavorite(screen.restaurantId),
                onBack = { viewModel.navigateBack() },
                onToggleFavorite = { viewModel.toggleFavorite(screen.restaurantId) },
                modifier = contentModifier
              )
            }

            is Screen.CreateSession -> {
              CreateSessionScreen(
                draft = createDraft,
                onStepChange = { step -> viewModel.updateCreateStep(step) },
                onAreaChange = { area -> viewModel.setDraftArea(area) },
                onToggleCategory = { cat -> viewModel.toggleDraftCategory(cat) },
                onClearCategories = { viewModel.clearDraftCategories() },
                onBudgetChange = { budget -> viewModel.setDraftBudget(budget) },
                onGroupSizeChange = { size -> viewModel.setDraftGroupSize(size) },
                onTitleChange = { title -> viewModel.setDraftTitle(title) },
                onCreatorNameChange = { name -> viewModel.setDraftCreatorName(name) },
                onSubmit = {
                  viewModel.submitCreateSession { code ->
                    viewModel.navigateTo(Screen.SessionView(code))
                  }
                },
                onCancel = { viewModel.navigateBack() },
                modifier = contentModifier
              )
            }

            is Screen.JoinSession -> {
              JoinSessionScreen(
                initialName = userName,
                onJoinClick = { code, name ->
                  viewModel.joinSession(code, name) { sessionCode ->
                    viewModel.navigateTo(Screen.SessionView(sessionCode))
                  }
                },
                onBack = { viewModel.navigateBack() },
                modifier = contentModifier
              )
            }

            is Screen.SessionView -> {
              SessionScreen(
                session = activeSession,
                restaurants = sessionRestaurants,
                participants = sessionParticipants,
                votes = sessionVotes,
                myParticipantId = myParticipantId,
                rankedResults = viewModel.getRankedResults(),
                onStartVoting = { viewModel.startVoting() },
                onCastVote = { rId, vote -> viewModel.castVote(rId, vote) },
                onFinishVoting = { viewModel.finishSessionVoting() },
                onSimulateFriends = { viewModel.simulateFriendVotes() },
                onRestaurantClick = { id -> viewModel.navigateTo(Screen.RestaurantDetail(id)) },
                onBack = { viewModel.navigateBack() },
                modifier = contentModifier
              )
            }
          }
        }
      }
    }
  }
}
