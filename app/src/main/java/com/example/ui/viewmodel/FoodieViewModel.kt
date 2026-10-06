package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FoodieConstants
import com.example.data.local.FoodieDatabase
import com.example.data.model.FavoriteEntity
import com.example.data.model.ParticipantEntity
import com.example.data.model.RankedRestaurant
import com.example.data.model.RestaurantEntity
import com.example.data.model.SessionEntity
import com.example.data.model.VoteEntity
import com.example.data.repository.FoodieRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

sealed class Screen {
  object Home : Screen()
  object Discover : Screen()
  object Sessions : Screen()
  object Profile : Screen()
  data class RestaurantDetail(val restaurantId: String) : Screen()
  object CreateSession : Screen()
  object JoinSession : Screen()
  data class SessionView(val sessionCode: String) : Screen()
}

data class CreateSessionDraft(
  val step: Int = 0,
  val area: String? = null,
  val selectedCategories: Set<String> = emptySet(),
  val budgetId: String = "any",
  val groupSize: Int = 4,
  val title: String = "Dîner ce soir",
  val creatorName: String = "",
  val isCreating: Boolean = false
)

class FoodieViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: FoodieRepository
  private val prefs = application.getSharedPreferences("foodie_prefs", Context.MODE_PRIVATE)

  // Navigation stack
  private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
  val currentScreen: StateFlow<Screen> = MutableStateFlow(Screen.Home)
  private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
  val activeScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  // User Profile
  private val _userName = MutableStateFlow(prefs.getString("user_name", "") ?: "")
  val userName: StateFlow<String> = _userName.asStateFlow()

  private val _deviceId: String = prefs.getString("device_id", null) ?: run {
    val newId = UUID.randomUUID().toString()
    prefs.edit().putString("device_id", newId).apply()
    newId
  }

  // Toast / notification messages
  private val _messageEvents = MutableSharedFlow<String>()
  val messageEvents: SharedFlow<String> = _messageEvents.asSharedFlow()

  // Restaurants & Favorites
  val restaurants: StateFlow<List<RestaurantEntity>>
  val favorites: StateFlow<List<FavoriteEntity>>
  val sessions: StateFlow<List<SessionEntity>>

  // Filter state for Discover screen (multi-select)
  private val _selectedCategoriesFilter = MutableStateFlow<Set<String>>(emptySet())
  val selectedCategoriesFilter: StateFlow<Set<String>> = _selectedCategoriesFilter.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  // Create Session Draft
  private val _createDraft = MutableStateFlow(CreateSessionDraft())
  val createDraft: StateFlow<CreateSessionDraft> = _createDraft.asStateFlow()

  // Active Session State
  private val _activeSessionCode = MutableStateFlow<String?>(null)
  private val _activeSession = MutableStateFlow<SessionEntity?>(null)
  val activeSession: StateFlow<SessionEntity?> = _activeSession.asStateFlow()

  private val _sessionParticipants = MutableStateFlow<List<ParticipantEntity>>(emptyList())
  val sessionParticipants: StateFlow<List<ParticipantEntity>> = _sessionParticipants.asStateFlow()

  private val _sessionVotes = MutableStateFlow<List<VoteEntity>>(emptyList())
  val sessionVotes: StateFlow<List<VoteEntity>> = _sessionVotes.asStateFlow()

  private val _sessionRestaurants = MutableStateFlow<List<RestaurantEntity>>(emptyList())
  val sessionRestaurants: StateFlow<List<RestaurantEntity>> = _sessionRestaurants.asStateFlow()

  private val _myParticipantId = MutableStateFlow<String?>(null)
  val myParticipantId: StateFlow<String?> = _myParticipantId.asStateFlow()

  init {
    val database = FoodieDatabase.getDatabase(application)
    repository = FoodieRepository(database.foodieDao())

    viewModelScope.launch {
      repository.ensureDatabaseSeeded()
    }

    restaurants = repository.allRestaurants.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      FoodieConstants.SEED_RESTAURANTS
    )

    favorites = repository.allFavorites.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    sessions = repository.allSessions.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )
  }

  // Navigation
  fun navigateTo(screen: Screen) {
    val current = _screenStack.value
    _screenStack.value = current + screen
    _currentScreen.value = screen
  }

  fun switchTab(screen: Screen) {
    _screenStack.value = listOf(screen)
    _currentScreen.value = screen
  }

  fun navigateBack(): Boolean {
    val current = _screenStack.value
    if (current.size > 1) {
      val newStack = current.dropLast(1)
      _screenStack.value = newStack
      _currentScreen.value = newStack.last()
      return true
    }
    return false
  }

  // User Profile
  fun saveUserName(name: String) {
    _userName.value = name
    prefs.edit().putString("user_name", name).apply()
    viewModelScope.launch {
      _messageEvents.emit("Prénom enregistré !")
    }
  }

  // Favorites
  fun toggleFavorite(restaurantId: String) {
    viewModelScope.launch {
      repository.toggleFavorite(restaurantId)
    }
  }

  fun isRestaurantFavorite(restaurantId: String): Boolean {
    return favorites.value.any { it.restaurantId == restaurantId }
  }

  // Discover Filters (Multi-select)
  fun toggleCategoryFilter(categoryId: String) {
    val current = _selectedCategoriesFilter.value
    _selectedCategoriesFilter.value = if (current.contains(categoryId)) {
      current - categoryId
    } else {
      current + categoryId
    }
  }

  fun clearCategoryFilter() {
    _selectedCategoriesFilter.value = emptySet()
  }

  fun setCategoryFilter(categoryId: String?) {
    _selectedCategoriesFilter.value = if (categoryId == null) emptySet() else setOf(categoryId)
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  // Create Session Flow
  fun resetCreateDraft() {
    _createDraft.value = CreateSessionDraft(
      creatorName = _userName.value
    )
  }

  fun updateCreateStep(step: Int) {
    _createDraft.value = _createDraft.value.copy(step = step)
  }

  fun setDraftArea(area: String?) {
    _createDraft.value = _createDraft.value.copy(area = area)
  }

  fun toggleDraftCategory(categoryId: String) {
    val current = _createDraft.value.selectedCategories
    val updated = if (current.contains(categoryId)) current - categoryId else current + categoryId
    _createDraft.value = _createDraft.value.copy(selectedCategories = updated)
  }

  fun clearDraftCategories() {
    _createDraft.value = _createDraft.value.copy(selectedCategories = emptySet())
  }

  fun setDraftBudget(budgetId: String) {
    _createDraft.value = _createDraft.value.copy(budgetId = budgetId)
  }

  fun setDraftGroupSize(size: Int) {
    _createDraft.value = _createDraft.value.copy(groupSize = size.coerceIn(2, 20))
  }

  fun setDraftTitle(title: String) {
    _createDraft.value = _createDraft.value.copy(title = title)
  }

  fun setDraftCreatorName(name: String) {
    _createDraft.value = _createDraft.value.copy(creatorName = name)
  }

  fun submitCreateSession(onSuccess: (String) -> Unit) {
    val draft = _createDraft.value
    val name = draft.creatorName.ifBlank { _userName.value }.ifBlank { "Moi" }
    saveUserName(name)

    _createDraft.value = draft.copy(isCreating = true)
    viewModelScope.launch {
      try {
        val session = repository.createSession(
          title = draft.title,
          area = draft.area,
          categories = draft.selectedCategories.toList(),
          budget = draft.budgetId,
          groupSize = draft.groupSize,
          creatorName = name
        )
        // Store session code in local history prefs
        addCodeToRecent(session.code)
        _createDraft.value = draft.copy(isCreating = false)
        openSession(session.code, session.creatorId)
        onSuccess(session.code)
      } catch (e: Exception) {
        _createDraft.value = draft.copy(isCreating = false)
        _messageEvents.emit("Erreur de création: ${e.message}")
      }
    }
  }

  // Join Session Flow
  fun joinSession(code: String, participantName: String, onSuccess: (String) -> Unit) {
    val cleanCode = code.uppercase().trim()
    val name = participantName.ifBlank { _userName.value }.ifBlank { "Invité" }
    saveUserName(name)

    viewModelScope.launch {
      val result = repository.joinSession(cleanCode, name)
      result.onSuccess { (session, participant) ->
        addCodeToRecent(session.code)
        openSession(session.code, participant.id)
        onSuccess(session.code)
      }.onFailure { e ->
        _messageEvents.emit(e.message ?: "Impossible de rejoindre la session")
      }
    }
  }

  // Session Observation
  fun openSession(code: String, participantId: String? = null) {
    val cleanCode = code.uppercase().trim()
    _activeSessionCode.value = cleanCode
    if (participantId != null) {
      _myParticipantId.value = participantId
      prefs.edit().putString("participant_$cleanCode", participantId).apply()
    } else {
      _myParticipantId.value = prefs.getString("participant_$cleanCode", null)
    }

    viewModelScope.launch {
      // Observe session details
      repository.getSession(cleanCode).collect { s ->
        _activeSession.value = s
        if (s != null) {
          val ids = s.getRestaurantIdList()
          val loadedRestaurants = repository.getRestaurantsByIds(ids)
          _sessionRestaurants.value = loadedRestaurants
        }
      }
    }

    viewModelScope.launch {
      repository.getParticipants(cleanCode).collect { list ->
        _sessionParticipants.value = list
      }
    }

    viewModelScope.launch {
      repository.getVotes(cleanCode).collect { list ->
        _sessionVotes.value = list
      }
    }
  }

  fun startVoting() {
    val session = _activeSession.value ?: return
    viewModelScope.launch {
      repository.updateSessionStatus(session.id, "voting")
    }
  }

  fun castVote(restaurantId: String, vote: String) {
    val session = _activeSession.value ?: return
    val participantId = _myParticipantId.value ?: return
    viewModelScope.launch {
      repository.castVote(session.code, participantId, restaurantId, vote)
    }
  }

  fun finishSessionVoting() {
    val session = _activeSession.value ?: return
    viewModelScope.launch {
      repository.updateSessionStatus(session.id, "done")
    }
  }

  fun simulateFriendVotes() {
    val session = _activeSession.value ?: return
    viewModelScope.launch {
      repository.simulateFriendVotes(session)
      _messageEvents.emit("Amis simulés ont voté !")
    }
  }

  fun getRankedResults(): List<RankedRestaurant> {
    val restaurantsList = _sessionRestaurants.value
    val votesList = _sessionVotes.value
    val participantsList = _sessionParticipants.value
    return repository.rankRestaurants(restaurantsList, votesList, participantsList.size)
  }

  private fun addCodeToRecent(code: String) {
    val current = prefs.getStringSet("recent_sessions", emptySet()) ?: emptySet()
    val updated = (setOf(code) + current).take(20).toSet()
    prefs.edit().putStringSet("recent_sessions", updated).apply()
  }
}
