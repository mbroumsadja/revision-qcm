package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.local.QuizScoreRecordEntity
import com.example.model.QcmDocument
import com.example.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    documents: List<QcmDocument>,
    scores: List<QuizScoreRecordEntity>,
    onOpenSetup: (QcmDocument) -> Unit,
    onDeleteDocument: (Long) -> Unit,
    onClearAllDocuments: () -> Unit,
    onGoToImport: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("docs") } // "docs" or "scores"

    val totalSessions = scores.size
    val averageScore = if (totalSessions > 0) scores.map { it.scorePercent }.average().toInt() else 0
    val bestScore = if (totalSessions > 0) scores.maxOf { it.scorePercent } else 0

    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.FRENCH)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Spacing - Header removed for clean, minimal aesthetic
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Fenêtres de sélection : Données et Scores uniquement
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "docs",
                    onClick = { selectedFilter = "docs" },
                    label = {
                        Text(
                            text = "Données",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selectedFilter == "docs") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 46.dp)
                        .testTag("filter_docs")
                )
                FilterChip(
                    selected = selectedFilter == "scores",
                    onClick = { selectedFilter = "scores" },
                    label = {
                        Text(
                            text = "Scores",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selectedFilter == "scores") FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 46.dp)
                        .testTag("filter_scores")
                )
            }
        }

        // ==========================================
        // FENÊTRE 1: DONNÉES DES QCM
        // ==========================================
        if (selectedFilter == "docs") {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Données des QCM",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (documents.isNotEmpty()) {
                            TextButton(
                                onClick = onClearAllDocuments,
                                modifier = Modifier.testTag("clear_all_docs_button")
                            ) {
                                Text("Vider", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            if (documents.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 640.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Aucun document enregistré",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = onGoToImport,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .testTag("empty_go_to_import_button")
                                    .heightIn(min = 44.dp)
                            ) {
                                Text("Charger un QCM", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                items(documents, key = { it.id }) { doc ->
                    val attempts = scores.count { it.documentId == doc.id }
                    DocumentCardItem(
                        doc = doc,
                        attemptsCount = attempts,
                        onRevise = { onOpenSetup(doc) },
                        onDelete = { onDeleteDocument(doc.id) },
                        modifier = Modifier.widthIn(max = 640.dp)
                    )
                }
            }
        }

        // ==========================================
        // FENÊTRE 2: HISTORIQUE DES SCORES
        // ==========================================
        if (selectedFilter == "scores") {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Historique des scores",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Stats Overview Cards Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatsCard(
                        title = "Sessions",
                        value = "$totalSessions",
                        modifier = Modifier.weight(1f)
                    )
                    StatsCard(
                        title = "Moyenne",
                        value = "$averageScore %",
                        modifier = Modifier.weight(1f)
                    )
                    StatsCard(
                        title = "Meilleur",
                        value = "$bestScore %",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (scores.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 640.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aucune session enregistrée",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(scores, key = { it.id }) { record ->
                    ScoreRecordItem(
                        record = record,
                        dateStr = dateFormat.format(Date(record.timestamp)),
                        modifier = Modifier.widthIn(max = 640.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DocumentCardItem(
    doc: QcmDocument,
    attemptsCount: Int,
    onRevise: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("doc_card_${doc.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = doc.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = buildString {
                            append("${doc.questions.size} questions")
                            if (attemptsCount > 0) {
                                append(" • $attemptsCount session${if (attemptsCount > 1) "s" else ""}")
                            }
                            if (doc.bestScore != null) {
                                append(" • Meilleur : ${doc.bestScore}%")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (doc.bestScore != null) {
                    val isGood = doc.bestScore >= 80
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isGood) AppTheme.extendedColors.okBg else AppTheme.extendedColors.badBg
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${doc.bestScore}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isGood) AppTheme.extendedColors.ok else AppTheme.extendedColors.bad
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .testTag("delete_doc_${doc.id}")
                        .heightIn(min = 44.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Supprimer")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onRevise,
                    modifier = Modifier
                        .testTag("revise_doc_${doc.id}")
                        .heightIn(min = 44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Réviser", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ScoreRecordItem(
    record: QuizScoreRecordEntity,
    dateStr: String,
    modifier: Modifier = Modifier
) {
    val isGood = record.scorePercent >= 70
    val scoreBadgeBg = if (isGood) AppTheme.extendedColors.okBg else AppTheme.extendedColors.badBg
    val scoreBadgeFg = if (isGood) AppTheme.extendedColors.ok else AppTheme.extendedColors.bad

    val min = record.elapsedSeconds / 60
    val sec = record.elapsedSeconds % 60
    val timeFormatted = String.format("%d:%02d", min, sec)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("score_record_${record.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = record.documentTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$dateStr • Mode ${record.mode}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(scoreBadgeBg)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${record.scorePercent}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = scoreBadgeFg
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScoreStatChip(label = "${record.correctCount} justes", color = AppTheme.extendedColors.ok, bgColor = AppTheme.extendedColors.okBg)
                ScoreStatChip(label = "${record.incorrectCount} fausses", color = AppTheme.extendedColors.bad, bgColor = AppTheme.extendedColors.badBg)
                if (record.unansweredCount > 0) {
                    ScoreStatChip(label = "${record.unansweredCount} sans rép.", color = MaterialTheme.colorScheme.onSurfaceVariant, bgColor = MaterialTheme.colorScheme.surfaceVariant)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Durée: $timeFormatted",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
