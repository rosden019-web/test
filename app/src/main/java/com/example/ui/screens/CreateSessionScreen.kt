package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FoodieConstants
import com.example.ui.components.kineticSheen
import com.example.ui.theme.FoodieTypography
import com.example.ui.theme.KineticBackground
import com.example.ui.theme.KineticBorder
import com.example.ui.theme.KineticForeground
import com.example.ui.theme.KineticGlass
import com.example.ui.theme.KineticGlassSurface
import com.example.ui.theme.KineticInk
import com.example.ui.theme.KineticMutedForeground
import com.example.ui.theme.KineticPrimary
import com.example.ui.theme.KineticPrimaryForeground
import com.example.ui.viewmodel.CreateSessionDraft

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateSessionScreen(
  draft: CreateSessionDraft,
  onStepChange: (Int) -> Unit,
  onAreaChange: (String?) -> Unit,
  onToggleCategory: (String) -> Unit,
  onClearCategories: () -> Unit,
  onBudgetChange: (String) -> Unit,
  onGroupSizeChange: (Int) -> Unit,
  onTitleChange: (String) -> Unit,
  onCreatorNameChange: (String) -> Unit,
  onSubmit: () -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler {
    if (draft.step > 0) {
      onStepChange(draft.step - 1)
    } else {
      onCancel()
    }
  }

  val stepTitles = listOf(
    "Où ?",
    "Qu'est-ce qu'on mange ?",
    "Budget",
    "Combien êtes-vous ?",
    "Dernière touche"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KineticBackground)
      .padding(horizontal = 22.dp, vertical = 22.dp)
  ) {
    // Navigation & Step Indicators
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = {
          if (draft.step > 0) onStepChange(draft.step - 1) else onCancel()
        },
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(KineticGlassSurface)
          .border(1.dp, KineticBorder, CircleShape)
          .testTag("create_step_back_btn")
      ) {
        Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "Retour",
          tint = KineticForeground
        )
      }

      // Step indicator pills
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        for (i in 0..4) {
          Box(
            modifier = Modifier
              .height(6.dp)
              .width(if (i <= draft.step) 24.dp else 12.dp)
              .clip(CircleShape)
              .background(if (i <= draft.step) KineticPrimary else KineticBorder)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Step Header (11 px Inter uppercase)
    Text(
      text = "ÉTAPE ${draft.step + 1} / 5",
      style = FoodieTypography.inter11,
      color = KineticPrimary
    )

    Spacer(modifier = Modifier.height(4.dp))

    // Step Title (40 px Anton)
    Text(
      text = stepTitles[draft.step],
      style = FoodieTypography.anton40,
      color = KineticForeground
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Step Body
    Box(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())
    ) {
      AnimatedContent(
        targetState = draft.step,
        transitionSpec = {
          if (targetState > initialState) {
            slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
          } else {
            slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
          }
        },
        label = "create_step_transition"
      ) { currentStep ->
        when (currentStep) {
          // STEP 0: AREA
          0 -> {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              val isAllSelected = draft.area == null
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(18.dp))
                  .background(if (isAllSelected) KineticPrimary else KineticGlassSurface)
                  .border(1.dp, if (isAllSelected) KineticPrimary else KineticBorder, RoundedCornerShape(18.dp))
                  .clickable { onAreaChange(null) }
                  .padding(18.dp)
                  .testTag("create_area_all")
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = if (isAllSelected) Color.White else KineticPrimary
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = "Tout Alger",
                    color = if (isAllSelected) Color.White else KineticForeground,
                    style = FoodieTypography.inter15,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                FoodieConstants.AREAS.forEach { areaName ->
                  val isSelected = draft.area == areaName
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(16.dp))
                      .background(if (isSelected) KineticPrimary else KineticGlassSurface)
                      .border(1.dp, if (isSelected) KineticPrimary else KineticBorder, RoundedCornerShape(16.dp))
                      .clickable { onAreaChange(areaName) }
                      .padding(horizontal = 16.dp, vertical = 12.dp)
                      .testTag("create_area_$areaName")
                  ) {
                    Text(
                      text = areaName,
                      color = if (isSelected) Color.White else KineticForeground,
                      style = FoodieTypography.inter14
                    )
                  }
                }
              }
            }
          }

          // STEP 1: CATEGORIES
          1 -> {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              val isSurprise = draft.selectedCategories.isEmpty()
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(18.dp))
                  .background(if (isSurprise) KineticInk else KineticGlassSurface)
                  .border(1.dp, if (isSurprise) KineticInk else KineticBorder, RoundedCornerShape(18.dp))
                  .clickable { onClearCategories() }
                  .padding(16.dp)
                  .testTag("create_surprise_cat")
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = "🤷", fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Surprise-moi (Toutes cuisines)",
                    color = if (isSurprise) Color.White else KineticForeground,
                    style = FoodieTypography.inter15
                  )
                }
              }

              FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                FoodieConstants.CATEGORIES.forEach { cat ->
                  val isSelected = draft.selectedCategories.contains(cat.id)
                  Box(
                    modifier = Modifier
                      .clip(CircleShape)
                      .background(if (isSelected) KineticPrimary else KineticGlass)
                      .border(1.dp, if (isSelected) KineticPrimary else KineticBorder, CircleShape)
                      .clickable { onToggleCategory(cat.id) }
                      .padding(horizontal = 16.dp, vertical = 10.dp)
                      .testTag("create_cat_${cat.id}")
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Text(text = cat.emoji, fontSize = 18.sp)
                      Text(
                        text = cat.label,
                        color = if (isSelected) KineticPrimaryForeground else KineticForeground,
                        style = FoodieTypography.inter13
                      )
                    }
                  }
                }
              }
            }
          }

          // STEP 2: BUDGET
          2 -> {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              FoodieConstants.BUDGETS.forEach { b ->
                val isSelected = draft.budgetId == b.id
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isSelected) KineticPrimary else KineticGlassSurface)
                    .border(1.dp, if (isSelected) KineticPrimary else KineticBorder, RoundedCornerShape(18.dp))
                    .clickable { onBudgetChange(b.id) }
                    .padding(18.dp)
                    .testTag("create_budget_${b.id}")
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = b.label,
                      color = if (isSelected) Color.White else KineticForeground,
                      style = FoodieTypography.inter15,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "DA",
                      color = if (isSelected) Color.White.copy(alpha = 0.7f) else KineticMutedForeground,
                      style = FoodieTypography.inter13
                    )
                  }
                }
              }
            }
          }

          // STEP 3: GROUP SIZE (96 px Anton)
          3 -> {
            Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Spacer(modifier = Modifier.height(24.dp))
              Text(
                text = "Combien d'amis votent ?",
                style = FoodieTypography.inter15,
                color = KineticMutedForeground
              )
              Spacer(modifier = Modifier.height(24.dp))

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
              ) {
                IconButton(
                  onClick = { onGroupSizeChange(draft.groupSize - 1) },
                  modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(KineticGlassSurface)
                    .border(1.dp, KineticBorder, CircleShape)
                    .testTag("size_minus_btn")
                ) {
                  Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Moins",
                    modifier = Modifier.size(28.dp),
                    tint = KineticForeground
                  )
                }

                // 96 px Anton for grand compteur
                Text(
                  text = "${draft.groupSize}",
                  style = FoodieTypography.anton96,
                  color = KineticPrimary
                )

                IconButton(
                  onClick = { onGroupSizeChange(draft.groupSize + 1) },
                  modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(KineticPrimary)
                    .testTag("size_plus_btn")
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Plus",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = "personnes prévues",
                style = FoodieTypography.inter15,
                color = KineticMutedForeground
              )
            }
          }

          // STEP 4: LAST DETAILS (16 px Inter)
          4 -> {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
              OutlinedTextField(
                value = draft.title,
                onValueChange = onTitleChange,
                label = { Text("Nom de la session", style = FoodieTypography.inter13) },
                placeholder = { Text("ex. Dîner ce soir...", style = FoodieTypography.inter15) },
                textStyle = FoodieTypography.inter16,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("create_title_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedContainerColor = KineticGlass,
                  unfocusedContainerColor = KineticGlassSurface,
                  focusedBorderColor = KineticPrimary,
                  unfocusedBorderColor = KineticBorder
                )
              )

              OutlinedTextField(
                value = draft.creatorName,
                onValueChange = onCreatorNameChange,
                label = { Text("Ton prénom", style = FoodieTypography.inter13) },
                placeholder = { Text("ex. Ryad, Amine...", style = FoodieTypography.inter15) },
                textStyle = FoodieTypography.inter16,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("create_creator_name_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedContainerColor = KineticGlass,
                  unfocusedContainerColor = KineticGlassSurface,
                  focusedBorderColor = KineticPrimary,
                  unfocusedBorderColor = KineticBorder
                )
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Action button (20 px Anton)
    Button(
      onClick = {
        if (draft.step < 4) {
          onStepChange(draft.step + 1)
        } else {
          onSubmit()
        }
      },
      enabled = !draft.isCreating,
      modifier = Modifier
        .fillMaxWidth()
        .height(58.dp)
        .testTag("create_continue_submit_btn"),
      shape = RoundedCornerShape(18.dp),
      colors = ButtonDefaults.buttonColors(containerColor = KineticPrimary)
    ) {
      if (draft.isCreating) {
        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
      } else {
        Box(modifier = Modifier.fillMaxWidth().kineticSheen(), contentAlignment = Alignment.Center) {
          Text(
            text = if (draft.step < 4) "Continuer" else "Créer la session",
            style = FoodieTypography.anton20,
            color = KineticPrimaryForeground
          )
        }
      }
    }
  }
}
