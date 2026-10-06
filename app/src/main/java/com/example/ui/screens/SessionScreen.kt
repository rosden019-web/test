package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FoodieConstants
import com.example.data.model.ParticipantEntity
import com.example.data.model.RankedRestaurant
import com.example.data.model.RestaurantEntity
import com.example.data.model.SessionEntity
import com.example.data.model.VoteEntity
import com.example.ui.components.kineticSheen
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticAccent
import com.example.ui.theme.KineticAccentForeground
import com.example.ui.theme.KineticBackground
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticForeground
import com.example.ui.theme.KineticGlass
import com.example.ui.theme.KineticGlassSurface
import com.example.ui.theme.KineticInk
import com.example.ui.theme.KineticInkForeground
import com.example.ui.theme.KineticMuted
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary
import com.example.ui.theme.KineticPrimaryForeground
import com.example.ui.theme.KineticSuccess

@Composable
fun SessionScreen(
  session: SessionEntity?,
  restaurants: List<RestaurantEntity>,
  participants: List<ParticipantEntity>,
  votes: List<VoteEntity>,
  myParticipantId: String?,
  rankedResults: List<RankedRestaurant>,
  onStartVoting: () -> Unit,
  onCastVote: (restaurantId: String, vote: String) -> Unit,
  onFinishVoting: () -> Unit,
  onSimulateFriends: () -> Unit,
  onRestaurantClick: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val context = LocalContext.current
  val clipboard = LocalClipboardManager.current

  if (session == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(KineticBackground),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator(color = KineticPrimary)
    }
    return
  }

  val myVotes = votes.filter { it.participantId == myParticipantId }
  val hasVotedAll = restaurants.isNotEmpty() && myVotes.size >= restaurants.size
  val allParticipantsFinished = participants.isNotEmpty() &&
    participants.all { p -> votes.filter { it.participantId == p.id }.size >= restaurants.size }

  val effectiveStatus = if (session.status == "voting" && allParticipantsFinished && restaurants.isNotEmpty()) {
    "done"
  } else {
    session.status
  }

  when (effectiveStatus) {
    // ----------------------------------------------------
    // 1. LOBBY
    // ----------------------------------------------------
    "lobby" -> {
      Column(
        modifier = modifier
          .fillMaxSize()
          .background(KineticBackground)
          .padding(22.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header (18 px Anton)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onBack,
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(KineticGlassSurface)
              .border(1.dp, KineticBorder, CircleShape)
          ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = KineticForeground)
          }

          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(KineticGlassSurface)
              .border(1.dp, KineticBorder, CircleShape)
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text(
              text = session.area ?: "Tout Alger",
              style = FoodieTypography.inter12,
              color = KineticPrimary,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Kinetic Glass Code Card
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, KineticBorder, RoundedCornerShape(24.dp)),
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = KineticGlass),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "TA SESSION EST PRÊTE 🎉",
              style = FoodieTypography.inter11,
              color = KineticPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = session.title,
              style = FoodieTypography.anton24,
              color = KineticForeground,
              textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Code de session (64 px Anton)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(KineticInk)
                .clickable {
                  clipboard.setText(AnnotatedString(session.code))
                }
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .testTag("session_code_display")
            ) {
              Text(
                text = session.code,
                style = FoodieTypography.anton64,
                color = KineticPrimary
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Partager (18 px Anton)
            Button(
              onClick = {
                val sendIntent = Intent().apply {
                  action = Intent.ACTION_SEND
                  putExtra(
                    Intent.EXTRA_TEXT,
                    "On mange où ce soir ? Viens voter sur Foodie Alger avec le code ${session.code} !"
                  )
                  type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Partager le code"))
              },
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(containerColor = KineticInk)
            ) {
              Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = KineticInkForeground)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Partager le code", style = FoodieTypography.anton18, color = KineticInkForeground)
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Participants Header (11 px Inter)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "PARTICIPANTS (${participants.size}/${session.groupSize})",
            style = FoodieTypography.inter11,
            color = KineticMutedForeground
          )

          if (participants.size < session.groupSize) {
            OutlinedButton(
              onClick = onSimulateFriends,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("simulate_friends_btn")
            ) {
              Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Ajouter amis (test)", style = FoodieTypography.inter11)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Participants List
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, KineticBorder, RoundedCornerShape(20.dp)),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = KineticGlass)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            participants.forEach { p ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .background(KineticPrimary.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = p.name.take(1).uppercase(),
                      style = FoodieTypography.anton15,
                      color = KineticPrimary
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = p.name + if (p.id == myParticipantId) " (Toi)" else "",
                    style = FoodieTypography.inter14,
                    fontWeight = FontWeight.SemiBold,
                    color = KineticForeground
                  )
                }

                if (p.isHost) {
                  Box(
                    modifier = Modifier
                      .background(KineticMuted, CircleShape)
                      .padding(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text("Hôte", style = FoodieTypography.inter10, color = KineticForeground)
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Start Voting button (20 px Anton)
        Button(
          onClick = onStartVoting,
          modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .testTag("start_voting_btn"),
          shape = RoundedCornerShape(18.dp),
          colors = ButtonDefaults.buttonColors(containerColor = KineticPrimary)
        ) {
          Box(modifier = Modifier.fillMaxWidth().kineticSheen(), contentAlignment = Alignment.Center) {
            Text(
              text = "Lancer les votes 🔥 (${restaurants.size} restaurants)",
              style = FoodieTypography.anton20,
              color = KineticPrimaryForeground
            )
          }
        }
      }
    }

    // ----------------------------------------------------
    // 2. VOTING (ÉCRAN D'ENCRE SOMBRE)
    // ----------------------------------------------------
    "voting" -> {
      if (hasVotedAll) {
        // Grand compteur d'attente (80 px / 96 px Anton)
        val completedCount = participants.count { p ->
          votes.count { it.participantId == p.id } >= restaurants.size
        }

        Column(
          modifier = modifier
            .fillMaxSize()
            .background(KineticBackground)
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          // 80 px Anton for votes terminés
          Text(
            text = "$completedCount/${participants.size}",
            style = FoodieTypography.anton80,
            color = KineticPrimary
          )

          Spacer(modifier = Modifier.height(8.dp))

          // 18 px Anton for 'ont fini de voter'
          Text(
            text = "ont fini de voter",
            style = FoodieTypography.anton18,
            color = KineticForeground
          )

          Spacer(modifier = Modifier.height(8.dp))

          // 15 px Inter for explanation
          Text(
            text = "Le résultat s'affiche dès que tout le monde a voté.",
            style = FoodieTypography.inter15,
            color = KineticMutedForeground,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(30.dp))

          // Voir le résultat (20 px Anton)
          Button(
            onClick = onFinishVoting,
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .testTag("view_results_early_btn"),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KineticInk)
          ) {
            Text(
              text = "Voir le résultat maintenant",
              style = FoodieTypography.anton20,
              color = KineticInkForeground
            )
          }
        }
      } else {
        // Active Voting Card
        val unvotedRestaurants = restaurants.filter { r ->
          myVotes.none { it.restaurantId == r.id }
        }
        val currentRestaurant = unvotedRestaurants.firstOrNull() ?: restaurants.first()
        val currentIdx = restaurants.size - unvotedRestaurants.size

        Column(
          modifier = modifier
            .fillMaxSize()
            .background(KineticInk)
        ) {
          // Top Photo Card
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1.1f)
          ) {
            Image(
              painter = painterResource(id = currentRestaurant.drawableRes),
              contentDescription = currentRestaurant.name,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )

            // Dark gradient overlay
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    listOf(
                      Color.Black.copy(alpha = 0.5f),
                      Color.Transparent,
                      KineticInk
                    )
                  )
                )
            )

            // Back button
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(40.dp)
                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
              Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = Color.White)
            }

            // Price badge (14 px Anton)
            Box(
              modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(KineticPrimary, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = currentRestaurant.priceRangeFormatted,
                color = KineticPrimaryForeground,
                style = FoodieTypography.anton14
              )
            }
          }

          // Restaurant info & Voting buttons
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .weight(0.9f)
              .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // 38 px Anton for Nom du restaurant pendant le vote
                Text(
                  text = currentRestaurant.name,
                  style = FoodieTypography.anton38,
                  color = KineticInkForeground,
                  modifier = Modifier.weight(1f)
                )

                // 14 px Anton for Indicateur de progression
                Text(
                  text = "${currentIdx + 1} / ${restaurants.size}",
                  style = FoodieTypography.anton14,
                  color = KineticInkForeground.copy(alpha = 0.6f)
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              // 14 px Inter for secondary metadata
              val catInfo = FoodieConstants.CATEGORIES.find { it.id == currentRestaurant.category }
              Text(
                text = "${catInfo?.emoji ?: "🍽️"} ${catInfo?.label ?: currentRestaurant.category} · 📍 ${currentRestaurant.area} · ⭐ ${currentRestaurant.rating}",
                style = FoodieTypography.inter14,
                color = KineticInkForeground.copy(alpha = 0.8f)
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = currentRestaurant.description,
                style = FoodieTypography.inter13,
                color = KineticInkForeground.copy(alpha = 0.65f),
                maxLines = 2
              )
            }

            // 3 VOTING BUTTONS (15 px Anton)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // NO BUTTON (Destructif / Sheer Ink)
              Card(
                modifier = Modifier
                  .weight(1f)
                  .height(84.dp)
                  .clip(RoundedCornerShape(20.dp))
                  .clickable { onCastVote(currentRestaurant.id, "no") }
                  .testTag("vote_btn_no"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF))
              ) {
                Column(
                  modifier = Modifier.fillMaxSize(),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center
                ) {
                  Text(text = "❌", fontSize = 24.sp)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(text = "Non", style = FoodieTypography.anton15, color = KineticInkForeground)
                }
              }

              // MAYBE BUTTON (KineticAccent Blue)
              Card(
                modifier = Modifier
                  .weight(1f)
                  .height(84.dp)
                  .clip(RoundedCornerShape(20.dp))
                  .clickable { onCastVote(currentRestaurant.id, "maybe") }
                  .testTag("vote_btn_maybe"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = KineticAccent)
              ) {
                Column(
                  modifier = Modifier.fillMaxSize(),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center
                ) {
                  Text(text = "👍", fontSize = 24.sp)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(text = "Pourquoi pas", style = FoodieTypography.anton15, color = KineticAccentForeground)
                }
              }

              // LOVE BUTTON (KineticPrimary Coral)
              Card(
                modifier = Modifier
                  .weight(1f)
                  .height(84.dp)
                  .clip(RoundedCornerShape(20.dp))
                  .clickable { onCastVote(currentRestaurant.id, "love") }
                  .testTag("vote_btn_love"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = KineticPrimary)
              ) {
                Column(
                  modifier = Modifier.fillMaxSize(),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center
                ) {
                  Text(text = "❤️", fontSize = 24.sp)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(text = "J'adore", style = FoodieTypography.anton15, color = KineticPrimaryForeground)
                }
              }
            }
          }
        }
      }
    }

    // ----------------------------------------------------
    // 3. RESULTS (WINNER PODIUM)
    // ----------------------------------------------------
    "done" -> {
      val top = rankedResults.firstOrNull()
      val others = rankedResults.drop(1)
      val totalP = participants.size.coerceAtLeast(1)

      Column(
        modifier = modifier
          .fillMaxSize()
          .background(KineticBackground)
          .verticalScroll(rememberScrollState())
          .padding(bottom = 36.dp)
      ) {
        // Top Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onBack,
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(KineticGlassSurface)
              .border(1.dp, KineticBorder, CircleShape)
          ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = KineticForeground)
          }

          Text(
            text = "RÉSULTAT DU VOTE",
            style = FoodieTypography.inter11,
            color = KineticPrimary
          )
        }

        if (top != null) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .background(KineticPrimary, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "🎯", fontSize = 30.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 11 px Inter uppercase
            Text(
              text = "VOUS AVEZ UN MATCH",
              style = FoodieTypography.inter11,
              color = KineticPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 46 px Anton for Nom du restaurant gagnant
            Text(
              text = top.restaurant.name,
              style = FoodieTypography.anton46,
              color = KineticForeground,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 15 px Inter for verdict
            val verdict = when {
              top.loveCount == totalP -> "Tout le monde adore !"
              top.partantsCount == totalP -> "Tout le monde est partant !"
              else -> "${top.partantsCount}/${totalP} sont partants."
            }

            Text(
              text = verdict,
              style = FoodieTypography.inter15,
              fontWeight = FontWeight.Bold,
              color = KineticForeground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Winner Photo with glass border
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(22.dp))
                .border(1.dp, KineticBorder, RoundedCornerShape(22.dp)),
              shape = RoundedCornerShape(22.dp)
            ) {
              Image(
                painter = painterResource(id = top.restaurant.drawableRes),
                contentDescription = top.restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // On y va CTA (20 px Anton on KineticPrimary)
            Button(
              onClick = { onRestaurantClick(top.restaurant.id) },
              modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .testTag("winner_on_y_va_btn"),
              shape = RoundedCornerShape(18.dp),
              colors = ButtonDefaults.buttonColors(containerColor = KineticPrimary)
            ) {
              Box(modifier = Modifier.fillMaxWidth().kineticSheen(), contentAlignment = Alignment.Center) {
                Text(
                  text = "On y va",
                  style = FoodieTypography.anton20,
                  color = KineticPrimaryForeground
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(28.dp))

          // Other Rankings
          if (others.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 22.dp)) {
              Text(
                text = "Autres options",
                style = FoodieTypography.inter11,
                color = KineticMutedForeground
              )

              Spacer(modifier = Modifier.height(10.dp))

              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .border(1.dp, KineticBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = KineticGlass)
              ) {
                Column {
                  val medals = listOf("🥈", "🥉", "4.", "5.", "6.", "7.", "8.")
                  others.forEachIndexed { idx, ranked ->
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRestaurantClick(ranked.restaurant.id) }
                        .padding(14.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Text(
                        text = medals.getOrElse(idx) { "${idx + 2}." },
                        style = FoodieTypography.anton17,
                        color = KineticForeground,
                        modifier = Modifier.width(32.dp)
                      )

                      Image(
                        painter = painterResource(id = ranked.restaurant.drawableRes),
                        contentDescription = ranked.restaurant.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                          .size(46.dp)
                          .clip(RoundedCornerShape(12.dp))
                      )

                      Spacer(modifier = Modifier.width(12.dp))

                      Column(modifier = Modifier.weight(1f)) {
                        Text(
                          text = ranked.restaurant.name,
                          style = FoodieTypography.anton17,
                          color = KineticForeground
                        )
                        Text(
                          text = ranked.restaurant.area,
                          style = FoodieTypography.inter12,
                          color = KineticMutedForeground
                        )
                      }

                      Text(
                        text = "${ranked.partantsCount}/$totalP",
                        style = FoodieTypography.anton18,
                        color = KineticPrimary
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
