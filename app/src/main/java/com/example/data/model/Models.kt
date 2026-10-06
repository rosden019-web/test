package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.R

@Entity(tableName = "restaurants")
data class RestaurantEntity(
  @PrimaryKey val id: String,
  val name: String,
  val description: String,
  val category: String,
  val categories: String, // comma separated: e.g. "burger,fastfood"
  val area: String,
  val address: String,
  val priceMin: Int,
  val priceMax: Int,
  val rating: Float,
  val imageKey: String,
  val instagramUrl: String? = null,
  val phone: String? = null,
  val openingHours: String? = null,
  val isTrending: Boolean = false
) {
  fun getCategoryList(): List<String> =
    categories.split(",").map { it.trim() }.filter { it.isNotEmpty() }

  val priceRangeFormatted: String
    get() = "${formatPrice(priceMin)}–${formatPrice(priceMax)} DA"

  val drawableRes: Int
    get() = when (imageKey) {
      "burger" -> R.drawable.food_burger
      "pizza" -> R.drawable.food_pizza
      "sushi" -> R.drawable.food_sushi
      "tacos" -> R.drawable.food_tacos
      "brunch" -> R.drawable.food_brunch
      "cafe" -> R.drawable.food_cafe
      "dessert" -> R.drawable.food_dessert
      "grill" -> R.drawable.food_grill
      "algerien" -> R.drawable.food_algerien
      "chicken" -> R.drawable.food_chicken
      else -> R.drawable.food_burger
    }

  companion object {
    fun formatPrice(amount: Int): String {
      return String.format("%,d", amount).replace(',', ' ')
    }
  }
}

@Entity(tableName = "favorites")
data class FavoriteEntity(
  @PrimaryKey val restaurantId: String,
  val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "sessions")
data class SessionEntity(
  @PrimaryKey val id: String,
  val code: String,
  val title: String,
  val area: String?,
  val budget: String,
  val categories: String, // comma separated
  val groupSize: Int,
  val creatorId: String,
  val creatorName: String,
  val status: String, // "lobby", "voting", "done"
  val restaurantIds: String, // comma separated ordered restaurant IDs
  val createdAt: Long = System.currentTimeMillis()
) {
  fun getRestaurantIdList(): List<String> =
    restaurantIds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}

@Entity(tableName = "participants")
data class ParticipantEntity(
  @PrimaryKey val id: String,
  val sessionCode: String,
  val name: String,
  val isHost: Boolean,
  val joinedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "votes")
data class VoteEntity(
  @PrimaryKey val id: String,
  val sessionCode: String,
  val participantId: String,
  val restaurantId: String,
  val vote: String // "love", "maybe", "no"
)

data class FoodCategory(
  val id: String,
  val label: String,
  val emoji: String
)

data class BudgetTier(
  val id: String,
  val label: String,
  val min: Int,
  val max: Int
)

data class RankedRestaurant(
  val restaurant: RestaurantEntity,
  val loveCount: Int,
  val maybeCount: Int,
  val noCount: Int,
  val totalParticipants: Int,
  val score: Int
) {
  val partantsCount: Int get() = loveCount + maybeCount
}
