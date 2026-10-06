package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SessionEntity
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticBackground
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticForeground
import com.example.ui.theme.KineticGlass
import com.example.ui.theme.KineticGlassSurface
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary
import com.example.ui.theme.KineticPrimaryForeground
import com.example.ui.theme.KineticSuccess

@Composable
fun SessionsHistoryScreen(
  sessions: List<SessionEntity>,
  onSessionClick: (String) -> Unit,
  onCreateNewSession: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KineticBackground)
      .padding(horizontal = 22.dp, vertical = 22.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Sessions",
          style = FoodieTypography.anton44,
          color = KineticForeground
        )
        Text(
          text = "Vos votes de groupe en cours et récents.",
          style = FoodieTypography.inter15,
          color = KineticMutedForeground
        )
      }

      IconButton(
        onClick = onCreateNewSession,
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(KineticPrimary)
          .testTag("sessions_history_create_btn")
      ) {
        Icon(Icons.Default.Add, contentDescription = "Créer", tint = Color.White)
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    if (sessions.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(40.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .background(KineticGlassSurface, CircleShape)
              .border(1.dp, KineticBorder, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Group,
              contentDescription = null,
              modifier = Modifier.size(36.dp),
              tint = KineticMutedForeground
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Aucune session pour l'instant.",
            style = FoodieTypography.inter15,
            color = KineticMutedForeground
          )

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = onCreateNewSession,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KineticPrimary)
          ) {
            Text("Créer une session", style = FoodieTypography.anton18, color = KineticPrimaryForeground)
          }
        }
      }
    } else {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(sessions, key = { it.id }) { s ->
          val statusLabel = when (s.status) {
            "lobby" -> "Ouvert"
            "voting" -> "Live"
            "done" -> "Terminé"
            else -> s.status
          }
          val statusBg = when (s.status) {
            "lobby" -> KineticGlassSurface
            "voting" -> KineticPrimary.copy(alpha = 0.15f)
            "done" -> KineticSuccess.copy(alpha = 0.15f)
            else -> KineticGlassSurface
          }
          val statusColor = when (s.status) {
            "lobby" -> KineticForeground
            "voting" -> KineticPrimary
            "done" -> KineticSuccess
            else -> KineticForeground
          }

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(18.dp))
              .border(1.dp, KineticBorder, RoundedCornerShape(18.dp))
              .clickable { onSessionClick(s.code) }
              .testTag("session_history_item_${s.code}"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = KineticGlass),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .background(KineticPrimary.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = s.code.take(2),
                  style = FoodieTypography.anton18,
                  color = KineticPrimary
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = s.title,
                  style = FoodieTypography.anton17,
                  color = KineticForeground
                )
                Text(
                  text = "${s.area ?: "Tout Alger"} · code ${s.code}",
                  style = FoodieTypography.inter12,
                  color = KineticMutedForeground
                )
              }

              Box(
                modifier = Modifier
                  .background(statusBg, CircleShape)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = statusLabel,
                  style = FoodieTypography.inter10,
                  color = statusColor
                )
              }

              Spacer(modifier = Modifier.width(6.dp))

              Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = KineticMutedForeground
              )
            }
          }
        }
      }
    }
  }
}
