package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.FoodieConstants
import com.example.data.model.ParticipantEntity
import com.example.data.model.RestaurantEntity
import com.example.data.model.VoteEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Foodie Alger", appName)
  }

  @Test
  fun `seed restaurants are loaded and categorized`() {
    val seeds = FoodieConstants.SEED_RESTAURANTS
    assertTrue("Seed restaurants should not be empty", seeds.isNotEmpty())
    assertTrue("All restaurants have valid ratings", seeds.all { it.rating in 3.0f..5.0f })
    assertTrue("All restaurants have positive price range", seeds.all { it.priceMin > 0 && it.priceMax >= it.priceMin })
  }
}
