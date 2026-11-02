package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QcmOption
import com.example.model.QuizMode
import com.example.model.SessionItem
import com.example.ui.theme.DarkBad
import com.example.ui.theme.DarkBadBg
import com.example.ui.theme.DarkOk
import com.example.ui.theme.DarkOkBg
import com.example.ui.theme.LightBad
import com.example.ui.theme.LightBadBg
import com.example.ui.theme.LightOk
import com.example.ui.theme.LightOkBg

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizScreen(
    uiState: UiState,
    onSelectOption: (Int) -> Unit,
    onGoToQuestion: (Int) -> Unit,
    onFinish: () -> Unit
) {
    val items = uiState.sessionItems
    val curIndex = uiState.currentIndex
    val currentItem = items.getOrNull(curIndex) ?: return
    val totalQuestions = items.size
    val mode = uiState.config.mode
    val currentAnswer = uiState.answers[curIndex]
    val isAnswered = currentAnswer != null && currentAnswer >= 0

    // Format timer
    val (timerText, progressFraction, isTimerLow) = when (mode) {
        QuizMode.LEARN -> {
            val totalSec = uiState.elapsedTotalSeconds.toInt()
            val m = totalSec / 60
            val s = totalSec % 60
            val frac = if (totalQuestions > 0) (curIndex + 1).toFloat() / totalQuestions.toFloat() else 0f
            Triple(String.format("%d:%02d", m, s), frac, false)
        }
        QuizMode.EXAM -> {
            val remSec = Math.max(0, uiState.examRemainingSeconds.toInt())
            val m = remSec / 60
            val s = remSec % 60
            val totalSec = (uiState.config.examTotalMinutes * 60).coerceAtLeast(1)
            val frac = (remSec.toFloat() / totalSec.toFloat()).coerceIn(0f, 1f)
            Triple(String.format("%d:%02d", m, s), frac, uiState.isTimerLow)
        }
        QuizMode.PACE -> {
            val remSec = Math.max(0, uiState.paceRemainingSeconds.toInt())
            val m = remSec / 60
            val s = remSec % 60
            val frac = (remSec.toFloat() / uiState.config.secondsPerQuestion.toFloat()).coerceIn(0f, 1f)
            Triple(String.format("%d:%02d", m, s), frac, uiState.isTimerLow)
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 250),
        label = "quiz_progress"
    )

    val qcmListState = rememberLazyListState()

    // Garde le numéro de QCM actif visible dans la liste verticale à gauche
    LaunchedEffect(curIndex) {
        val targetIdx = (curIndex - 1).coerceAtLeast(0)
        qcmListState.animateScrollToItem(targetIdx)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        // En-tête supérieur : Titre de question, mode et affichage du temps
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Question ${curIndex + 1} sur $totalQuestions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = when (mode) {
                        QuizMode.EXAM -> "Mode Examen"
                        QuizMode.LEARN -> "Mode Apprentissage"
                        QuizMode.PACE -> "Mode Rythmé"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Badge Chronomètre en haut
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isTimerLow) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = "Chronomètre",
                    tint = if (isTimerLow) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = timerText,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isTimerLow) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("quiz_timer")
                )
            }
        }

        // Espace ajouté en dessous du mode et de l'en-tête de la page
        Spacer(modifier = Modifier.height(16.dp))

        // Ligne de progression du chrono diminuée vers la gauche pour s'aligner parfaitement avec le composant des QCM à droite
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Espace correspondant à la largeur de la colonne des numéros de QCM à gauche
            Spacer(modifier = Modifier.width(52.dp))

            // Espace correspondant au séparateur vertical
            Spacer(modifier = Modifier.width(1.dp))

            // La ligne de progression s'aligne directement au-dessus du composant des QCM
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .testTag("quiz_progress_bar"),
                color = if (isTimerLow) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Corps : À GAUCHE (Numéros des QCM) | À DROITE (Champs questions et réponses)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // =========================================================================
            // GAUCHE : Liste verticale des numéros des QCM (ajustements conservés)
            // =========================================================================
            Column(
                modifier = Modifier
                    .width(52.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "QCM",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyColumn(
                    state = qcmListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("vertical_qcm_list"),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(totalQuestions) { idx ->
                        val answeredThis = uiState.answers.containsKey(idx)
                        val isCur = idx == curIndex

                        val containerColor = when {
                            isCur -> MaterialTheme.colorScheme.primary
                            answeredThis -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        }
                        val textColor = when {
                            isCur -> MaterialTheme.colorScheme.onPrimary
                            answeredThis -> MaterialTheme.colorScheme.onPrimaryContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        val borderColor = when {
                            isCur -> MaterialTheme.colorScheme.primary
                            answeredThis -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(containerColor)
                                .border(
                                    width = if (isCur) 2.dp else 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onGoToQuestion(idx) }
                                .testTag("grid_q_$idx")
                                .testTag("qcm_item_$idx"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isCur) FontWeight.ExtraBold else FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }
            }

            // Séparateur vertical subtil
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            )

            // =========================================================================
            // DROITE : Composant de champs de questions et de réponses
            // =========================================================================
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Carte de la question et champs de réponse
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("question_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = currentItem.text,
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            // Liste des options de réponse
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                currentItem.options.forEachIndexed { optIndex, option ->
                                    OptionItemButton(
                                        mode = mode,
                                        index = optIndex,
                                        option = option,
                                        isSelected = currentAnswer == optIndex,
                                        isAnswered = isAnswered,
                                        onSelect = { onSelectOption(optIndex) }
                                    )
                                }
                            }

                            // Boîte d'explication (en mode Apprentissage une fois répondu)
                            if (mode == QuizMode.LEARN && isAnswered) {
                                val chosenOption = currentItem.options.getOrNull(currentAnswer ?: -1)
                                val isCorrect = chosenOption?.isCorrect == true
                                val correctOption = currentItem.options.firstOrNull { it.isCorrect }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.background)
                                        .border(
                                            BorderStroke(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.outline
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(start = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .drawLeftBorder(if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                                            .padding(12.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(
                                                text = if (isCorrect) {
                                                    "Correct !"
                                                } else {
                                                    "Incorrect. Bonne réponse : ${correctOption?.letter}. ${correctOption?.text ?: ""}"
                                                },
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                            )

                                            if (currentItem.explanation.isNotBlank()) {
                                                Text(
                                                    text = currentItem.explanation,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Boutons d'action et de navigation
                item {
                    when (mode) {
                        QuizMode.EXAM -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onGoToQuestion(curIndex - 1) },
                                        enabled = curIndex > 0,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("exam_prev_button"),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Précédente")
                                    }

                                    OutlinedButton(
                                        onClick = { onGoToQuestion(curIndex + 1) },
                                        enabled = curIndex < totalQuestions - 1,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("exam_next_button"),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Suivante")
                                    }
                                }

                                Button(
                                    onClick = onFinish,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("exam_finish_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Terminer l'examen", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        QuizMode.LEARN -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onFinish,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("learn_stop_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Arrêter")
                                }

                                Button(
                                    onClick = {
                                        if (curIndex < totalQuestions - 1) {
                                            onGoToQuestion(curIndex + 1)
                                        } else {
                                            onFinish()
                                        }
                                    },
                                    enabled = isAnswered,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("learn_next_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (curIndex < totalQuestions - 1) "Suivante" else "Voir le score",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        QuizMode.PACE -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onFinish,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("pace_stop_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Arrêter")
                                }

                                Button(
                                    onClick = {
                                        if (curIndex < totalQuestions - 1) {
                                            onGoToQuestion(curIndex + 1)
                                        } else {
                                            onFinish()
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("pace_skip_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Passer", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun OptionItemButton(
    mode: QuizMode,
    index: Int,
    option: QcmOption,
    isSelected: Boolean,
    isAnswered: Boolean,
    onSelect: () -> Unit
) {
    // Style computation matching HTML .opt classes
    val containerColor: Color
    val borderColor: Color
    val borderWidth = if (isSelected) 2.dp else 1.dp

    if (mode == QuizMode.LEARN && isAnswered) {
        if (option.isCorrect) {
            containerColor = com.example.ui.theme.AppTheme.extendedColors.okBg
            borderColor = com.example.ui.theme.AppTheme.extendedColors.ok
        } else if (isSelected) {
            containerColor = com.example.ui.theme.AppTheme.extendedColors.badBg
            borderColor = com.example.ui.theme.AppTheme.extendedColors.bad
        } else {
            containerColor = MaterialTheme.colorScheme.surface
            borderColor = MaterialTheme.colorScheme.outline
        }
    } else {
        if (isSelected) {
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
            borderColor = MaterialTheme.colorScheme.primary
        } else {
            containerColor = MaterialTheme.colorScheme.surface
            borderColor = MaterialTheme.colorScheme.outline
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(containerColor)
            .border(borderWidth, borderColor, RoundedCornerShape(10.dp))
            .clickable(enabled = !(mode == QuizMode.LEARN && isAnswered)) {
                onSelect()
            }
            .padding(14.dp)
            .testTag("option_${option.letter}"),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Letter indicator badge
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.letter,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = option.text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Optional check or error mark in feedback mode
            if (mode == QuizMode.LEARN && isAnswered) {
                if (option.isCorrect) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Bonne réponse",
                        tint = Color(0xFF15803D),
                        modifier = Modifier.size(20.dp)
                    )
                } else if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Ta réponse (incorrecte)",
                        tint = Color(0xFFB42318),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

fun Modifier.drawLeftBorder(color: Color): Modifier = this.border(
    BorderStroke(4.dp, color),
    shape = RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp, topEnd = 0.dp, bottomEnd = 0.dp)
)
