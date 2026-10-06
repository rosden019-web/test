package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.kineticSheen
import com.example.ui.theme.AntonFamily
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticBackground
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticForeground
import com.example.ui.theme.KineticGlass
import com.example.ui.theme.KineticGlassSurface
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary
import com.example.ui.theme.KineticPrimaryForeground

@Composable
fun JoinSessionScreen(
  initialName: String,
  onJoinClick: (String, String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  var code by remember { mutableStateOf("") }
  var name by remember { mutableStateOf(initialName) }
  var isSubmitting by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KineticBackground)
      .padding(22.dp)
  ) {
    // Top Bar
    Row(verticalAlignment = Alignment.CenterVertically) {
      IconButton(
        onClick = onBack,
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(KineticGlassSurface)
          .border(1.dp, KineticBorder, CircleShape)
          .testTag("join_back_btn")
      ) {
        Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "Retour",
          tint = KineticForeground
        )
      }
    }

    Spacer(modifier = Modifier.height(26.dp))

    // Mention (11 px Inter uppercase)
    Text(
      text = "REJOINDRE",
      style = FoodieTypography.inter11,
      color = KineticPrimary
    )

    Spacer(modifier = Modifier.height(4.dp))

    // Titre (44 px Anton)
    Text(
      text = "Entre ton code",
      style = FoodieTypography.anton44,
      color = KineticForeground
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Description (15 px Inter)
    Text(
      text = "Entre le code à 6 lettres partagé par tes amis pour voter ensemble.",
      style = FoodieTypography.inter15,
      color = KineticMutedForeground
    )

    Spacer(modifier = Modifier.height(28.dp))

    // Saisie du code (44 px Anton)
    OutlinedTextField(
      value = code,
      onValueChange = { input ->
        val filtered = input.uppercase().filter { it.isLetterOrDigit() }.take(6)
        code = filtered
      },
      textStyle = TextStyle(
        fontFamily = AntonFamily,
        fontSize = 44.sp,
        textAlign = TextAlign.Center,
        letterSpacing = 8.sp,
        color = KineticPrimary
      ),
      placeholder = {
        Text(
          text = "CODE",
          modifier = Modifier.fillMaxWidth(),
          style = TextStyle(
            fontFamily = AntonFamily,
            fontSize = 44.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 8.sp,
            color = KineticBorder.copy(alpha = 0.35f)
          )
        )
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Characters,
        imeAction = ImeAction.Next
      ),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("join_code_input"),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = KineticGlass,
        unfocusedContainerColor = KineticGlassSurface,
        focusedBorderColor = KineticPrimary,
        unfocusedBorderColor = KineticBorder
      )
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Name Input (16 px Inter)
    OutlinedTextField(
      value = name,
      onValueChange = { name = it },
      label = { Text("Ton prénom", style = FoodieTypography.inter13) },
      placeholder = { Text("ex. Amine, Sarah...", style = FoodieTypography.inter15) },
      textStyle = FoodieTypography.inter16,
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(
        onDone = {
          if (code.length == 6) {
            isSubmitting = true
            onJoinClick(code, name)
          }
        }
      ),
      shape = RoundedCornerShape(18.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("join_name_input"),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = KineticGlass,
        unfocusedContainerColor = KineticGlassSurface,
        focusedBorderColor = KineticPrimary,
        unfocusedBorderColor = KineticBorder
      )
    )

    Spacer(modifier = Modifier.weight(1f))

    // Action button (20 px Anton)
    Button(
      onClick = {
        isSubmitting = true
        onJoinClick(code, name)
      },
      enabled = code.length == 6 && !isSubmitting,
      modifier = Modifier
        .fillMaxWidth()
        .height(58.dp)
        .testTag("join_submit_btn"),
      shape = RoundedCornerShape(18.dp),
      colors = ButtonDefaults.buttonColors(containerColor = KineticPrimary)
    ) {
      if (isSubmitting) {
        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
      } else {
        Box(modifier = Modifier.fillMaxWidth().kineticSheen(), contentAlignment = Alignment.Center) {
          Text(
            text = "Rejoindre",
            style = FoodieTypography.anton20,
            color = KineticPrimaryForeground
          )
        }
      }
    }
  }
}
