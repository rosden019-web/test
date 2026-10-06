package com.example.data.repository

import com.example.data.local.FoodieConstants
import com.example.data.local.FoodieDao
import com.example.data.model.FavoriteEntity
import com.example.data.model.ParticipantEntity
import com.example.data.model.RankedRestaurant
import com.example.data.model.RestaurantEntity
import com.example.data.model.SessionEntity
import com.example.data.model.VoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class FoodieRepository(private val dao: FoodieDao) {

  val allRestaurants: Flow<List<RestaurantEntity>> = dao.getAllRestaurants()
  val allSessions: Flow<List<SessionEntity>> = dao.getAllSessions()
  val allFavorites: Flow<List<FavoriteEntity>> = dao.getAllFavorites()

  fun getRestaurant(id: String): Flow<RestaurantEntity?> = dao.getRestaurantById(id)

  fun isFavorite(id: String): Flow<Boolean> = dao.isFavorite(id)

  suspend fun ensureDatabaseSeeded() {
    val count = dao.countRestaurants()
    if (count == 0) {
      dao.insertRestaurants(FoodieConstants.SEED_RESTAURANTS)
    }
  }

  suspend fun toggleFavorite(restaurantId: String) {
    val isFav = dao.isFavorite(restaurantId).firstOrNull() ?: false
    if (isFav) {
      dao.deleteFavorite(restaurantId)
    } else {
      dao.insertFavorite(FavoriteEntity(restaurantId = restaurantId))
    }
  }

  fun getSession(code: String): Flow<SessionEntity?> = dao.getSessionByCode(code.uppercase().trim())

  fun getParticipants(code: String): Flow<List<ParticipantEntity>> =
    dao.getParticipants(code.uppercase().trim())

  fun getVotes(code: String): Flow<List<VoteEntity>> =
    dao.getVotes(code.uppercase().trim())

  suspend fun getRestaurantsByIds(ids: List<String>): List<RestaurantEntity> =
    dao.getRestaurantsByIds(ids)

  suspend fun createSession(
    title: String,
    area: String?,
    categories: List<String>,
    budget: String,
    groupSize: Int,
    creatorName: String
  ): SessionEntity {
    ensureDatabaseSeeded()
    val all = dao.getAllRestaurants().firstOrNull() ?: FoodieConstants.SEED_RESTAURANTS
    val selectedRestaurants = pickRestaurants(all, area, categories, budget)

    val code = generateSessionCode()
    val sessionId = UUID.randomUUID().toString()
    val creatorId = UUID.randomUUID().toString()

    val session = SessionEntity(
      id = sessionId,
      code = code,
      title = title.ifBlank { "Dîner ce soir" },
      area = area,
      budget = budget,
      categories = categories.joinToString(","),
      groupSize = groupSize,
      creatorId = creatorId,
      creatorName = creatorName.ifBlank { "Hôte" },
      status = "lobby",
      restaurantIds = selectedRestaurants.joinToString(",") { it.id }
    )

    dao.insertSession(session)

    // Add creator as first participant
    val creatorParticipant = ParticipantEntity(
      id = creatorId,
      sessionCode = code,
      name = creatorName.ifBlank { "Hôte" },
      isHost = true
    )
    dao.insertParticipant(creatorParticipant)

    return session
  }

  suspend fun joinSession(code: String, participantName: String): Result<Pair<SessionEntity, ParticipantEntity>> {
    val cleanCode = code.uppercase().trim()
    val session = dao.getSessionByCodeDirect(cleanCode)
      ?: return Result.failure(IllegalArgumentException("Code de session introuvable ($cleanCode)"))

    val participantId = UUID.randomUUID().toString()
    val participant = ParticipantEntity(
      id = participantId,
      sessionCode = cleanCode,
      name = participantName.ifBlank { "Invité" },
      isHost = false
    )
    dao.insertParticipant(participant)

    return Result.success(Pair(session, participant))
  }

  suspend fun updateSessionStatus(sessionId: String, status: String) {
    dao.updateSessionStatus(sessionId, status)
  }

  suspend fun castVote(sessionCode: String, participantId: String, restaurantId: String, vote: String) {
    val voteEntity = VoteEntity(
      id = "$sessionCode-$participantId-$restaurantId",
      sessionCode = sessionCode,
      participantId = participantId,
      restaurantId = restaurantId,
      vote = vote
    )
    dao.insertVote(voteEntity)
  }

  suspend fun simulateFriendVotes(session: SessionEntity) {
    val existingParticipants = dao.getParticipants(session.code).firstOrNull() ?: emptyList()
    val needed = session.groupSize - existingParticipants.size
    val friendNames = listOf("Amine", "Sarah", "Yacine", "Lina", "Karim", "Selma", "Nassim", "Inès")
    val restaurantIds = session.getRestaurantIdList()

    val createdFriends = mutableListOf<ParticipantEntity>()
    for (i in 0 until needed.coerceAtLeast(0)) {
      val friendName = friendNames.getOrElse(i) { "Ami #${i + 1}" }
      val p = ParticipantEntity(
        id = UUID.randomUUID().toString(),
        sessionCode = session.code,
        name = friendName,
        isHost = false
      )
      dao.insertParticipant(p)
      createdFriends.add(p)
    }

    // Cast random believable votes for simulated friends
    val voteOptions = listOf("love", "love", "maybe", "maybe", "no")
    val allFriends = (existingParticipants.filter { !it.isHost } + createdFriends)
    for (friend in allFriends) {
      for (rId in restaurantIds) {
        val voteChoice = voteOptions.random()
        val v = VoteEntity(
          id = "${session.code}-${friend.id}-$rId",
          sessionCode = session.code,
          participantId = friend.id,
          restaurantId = rId,
          vote = voteChoice
        )
        dao.insertVote(v)
      }
    }
  }

  fun rankRestaurants(
    restaurants: List<RestaurantEntity>,
    votes: List<VoteEntity>,
    participantCount: Int
  ): List<RankedRestaurant> {
    val effectiveCount = participantCount.coerceAtLeast(1)
    return restaurants.map { r ->
      val restaurantVotes = votes.filter { it.restaurantId == r.id }
      val loveCount = restaurantVotes.count { it.vote == "love" }
      val maybeCount = restaurantVotes.count { it.vote == "maybe" }
      val noCount = restaurantVotes.count { it.vote == "no" }
      // love = +2, maybe = +1, no = -2 (veto penalty)
      val score = (loveCount * 2) + maybeCount - (noCount * 2)

      RankedRestaurant(
        restaurant = r,
        loveCount = loveCount,
        maybeCount = maybeCount,
        noCount = noCount,
        totalParticipants = effectiveCount,
        score = score
      )
    }.sortedWith(
      compareByDescending<RankedRestaurant> { it.score }
        .thenByDescending { it.loveCount }
        .thenByDescending { it.restaurant.rating }
    )
  }

  private fun pickRestaurants(
    all: List<RestaurantEntity>,
    area: String?,
    categories: List<String>,
    budget: String
  ): List<RestaurantEntity> {
    val b = FoodieConstants.BUDGETS.find { it.id == budget } ?: FoodieConstants.BUDGETS.last()

    fun byArea(r: RestaurantEntity) = area == null || r.area.equals(area, ignoreCase = true)
    fun byCategory(r: RestaurantEntity) = categories.isEmpty() ||
      r.getCategoryList().any { it in categories } || r.category in categories
    fun byBudget(r: RestaurantEntity) = r.priceMin <= b.max && r.priceMax >= b.min

    val tier1 = all.filter { byArea(it) && byCategory(it) && byBudget(it) }.shuffled()
    val tier2 = all.filter { byCategory(it) && byBudget(it) }.shuffled()
    val tier3 = all.filter { byCategory(it) }.shuffled()
    val tier4 = all.shuffled()

    val chosen = mutableListOf<RestaurantEntity>()
    val seenIds = mutableSetOf<String>()

    for (tier in listOf(tier1, tier2, tier3, tier4)) {
      for (r in tier) {
        if (chosen.size >= 8) break
        if (seenIds.add(r.id)) {
          chosen.add(r)
        }
      }
      if (chosen.size >= 6) break
    }

    return chosen.ifEmpty { all.take(6) }
  }

  private fun generateSessionCode(): String {
    val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    return (1..6).map { chars.random() }.joinToString("")
  }
}
