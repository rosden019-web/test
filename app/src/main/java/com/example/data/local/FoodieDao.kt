package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FavoriteEntity
import com.example.data.model.ParticipantEntity
import com.example.data.model.RestaurantEntity
import com.example.data.model.SessionEntity
import com.example.data.model.VoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodieDao {

  // Restaurants
  @Query("SELECT * FROM restaurants ORDER BY rating DESC")
  fun getAllRestaurants(): Flow<List<RestaurantEntity>>

  @Query("SELECT * FROM restaurants WHERE id = :id LIMIT 1")
  fun getRestaurantById(id: String): Flow<RestaurantEntity?>

  @Query("SELECT * FROM restaurants WHERE id = :id LIMIT 1")
  suspend fun getRestaurantByIdDirect(id: String): RestaurantEntity?

  @Query("SELECT * FROM restaurants WHERE id IN (:ids)")
  suspend fun getRestaurantsByIds(ids: List<String>): List<RestaurantEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRestaurants(restaurants: List<RestaurantEntity>)

  @Query("SELECT COUNT(*) FROM restaurants")
  suspend fun countRestaurants(): Int

  // Favorites
  @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
  fun getAllFavorites(): Flow<List<FavoriteEntity>>

  @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE restaurantId = :id)")
  fun isFavorite(id: String): Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavorite(fav: FavoriteEntity)

  @Query("DELETE FROM favorites WHERE restaurantId = :id")
  suspend fun deleteFavorite(id: String)

  // Sessions
  @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
  fun getAllSessions(): Flow<List<SessionEntity>>

  @Query("SELECT * FROM sessions WHERE code = :code LIMIT 1")
  fun getSessionByCode(code: String): Flow<SessionEntity?>

  @Query("SELECT * FROM sessions WHERE code = :code LIMIT 1")
  suspend fun getSessionByCodeDirect(code: String): SessionEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSession(session: SessionEntity)

  @Update
  suspend fun updateSession(session: SessionEntity)

  @Query("UPDATE sessions SET status = :status WHERE id = :sessionId")
  suspend fun updateSessionStatus(sessionId: String, status: String)

  // Participants
  @Query("SELECT * FROM participants WHERE sessionCode = :sessionCode ORDER BY joinedAt ASC")
  fun getParticipants(sessionCode: String): Flow<List<ParticipantEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertParticipant(participant: ParticipantEntity)

  // Votes
  @Query("SELECT * FROM votes WHERE sessionCode = :sessionCode")
  fun getVotes(sessionCode: String): Flow<List<VoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVote(vote: VoteEntity)
}
