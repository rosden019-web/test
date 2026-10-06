package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.FavoriteEntity
import com.example.data.model.ParticipantEntity
import com.example.data.model.RestaurantEntity
import com.example.data.model.SessionEntity
import com.example.data.model.VoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    RestaurantEntity::class,
    FavoriteEntity::class,
    SessionEntity::class,
    ParticipantEntity::class,
    VoteEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class FoodieDatabase : RoomDatabase() {

  abstract fun foodieDao(): FoodieDao

  companion object {
    @Volatile
    private var INSTANCE: FoodieDatabase? = null

    fun getDatabase(context: Context): FoodieDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          FoodieDatabase::class.java,
          "foodie_alger_database"
        )
          .fallbackToDestructiveMigration()
          .addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              CoroutineScope(Dispatchers.IO).launch {
                getDatabase(context).foodieDao().insertRestaurants(FoodieConstants.SEED_RESTAURANTS)
              }
            }
          })
          .build()

        INSTANCE = instance
        instance
      }
    }
  }
}
