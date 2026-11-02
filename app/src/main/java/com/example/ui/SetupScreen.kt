package com.example.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.QcmDocument
import com.example.model.QuizConfig
import com.example.model.QuizMode

@Composable
fun SetupScreen(
    doc: QcmDocument,
    config: QuizConfig,
    onBack: () -> Unit,
    onModeChange: (QuizMode) -> Unit,
    onQuestionCountChange: (Int) -> Unit,
    onPaceSecondsChange: (Int) -> Unit,
    onExamMinutesChange: (Int) -> Unit,
    onShuffleQuestionsChange: (Boolean) -> Unit,
    onShuffleOptionsChange: (Boolean) -> Unit,
    onStartQuiz: () -> Unit
) {
    val totalQuestions = doc.questions.size
    val scoredQuestions = doc.questions.count { it.correctLetter != null }
    val maxQuestions = if (scoredQuestions > 0) scoredQuestions else if (totalQuestions > 0) totalQuestions else 1

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.testTag("back_to_docs_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Retour aux documents")
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = doc.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Mode Selection Cards
        item {
            Text(
                text = "Mode de révision",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                QuizMode.values().forEach { mode ->
                    val isSelected = config.mode == mode
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onModeChange(mode) }
                            .testTag("mode_${mode.name.lowercase()}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.label,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(RoundedCornerShape(13.dp))
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Sélectionné",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Question count chips & direct typing
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Nombre de questions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Champ de saisie pour taper directement le nombre souhaité (défaut : 20 QCMs)
                    val initialCount = if (config.questionCount > 0) {
                        config.questionCount.coerceAtMost(maxQuestions)
                    } else {
                        20.coerceAtMost(maxQuestions)
                    }
                    var customInputText by remember(config.questionCount, maxQuestions) {
                        mutableStateOf(initialCount.toString())
                    }

                    OutlinedTextField(
                        value = customInputText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }
                            customInputText = filtered
                            val parsed = filtered.toIntOrNull()
                            if (parsed != null && parsed > 0) {
                                val clamped = parsed.coerceIn(1, maxQuestions)
                                onQuestionCountChange(clamped)
                            } else if (filtered.isEmpty()) {
                                onQuestionCountChange(0) // 0 means all questions
                            }
                        },
                        label = { Text("Tapez votre nombre désiré") },
                        placeholder = { Text("Ex: 20 (max $maxQuestions)") },
                        supportingText = {
                            Text(
                                text = if (config.questionCount > 0) {
                                    "${config.questionCount} question${if (config.questionCount > 1) "s" else ""} sélectionnée${if (config.questionCount > 1) "s" else ""} sur $maxQuestions"
                                } else {
                                    "Toutes les $maxQuestions questions seront révisées"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_question_count_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Mode-specific sliders / controls
                    if (config.mode == QuizMode.PACE) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Temps par question",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${config.secondsPerQuestion} secondes",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Slider(
                                value = config.secondsPerQuestion.toFloat(),
                                onValueChange = { onPaceSecondsChange(it.toInt()) },
                                valueRange = 10f..120f,
                                steps = 10,
                                modifier = Modifier.testTag("pace_slider")
                            )
                        }
                    }

                    if (config.mode == QuizMode.EXAM) {
                        val autoMin = Math.ceil(
                            (if (config.questionCount > 0) config.questionCount else scoredQuestions) * 0.75
                        ).toInt()
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Durée totale de l'examen",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (config.examTotalMinutes == 0) "Auto ($autoMin min)" else "${config.examTotalMinutes} min",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(0, 5, 10, 15, 20, 30).forEach { mins ->
                                    val label = if (mins == 0) "Auto" else "${mins}m"
                                    FilterChip(
                                        selected = config.examTotalMinutes == mins,
                                        onClick = { onExamMinutesChange(mins) },
                                        label = { Text(label) }
                                    )
                                }
                            }
                        }
                    }

                    // Shuffle checkboxes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = config.shuffleQuestions,
                            onCheckedChange = onShuffleQuestionsChange,
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("shuffle_questions_checkbox")
                        )
                        Text(
                            text = "Mélanger les questions",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable { onShuffleQuestionsChange(!config.shuffleQuestions) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = config.shuffleOptions,
                            onCheckedChange = onShuffleOptionsChange,
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("shuffle_options_checkbox")
                        )
                        Text(
                            text = "Mélanger les réponses",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable { onShuffleOptionsChange(!config.shuffleOptions) }
                        )
                    }
                }
            }
        }

        // Start Quiz CTA
        item {
            Button(
                onClick = onStartQuiz,
                enabled = scoredQuestions > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_quiz_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Commencer la révision",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
