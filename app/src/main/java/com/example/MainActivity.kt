package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.HistoryScreen
import com.example.ui.HomeScreen
import com.example.ui.MainTab
import com.example.ui.QcmViewModel
import com.example.ui.QuizScreen
import com.example.ui.ResultScreen
import com.example.ui.Screen
import com.example.ui.SetupScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val viewModel: QcmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val isDark = uiState.isDarkMode ?: systemDark

            MyApplicationTheme(darkTheme = isDark) {
                MainContent(
                    viewModel = viewModel,
                    isDark = isDark,
                    onToggleDarkTheme = { viewModel.toggleDarkMode(systemDark) }
                )
            }
        }
    }
}

@Composable
fun MainContent(
    viewModel: QcmViewModel,
    isDark: Boolean,
    onToggleDarkTheme: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissMessages()
        }
    }

    LaunchedEffect(uiState.infoMessage) {
        uiState.infoMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissMessages()
        }
    }

    // Handle Android system back gestures appropriately
    BackHandler(enabled = uiState.currentScreen != Screen.Home || uiState.selectedTab != MainTab.HOME) {
        when {
            uiState.currentScreen == Screen.Setup -> viewModel.selectTab(MainTab.HISTORIQUE)
            uiState.currentScreen == Screen.Quiz -> viewModel.returnToSetup()
            uiState.selectedTab != MainTab.HOME -> viewModel.selectTab(MainTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // Floating bottom navigation dock based on Proposition 1 from user mockup
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 2.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                FloatingDockNavBar(
                    selectedTab = uiState.selectedTab,
                    isCurrentScreenHome = uiState.currentScreen == Screen.Home,
                    isDark = isDark,
                    onTabSelected = { tab -> viewModel.selectTab(tab) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                Screen.Home -> {
                    when (uiState.selectedTab) {
                        MainTab.HOME -> {
                            HomeScreen(
                                uiState = uiState,
                                onImportFileUri = { uri -> viewModel.importFromFileUri(context, uri) },
                                onGenerateTestQcm = { viewModel.generateTestQcmDeck() },
                                onDismissMessage = { viewModel.dismissMessages() }
                            )
                        }
                        MainTab.HISTORIQUE -> {
                            HistoryScreen(
                                documents = uiState.documents,
                                scores = uiState.scoreHistory,
                                onOpenSetup = { doc -> viewModel.openSetup(doc) },
                                onDeleteDocument = { id -> viewModel.deleteDocument(id) },
                                onClearAllDocuments = { viewModel.deleteAllDocuments() },
                                onGoToImport = { viewModel.selectTab(MainTab.HOME) }
                            )
                        }
                        MainTab.RESULTATS -> {
                            ResultScreen(
                                uiState = uiState,
                                onRetryMistakes = { viewModel.retryMistakes() },
                                onNewSession = { viewModel.returnToSetup() },
                                onGoToQcm = { viewModel.selectTab(MainTab.HISTORIQUE) },
                                onFilterChange = { filter -> viewModel.setResultFilter(filter) }
                            )
                        }
                    }
                }
                Screen.Setup -> {
                    val doc = uiState.selectedDoc
                    if (doc != null) {
                        SetupScreen(
                            doc = doc,
                            config = uiState.config,
                            onBack = { viewModel.selectTab(MainTab.HISTORIQUE) },
                            onModeChange = { mode -> viewModel.setMode(mode) },
                            onQuestionCountChange = { count -> viewModel.setQuestionCount(count) },
                            onPaceSecondsChange = { sec -> viewModel.setPaceSeconds(sec) },
                            onExamMinutesChange = { min -> viewModel.setExamMinutes(min) },
                            onShuffleQuestionsChange = { shuf -> viewModel.setShuffleQuestions(shuf) },
                            onShuffleOptionsChange = { shuf -> viewModel.setShuffleOptions(shuf) },
                            onStartQuiz = { viewModel.startQuiz() }
                        )
                    } else {
                        viewModel.selectTab(MainTab.HISTORIQUE)
                    }
                }
                Screen.Quiz -> {
                    QuizScreen(
                        uiState = uiState,
                        onSelectOption = { optIndex -> viewModel.selectOption(optIndex) },
                        onGoToQuestion = { qIndex -> viewModel.goToQuestion(qIndex) },
                        onFinish = { viewModel.finishQuiz() }
                    )
                }
            }
        }
    }
}

/**
 * Floating dock navigation bar adhering to Proposition 1 in user mockup:
 * - Rounded pill dock container floating above screen bottom
 * - Active item expands into a capsule containing [ (Icon) (Label) ] with soft tinted background
 * - Inactive items display minimalist outline icons without text
 * - Signature bottom indicator pill bar
 */
@Composable
fun FloatingDockNavBar(
    selectedTab: MainTab,
    isCurrentScreenHome: Boolean,
    isDark: Boolean,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // Dark dock styling as shown in Proposition 1
    val dockBg = if (isDark) Color(0xFF142429) else Color(0xFF12262C)
    val dockBorder = if (isDark) Color(0xFF2A454C) else Color(0xFF1F3840)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .testTag("bottom_nav_bar"),
        shape = RoundedCornerShape(24.dp),
        color = dockBg,
        shadowElevation = 5.dp,
        border = BorderStroke(1.dp, dockBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Home tab (replaces QCM)
                val isHomeSelected = isCurrentScreenHome && selectedTab == MainTab.HOME
                FloatingNavItem(
                    selected = isHomeSelected,
                    onClick = { onTabSelected(MainTab.HOME) },
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    label = "Home",
                    isDark = isDark,
                    testTag = "tab_home",
                    altTestTag = "tab_qcm"
                )

                // 2. Historique tab
                val isHistorySelected = isCurrentScreenHome && selectedTab == MainTab.HISTORIQUE
                FloatingNavItem(
                    selected = isHistorySelected,
                    onClick = { onTabSelected(MainTab.HISTORIQUE) },
                    selectedIcon = Icons.Filled.History,
                    unselectedIcon = Icons.Outlined.History,
                    label = "Historique",
                    isDark = isDark,
                    testTag = "tab_historique"
                )

                // 3. Résultats tab
                val isResultSelected = isCurrentScreenHome && selectedTab == MainTab.RESULTATS
                FloatingNavItem(
                    selected = isResultSelected,
                    onClick = { onTabSelected(MainTab.RESULTATS) },
                    selectedIcon = Icons.Filled.Analytics,
                    unselectedIcon = Icons.Outlined.Analytics,
                    label = "Résultats",
                    isDark = isDark,
                    testTag = "tab_resultats"
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Signature horizontal home indicator line from Proposition 1
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.22f))
            )
        }
    }
}

@Composable
private fun FloatingNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    label: String,
    isDark: Boolean,
    testTag: String,
    altTestTag: String? = null
) {
    // Soft tinted active capsule background matching Proposition 1
    val activePillBg = if (isDark) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.30f)
    }
    val animatedBg by animateColorAsState(
        targetValue = if (selected) activePillBg else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "pill_bg"
    )

    val activeContentColor = if (isDark) MaterialTheme.colorScheme.primary else Color.White
    val inactiveContentColor = Color(0xFF8E9EA2)

    val contentColor by animateColorAsState(
        targetValue = if (selected) activeContentColor else inactiveContentColor,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "pill_content"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(animatedBg)
            .clickable(onClick = onClick)
            .testTag(testTag)
            .then(if (altTestTag != null) Modifier.testTag(altTestTag) else Modifier)
            .padding(horizontal = if (selected) 14.dp else 12.dp, vertical = 5.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = 0.8f,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )

            AnimatedVisibility(
                visible = selected,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                }
            }
        }
    }
}
