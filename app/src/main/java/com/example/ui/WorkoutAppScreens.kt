package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlin.math.sin

// ==========================================
// LIQUID GLASS & VISUAL POLISH UTILITIES
// ==========================================

fun Modifier.glassCard(
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(16.dp),
    borderColor: Color = Color.White.copy(alpha = 0.08f),
    glowColor: Color = Color.Unspecified
): Modifier = this
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Slate800.copy(alpha = 0.50f),
                Slate900.copy(alpha = 0.70f)
            )
        ),
        shape = shape
    )
    .drawBehind {
        if (glowColor != Color.Unspecified) {
            drawCircle(
                color = glowColor.copy(alpha = 0.08f),
                radius = size.maxDimension * 0.45f,
                center = Offset(size.width * 0.5f, size.height * 0.5f)
            )
        }
        
        // Liquid glass sheen diagonal reflection
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.03f),
                    Color.Transparent,
                    Color.White.copy(alpha = 0.01f),
                    Color.Transparent
                ),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height)
            ),
            size = size
        )
    }
    .border(
        width = 1.2.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.22f),
                Color.White.copy(alpha = 0.04f),
                ThemeConfig.primaryColor.copy(alpha = 0.12f),
                Color.White.copy(alpha = 0.04f),
                Color.White.copy(alpha = 0.22f)
            )
        ),
        shape = shape
    )

@Composable
fun AmbientOscillatingBackground(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "ambient")
    
    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse1"
    )
    val pulse2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .drawBehind {
                val width = size.width
                val height = size.height
                
                val radX1 = (width / 2f) + (width / 4f) * sin(Math.toRadians(pulse1.toDouble())).toFloat()
                val radY1 = (height / 2f) + (height / 5f) * sin(Math.toRadians(pulse1.toDouble() * 1.5)).toFloat()
                
                val radX2 = (width / 2f) - (width / 3f) * sin(Math.toRadians(pulse2.toDouble())).toFloat()
                val radY2 = (height / 2f) - (height / 4f) * sin(Math.toRadians(pulse2.toDouble() * 1.2)).toFloat()

                // Draw gorgeous colored ambient fluid spheres under the glass of our views
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ThemeConfig.primaryColor.copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = Offset(radX1, radY1),
                        radius = size.maxDimension * 0.45f
                    ),
                    center = Offset(radX1, radY1),
                    radius = size.maxDimension * 0.45f
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ThemeConfig.secondaryColor.copy(alpha = 0.14f),
                            Color.Transparent
                        ),
                        center = Offset(radX2, radY2),
                        radius = size.maxDimension * 0.5f
                    ),
                    center = Offset(radX2, radY2),
                    radius = size.maxDimension * 0.5f
                )

                // STYLISH WATERMARK VECTOR DUMBBELLS
                // Highly refined, high-Contrast vector shapes adapting to the active theme palette
                val scaleFactor = (width / 1080f).coerceIn(0.7f, 1.5f)
                val dumbbellColor = ThemeConfig.primaryColor.copy(alpha = 0.05f)

                // 1. Primary Dumbbell: Upper right center, tilted at -25 degrees
                withTransform({
                    translate(width * 0.78f, height * 0.32f)
                    rotate(-25f)
                    scale(scaleFactor, scaleFactor)
                }) {
                    // Central Handle Bar
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-150f, -8f),
                        size = androidx.compose.ui.geometry.Size(300f, 16f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    
                    // Left Side Components
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-153f, -10f),
                        size = androidx.compose.ui.geometry.Size(20f, 20f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-128f, -50f),
                        size = androidx.compose.ui.geometry.Size(26f, 100f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-97f, -65f),
                        size = androidx.compose.ui.geometry.Size(32f, 130f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-60f, -30f),
                        size = androidx.compose.ui.geometry.Size(10f, 60f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                    )
                    
                    // Right Side Components
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(50f, -30f),
                        size = androidx.compose.ui.geometry.Size(10f, 60f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(65f, -65f),
                        size = androidx.compose.ui.geometry.Size(32f, 130f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(102f, -50f),
                        size = androidx.compose.ui.geometry.Size(26f, 100f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(133f, -10f),
                        size = androidx.compose.ui.geometry.Size(20f, 20f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f)
                    )
                }

                // 2. Secondary Dumbbell: Bottom left, smaller, tilted at 35 degrees for compositional balance
                withTransform({
                    translate(width * 0.18f, height * 0.78f)
                    rotate(35f)
                    scale(scaleFactor * 0.7f, scaleFactor * 0.7f)
                }) {
                    // Central Handle Bar
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-150f, -8f),
                        size = androidx.compose.ui.geometry.Size(300f, 16f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    
                    // Left Side Components
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-153f, -10f),
                        size = androidx.compose.ui.geometry.Size(20f, 20f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-128f, -50f),
                        size = androidx.compose.ui.geometry.Size(26f, 100f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-97f, -65f),
                        size = androidx.compose.ui.geometry.Size(32f, 130f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(-60f, -30f),
                        size = androidx.compose.ui.geometry.Size(10f, 60f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                    )
                    
                    // Right Side Components
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(50f, -30f),
                        size = androidx.compose.ui.geometry.Size(10f, 60f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(65f, -65f),
                        size = androidx.compose.ui.geometry.Size(32f, 130f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(102f, -50f),
                        size = androidx.compose.ui.geometry.Size(26f, 100f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = dumbbellColor,
                        topLeft = Offset(133f, -10f),
                        size = androidx.compose.ui.geometry.Size(20f, 20f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f)
                    )
                }
            }
    ) {
        content()
    }
}

@Composable
fun ParallaxHeader(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val translationY = remember(lazyListState) {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0) {
                lazyListState.firstVisibleItemScrollOffset * 0.45f
            } else {
                0f
            }
        }
    }
    
    val alpha = remember(lazyListState) {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0) {
                val offset = lazyListState.firstVisibleItemScrollOffset
                (1f - (offset / 380f)).coerceIn(0.1f, 1f)
            } else {
                0.1f
            }
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                this.translationY = translationY.value
                this.alpha = alpha.value
            }
    ) {
        content()
    }
}

// Primary structure that represents the bottom tabs
enum class AppTab(val title: String, val icon: ImageVector) {
    LIBRARY("Biblioteca", Icons.Default.FitnessCenter),
    COACH("Coach IA", Icons.Default.SmartToy),
    NUTRITION("Nutrição", Icons.Default.Restaurant),
    ROUTINE("Meu Treino", Icons.Default.CalendarMonth)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutAppMainScreen(viewModel: WorkoutViewModel) {
    var selectedTab by remember { mutableStateOf(AppTab.ROUTINE) }
    val isWorkoutRunning by viewModel.isWorkoutRunning.collectAsStateWithLifecycle()
    val activeSplit by viewModel.activeSplit.collectAsStateWithLifecycle()
    val showCelebration by viewModel.showWorkoutCelebration.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()

    AmbientOscillatingBackground {
        if (profile.name.isBlank()) {
            // Full Screen Step-by-Step Onboarding Questionnaire
            OnboardingQuizScreen(
                onComplete = { updatedProfile ->
                    viewModel.updateProfile(updatedProfile)
                    viewModel.generateAiWorkout() // Proactively trigger biomechanical plan generation
                }
            )
        } else if (isWorkoutRunning && activeSplit != null) {
            // Full screen active workout runner
            ActiveWorkoutTrackerScreen(
                viewModel = viewModel,
                split = activeSplit!!,
                onDismiss = { viewModel.cancelActiveWorkout() }
            )
        } else {
            // Main Standard Navigation Layout
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Transparent,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Lime500, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = null,
                                        tint = Slate900,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "TREINO INTELIGENTE",
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = Slate50,
                                    fontSize = 18.sp
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Slate900.copy(alpha = 0.55f),
                            titleContentColor = Slate50
                        ),
                        actions = {
                            var showThemeMenu by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { showThemeMenu = !showThemeMenu }) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = "Mudar Paleta",
                                        tint = Lime500,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = showThemeMenu,
                                    onDismissRequest = { showThemeMenu = false },
                                    modifier = Modifier.background(Slate800)
                                ) {
                                    ThemeConfig.palettes.forEach { p ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(12.dp)
                                                            .background(p.primary, CircleShape)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(p.name, color = Slate50, fontSize = 13.sp)
                                                }
                                            },
                                            onClick = {
                                                viewModel.updateSelectedTheme(p.code)
                                                showThemeMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.testTag("app_top_bar")
                    )
                },
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    ) {
                        NavigationBar(
                            containerColor = Color.Transparent,
                            tonalElevation = 0.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .glassCard(shape = RoundedCornerShape(24.dp))
                                .height(68.dp)
                                .testTag("navigation_tab_bar")
                        ) {
                            AppTab.values().forEach { tab ->
                                val isSelected = selectedTab == tab
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { selectedTab = tab },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title,
                                            tint = if (isSelected) Slate900 else Slate400
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.title,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Lime400 else Slate400,
                                            fontSize = 11.sp
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = Lime500,
                                        selectedIconColor = Slate900,
                                        unselectedIconColor = Slate400
                                    ),
                                    modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Crossfade(
                        targetState = selectedTab,
                        animationSpec = tween(durationMillis = 200),
                        label = "tab_fade"
                    ) { tab ->
                        when (tab) {
                            AppTab.LIBRARY -> ExerciseLibraryScreen()
                            AppTab.COACH -> AiCoachScreen(viewModel = viewModel)
                            AppTab.NUTRITION -> NutritionScreen(viewModel = viewModel)
                            AppTab.ROUTINE -> MyRoutineScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }

        // Overlay workout congratulations
        if (showCelebration) {
            val sessions by viewModel.completedSessions.collectAsStateWithLifecycle()
            val latestSession = sessions.firstOrNull()
            if (latestSession != null) {
                WorkoutCelebrationOverlay(
                    session = latestSession,
                    onDismiss = { viewModel.dismissCelebration() }
                )
            }
        }
    }
}

// ==========================================
// 1. LIBRARY SCREEN
// ==========================================
@Composable
fun ExerciseLibraryScreen() {
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var activeDetailExercise by remember { mutableStateOf<Exercise?>(null) }

    val categories = listOf("Todos", "Pernas", "Peito", "Costas", "Ombros", "Glúteos", "Abdominais", "Bíceps", "Tríceps", "Mobilidade")
    val filteredExercises = remember(searchQuery, selectedCategory) {
        CuratedExercises.list.filter {
            val matchesCategory = selectedCategory == "Todos" || it.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = it.name.contains(searchQuery, ignoreCase = true) || 
                               it.primaryMuscle.contains(searchQuery, ignoreCase = true) || 
                               it.tags.any { tag -> tag.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesSearch
        }
    }

    val scrollState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Sticky Search text field in glass style
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Músculo, nome, ou tag (casa, joelho)...", color = Slate400) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate400) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Lime500,
                    unfocusedBorderColor = Slate700.copy(alpha = 0.4f),
                    focusedContainerColor = Slate800.copy(alpha = 0.65f),
                    unfocusedContainerColor = Slate800.copy(alpha = 0.45f),
                    focusedTextColor = Slate50,
                    unfocusedTextColor = Slate50
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("exercise_search_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips Selection Row & Exercise list with Parallax header
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                // Parallax Header Banner Card
                item {
                    ParallaxHeader(lazyListState = scrollState) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .glassCard(glowColor = Lime500, shape = RoundedCornerShape(20.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column {
                                Text(
                                    text = "CONHECIMENTO CIENTÍFICO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Lime400,
                                    letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Biblioteca de Biomecânica",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate50
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Substituições inteligentes e precauções para proteção articular.",
                                    fontSize = 11.sp,
                                    color = Slate400
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(androidx.compose.foundation.rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) Lime500 else Slate800.copy(alpha = 0.5f))
                                    .border(
                                        1.dp,
                                        if (isSelected) Lime500 else Slate700.copy(alpha = 0.4f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CenterRow {
                                    Text(
                                        text = cat,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected) Slate900 else Slate50
                                    )
                                }
                            }
                        }
                    }
                }

                // Header count
                item {
                    Text(
                        text = "Mostrando ${filteredExercises.size} exercícios especializados",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400
                    )
                }

                if (filteredExercises.isEmpty()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = Slate400,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Nenhum exercício encontrado.",
                                color = Slate50,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Tente buscar termos como RDL, casa, ombro ou joelho.",
                                color = Slate400,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }

                items(filteredExercises) { ex ->
                    ExerciseCuratedCard(
                        exercise = ex,
                        onClick = { activeDetailExercise = ex }
                    )
                }
            }
        }

        // Expanded Exercise details Bottom Drawer
        AnimatedVisibility(
            visible = activeDetailExercise != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            activeDetailExercise?.let { ex ->
                ExerciseDetailModal(
                    exercise = ex,
                    onDismiss = { activeDetailExercise = null }
                )
            }
        }
    }
}

@Composable
fun CenterRow(content: @Composable RowScope.() -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, content = content)
}

@Composable
fun ExerciseCuratedCard(exercise: Exercise, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(shape = RoundedCornerShape(16.dp))
            .testTag("exercise_card_${exercise.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exercise dynamic muscle icon tag
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(Slate900.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = Lime500,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Lime500.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = exercise.category.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Lime400,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(Slate700, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = exercise.difficultyLevel,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = exercise.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate50
                )
                Text(
                    text = "Músculo Alvo: ${exercise.primaryMuscle}",
                    fontSize = 12.sp,
                    color = Slate400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Slate400,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ExerciseDetailModal(exercise: Exercise, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("exercise_modal_pane"),
        color = Slate900
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header Action Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .background(Slate800, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Slate50)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "DETALHES DO EXERCÍCIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Lime500,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = exercise.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate50
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                // Interactive Chip Tags Display Row
                item {
                    Text("Tags biomecânicas", fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        exercise.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(Slate800, RoundedCornerShape(12.dp))
                                    .border(1.dp, Slate700, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("#$tag", fontSize = 11.sp, color = Lime400, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                // Muscles and Targets info Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate800),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(1.dp, Slate700, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            DetailAttributeRow("Músculo Principal", exercise.primaryMuscle)
                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Slate700)
                            DetailAttributeRow("Secundários", exercise.secondaryMuscles)
                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Slate700)
                            DetailAttributeRow("Equipamento", exercise.equipmentNeeded)
                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Slate700)
                            DetailAttributeRow("Dificuldade", exercise.difficultyLevel)
                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Slate700)
                            DetailAttributeRow("Objetivo Clínico", exercise.recommendedObjective)
                        }
                    }
                }

                // Step-by-step executions instructions
                item {
                    Text(
                        text = "Como Executar Passo a Passo",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate50
                    )
                }

                itemsIndexed(exercise.executionSteps) { index, step ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Lime500, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Slate900
                            )
                        }
                        Text(
                            text = step,
                            fontSize = 13.sp,
                            color = Slate50,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Posture tips
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Lime500.copy(alpha = 0.05f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(1.dp, Lime500.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Lime500)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Dicas de Postura", color = Lime400, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            exercise.postureTips.forEach { tip ->
                                Text("• $tip", fontSize = 13.sp, color = Slate50, lineHeight = 18.sp, modifier = Modifier.padding(vertical = 2.dp))
                            }
                        }
                    }
                }

                // Common Errors and safety Warnings block
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Crimson400.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(1.dp, Crimson400.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Crimson400)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Evite Erros Comuns & Lesões", color = Crimson400, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            exercise.commonErrors.forEach { err ->
                                Text("• $err", fontSize = 13.sp, color = Slate50, lineHeight = 18.sp, modifier = Modifier.padding(vertical = 2.dp))
                            }
                        }
                    }
                }

                // Variations (Variações fáceis / difíceis)
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate800),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(1.dp, Slate700, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Variações de Intensidade", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(Lime500.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("FÁCIL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Lime400)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(exercise.easierVariation, fontSize = 13.sp, color = Slate50, modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(Crimson400.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("DIFÍCIL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Crimson400)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(exercise.harderVariation, fontSize = 13.sp, color = Slate50, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Substitutions Panel (Adapto)
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate800),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(1.dp, Slate700, RoundedCornerShape(12.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SwapCalls, contentDescription = null, tint = Lime500)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Substitutos Inteligente (Trocas Rápidas)",
                                    color = Lime400,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            exercise.smartSubstitutions.forEach { substitution ->
                                Text("• $substitution", fontSize = 13.sp, color = Slate50, lineHeight = 18.sp, modifier = Modifier.padding(vertical = 3.dp))
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .testTag("close_details_button")
            ) {
                Text("Voltar para Biblioteca", color = Slate50)
            }
        }
    }
}

@Composable
fun DetailAttributeRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = Slate400, fontWeight = FontWeight.Bold)
        Text(value, fontSize = 12.sp, color = Slate50, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End, modifier = Modifier.widthIn(max = 200.dp))
    }
}

@Composable
fun FlowRow(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    // Elegant standard flow simulator using single column/rows safely
    Column(verticalArrangement = verticalArrangement) {
        Row(horizontalArrangement = horizontalArrangement, modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

// ==========================================
// 2. COACH IA & QUESTIONNAIRE SCREEN
// ==========================================
@Composable
fun AiCoachScreen(viewModel: WorkoutViewModel) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val aiState by viewModel.aiState.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()

    var showQuizDialog by remember { mutableStateOf(false) }
    var chatInputText by remember { mutableStateOf("") }
    
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val preDefinedQuestions = listOf(
        "Como trocar o agachamento se meu joelho doer?",
        "Qual o melhor descanso entre séries para hipertrofia?",
        "Estou com dor na lombar, o que devo evitar?",
        "O que comer no pré-treino para ter energia?"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Profile setup quick bar
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .glassCard(shape = RoundedCornerShape(16.dp), glowColor = Lime500)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "COACH INTELIGENTE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Lime500,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Gere treinos ou tire dúvidas",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate50
                            )
                        }
                        Button(
                            onClick = { showQuizDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Lime500),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("open_personalizer_button")
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = Slate900, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ajustar Perfil", color = Slate900, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Display Current Settings Brief
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ProfileSettingBadge("Foco: ${profile.objective}")
                        ProfileSettingBadge("Nível: ${profile.level}")
                        ProfileSettingBadge("Local: ${profile.location}")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.generateAiWorkout() },
                        colors = ButtonDefaults.buttonColors(containerColor = Slate900.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Lime500, RoundedCornerShape(24.dp))
                            .testTag("generate_workout_button"),
                        enabled = aiState !is AiGenerationState.Loading
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Lime500)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (aiState is AiGenerationState.Loading) "Montando biomecânica..." else "Montar Treino Sob Medida (IA)",
                            color = Lime500,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chat Interface
            Text(
                text = "Tire Dúvidas Rápidas com o Coach",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Slate50
            )

            // Suggestions scrollable chips Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .horizontalScroll(androidx.compose.foundation.rememberScrollState())
            ) {
                preDefinedQuestions.forEach { q ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Slate800.copy(alpha = 0.45f))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                            .clickable { 
                                viewModel.sendChatMessage(q)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = q,
                            fontSize = 10.sp,
                            color = Slate50,
                            maxLines = 1,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Chat conversation bubbles log glass container
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .glassCard(shape = RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(chatMessages) { msg ->
                    ChatBubble(msg = msg)
                }

                if (isChatLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Lime500,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Coach Forte está escrevendo...", color = Slate400, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Prompt entry text bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = chatInputText,
                    onValueChange = { chatInputText = it },
                    placeholder = { Text("Fórmula de treino, postura...", color = Slate400, fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Lime500,
                        unfocusedBorderColor = Slate700,
                        focusedContainerColor = Slate800,
                        unfocusedContainerColor = Slate800,
                        focusedTextColor = Slate50,
                        unfocusedTextColor = Slate50
                    ),
                    shape = RoundedCornerShape(24.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (chatInputText.isNotBlank()) {
                                viewModel.sendChatMessage(chatInputText)
                                chatInputText = ""
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_text")
                )

                IconButton(
                    onClick = {
                        if (chatInputText.isNotBlank()) {
                            viewModel.sendChatMessage(chatInputText)
                            chatInputText = ""
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier
                        .background(Lime500, CircleShape)
                        .size(48.dp),
                    enabled = chatInputText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        tint = Slate900
                    )
                }
            }
        }

        // Overlay states panel (Loading & Success)
        if (aiState is AiGenerationState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Slate900.copy(alpha = 0.85f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate800),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(24.dp)
                        .border(1.dp, Lime500, RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Lime500, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Analisando seus parâmetros...",
                            fontWeight = FontWeight.Bold,
                            color = Slate50,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "O Coach Forte está selecionando exercícios biomecanicamente seguros e gerando as adaptações de segurança sob medida para suas limitações físicas.",
                            color = Slate400,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else if (aiState is AiGenerationState.Error) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissAiState() },
                containerColor = Slate800,
                title = { Text("Erro ao Gerar Treino", color = Crimson400, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        (aiState as AiGenerationState.Error).message,
                        color = Slate50
                    )
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissAiState() }) {
                        Text("Ok", color = Lime500, fontWeight = FontWeight.Bold)
                    }
                }
            )
        } else if (aiState is AiGenerationState.Success) {
            AIWorkoutSuccessDialog(
                plan = (aiState as AiGenerationState.Success).plan,
                onDismiss = { viewModel.dismissAiState() }
            )
        }

        // Questionnaire Setup Dialog (Post-onboarding modifications)
        if (showQuizDialog) {
            PersonalizerQuizDialog(
                current = profile,
                viewModel = viewModel,
                onSave = { updated ->
                    viewModel.updateProfile(updated)
                    showQuizDialog = false
                },
                onDismiss = { showQuizDialog = false }
            )
        }
    }
}

@Composable
fun ProfileSettingBadge(text: String) {
    Box(
        modifier = Modifier
            .background(Slate700, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = Slate400,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val isCoach = msg.sender == "coach"
    val align = if (isCoach) Alignment.Start else Alignment.End
    val bg = if (isCoach) Slate900 else Lime500
    val textCol = if (isCoach) Slate50 else Slate900
    val labelCol = if (isCoach) Lime500 else Slate700
    val labelText = if (isCoach) "COACH FORTE" else "VOCÊ"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = labelText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = labelCol,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "• ${msg.timeString}",
                fontSize = 9.sp,
                color = Slate400
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (isCoach) 2.dp else 12.dp,
                        bottomEnd = if (isCoach) 12.dp else 2.dp
                    )
                )
                .background(bg)
                .border(
                    1.dp,
                    if (isCoach) Slate700 else Lime500,
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (isCoach) 2.dp else 12.dp,
                        bottomEnd = if (isCoach) 12.dp else 2.dp
                    )
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = msg.content,
                fontSize = 13.sp,
                color = textCol,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun AIWorkoutSuccessDialog(plan: WorkoutPlan, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Slate800,
        icon = { Icon(Icons.Default.Celebration, contentDescription = null, tint = Lime500, modifier = Modifier.size(36.dp)) },
        title = { Text("Treino Gerado com Sucesso!", color = Slate50, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "O seu treino \"${plan.title}\" foi importado e salvo como sua Rotina Ativa!",
                    color = Slate50,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Acesse a aba \"Meu Treino\" na barra de navegação para visualizar as divisões de séries e iniciar a execução!",
                    color = Slate400,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Lime500)
            ) {
                Text("Ótimo! Ver meu treino", color = Slate900, fontWeight = FontWeight.Bold)
            }
        }
    )
}

// ==========================================
// 3. ONBOARDING QUIZ SCREEN (FIRST-TIME USER)
// ==========================================
@Composable
fun OnboardingQuizScreen(
    onComplete: (UserProfile) -> Unit
) {
    var step by remember { mutableStateOf(1) }
    
    // Core parameters to collect
    var name by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("") }
    var weightStr by remember { mutableStateOf("") }
    var heightStr by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("Hipertrofia") }
    var level by remember { mutableStateOf("Iniciante") }
    var location by remember { mutableStateOf("Ambos") }
    
    // Checkbox equipment multi-selection
    val equipmentsList = listOf("Halteres", "Barra", "Máquinas articuladas", "Peso Corporal", "Polias e Cabos", "Bandas Elásticas")
    val selectedEquipments = remember { mutableStateListOf("Halteres", "Peso Corporal") }
    
    var frequency by remember { mutableStateOf(3) }
    var durationMinutes by remember { mutableStateOf(45) }
    var limitations by remember { mutableStateOf("Nenhuma") }
    var preferences by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(androidx.compose.foundation.rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Visual Header Logo Badge
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Lime500, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Slate900, modifier = Modifier.size(36.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "TREINO INTELIGENTE",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Slate50,
                letterSpacing = 2.sp
            )
            Text(
                "Biomecânica & Segurança Articular Personalizada",
                fontSize = 12.sp,
                color = Lime400,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Step Indicator Cards
            Card(
                colors = CardDefaults.cardColors(containerColor = Slate800),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate700, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Title matching step
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when(step) {
                                1 -> "Passo 1: Identificação"
                                2 -> "Passo 2: Objetivo & Nível"
                                3 -> "Passo 3: Local & Equipamento"
                                else -> "Passo 4: Segurança & Rotina"
                            },
                            color = Lime400,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Text("Etapa $step de 4", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when(step) {
                        1 -> {
                            // Identification Info
                            Text("Qual o seu nome?", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                placeholder = { Text("Ex: Matheus", color = Slate400) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Lime500,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Slate50,
                                    unfocusedTextColor = Slate50
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .testTag("onboarding_name_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("Idade (anos)", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            OutlinedTextField(
                                value = ageStr,
                                onValueChange = { ageStr = it.filter { ch -> ch.isDigit() } },
                                placeholder = { Text("Ex: 25", color = Slate400) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Lime500,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Slate50,
                                    unfocusedTextColor = Slate50
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Peso (kg)", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    OutlinedTextField(
                                        value = weightStr,
                                        onValueChange = { weightStr = it },
                                        placeholder = { Text("Ex: 75", color = Slate400) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Lime500,
                                            unfocusedBorderColor = Slate700,
                                            focusedTextColor = Slate50,
                                            unfocusedTextColor = Slate50
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Altura (cm)", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    OutlinedTextField(
                                        value = heightStr,
                                        onValueChange = { heightStr = it },
                                        placeholder = { Text("Ex: 175", color = Slate400) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Lime500,
                                            unfocusedBorderColor = Slate700,
                                            focusedTextColor = Slate50,
                                            unfocusedTextColor = Slate50
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                        2 -> {
                            // Objectives & Levels selection
                            Text("Seu objetivo principal hoje:", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(
                                    "Hipertrofia" to "Hipertrofia (Ganho de Massa)",
                                    "Emagrecimento" to "Emagrecimento & Definição",
                                    "Força" to "Força (Aumento de Carga)",
                                    "Resistência" to "Resistência & Cardio",
                                    "Mobilidade" to "Mobilidade & Flexibilidade",
                                    "Saúde Geral" to "Saúde Geral & Longevidade"
                                ).forEach { (key, label) ->
                                    SettingSelectableRow(
                                        selected = objective == key,
                                        title = label,
                                        onClick = { objective = key }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text("Nível de treinamento na musculação:", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Iniciante", "Intermediário", "Avançado").forEach { lvl ->
                                    val active = level == lvl
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (active) Lime500 else Slate900)
                                            .border(1.dp, if (active) Lime500 else Slate700, RoundedCornerShape(8.dp))
                                            .clickable { level = lvl }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(lvl, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Slate900 else Slate50)
                                    }
                                }
                            }
                        }
                        3 -> {
                            // Logistics and equipments
                            Text("Local de Treino preferido:", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(
                                    "Casa" to "Casa",
                                    "Academia" to "Academia",
                                    "Ambos" to "Ambos"
                                ).forEach { (key, label) ->
                                    val active = location == key
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (active) Lime500 else Slate900)
                                            .border(1.dp, if (active) Lime500 else Slate700, RoundedCornerShape(8.dp))
                                            .clickable { location = key }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Slate900 else Slate50)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text("Equipamentos que você possui acessíveis:", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                equipmentsList.forEach { eq ->
                                    val isChecked = selectedEquipments.contains(eq)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isChecked) Lime500.copy(alpha = 0.08f) else Slate900)
                                            .border(1.dp, if (isChecked) Lime500 else Slate700, RoundedCornerShape(8.dp))
                                            .clickable {
                                                if (isChecked) selectedEquipments.remove(eq) else selectedEquipments.add(eq)
                                            }
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = {
                                                if (isChecked) selectedEquipments.remove(eq) else selectedEquipments.add(eq)
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = Lime500, uncheckedColor = Slate600)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(eq, fontSize = 12.sp, color = if (isChecked) Lime400 else Slate50, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                        else -> {
                            // Frequencies, durations & limitations
                            Text("Quantas vezes você quer treinar na semana?", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(2, 3, 4, 5, 6).forEach { days ->
                                    val active = frequency == days
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (active) Lime500 else Slate900)
                                            .border(1.dp, if (active) Lime500 else Slate700, RoundedCornerShape(8.dp))
                                            .clickable { frequency = days }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("$days d", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Slate900 else Slate50)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text("Duração disponível para cada sessão de treino:", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(20, 30, 45, 60, 90).forEach { mins ->
                                    val active = durationMinutes == mins
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (active) Lime500 else Slate900)
                                            .border(1.dp, if (active) Lime500 else Slate700, RoundedCornerShape(8.dp))
                                            .clickable { durationMinutes = mins }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("${mins}m", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Slate900 else Slate50)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text("Você possui alguma dor articular ou limitação?", color = Amber400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(
                                    "Nenhuma" to "Sem restrições (Treinar tudo)",
                                    "Lombar" to "Especial cuidado com LOMBAR",
                                    "Joelho" to "Evitar excesso nos JOELHOS",
                                    "Ombro" to "Evitar excesso com OMBRO",
                                    "Outros" to "Outros (Mais restrições)"
                                ).forEach { (key, label) ->
                                    val active = limitations == key
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (active) Amber400.copy(alpha = 0.12f) else Slate900)
                                            .border(1.dp, if (active) Amber400 else Slate700, RoundedCornerShape(8.dp))
                                            .clickable { limitations = key }
                                            .padding(10.dp)
                                    ) {
                                        CenterRow {
                                            RadioButton(
                                                selected = active,
                                                onClick = { limitations = key },
                                                colors = RadioButtonDefaults.colors(selectedColor = Amber400, unselectedColor = Slate600)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(label, fontSize = 11.sp, color = if (active) Amber400 else Slate50, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text("Preferências ou focos extras (Ex: focar costas, glúteos...)", color = Slate400, fontSize = 12.sp)
                            OutlinedTextField(
                                value = preferences,
                                onValueChange = { preferences = it },
                                placeholder = { Text("Quero focar em core, evitar carregar pesos acima da cabeça...", color = Slate600, fontSize = 12.sp) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Lime500,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Slate50,
                                    unfocusedTextColor = Slate50
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action controllers back/forward
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (step > 1) {
                            TextButton(
                                onClick = { step-- },
                                colors = ButtonDefaults.textButtonColors(contentColor = Slate400)
                            ) {
                                Text("Voltar", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Spacer(modifier = Modifier.width(60.dp))
                        }

                        Button(
                            onClick = {
                                if (step < 4) {
                                    if (step == 1 && name.isBlank()) {
                                        // Simple validation check before proceeding
                                        name = "Iniciante Forte"
                                    }
                                    step++
                                } else {
                                    // Submit entire model
                                    val calculatedAge = ageStr.toIntOrNull() ?: 25
                                    val calculatedWeight = weightStr.toDoubleOrNull() ?: 70.0
                                    val calculatedHeight = heightStr.toDoubleOrNull() ?: 170.0
                                    
                                    val finalProfile = UserProfile(
                                        name = name.ifBlank { "Atleta Forte" },
                                        age = calculatedAge,
                                        weight = calculatedWeight,
                                        height = calculatedHeight,
                                        objective = objective,
                                        level = level,
                                        location = location,
                                        equipments = selectedEquipments.toList(),
                                        frequency = frequency,
                                        durationMinutes = durationMinutes,
                                        limitations = limitations,
                                        preferences = preferences
                                    )
                                    onComplete(finalProfile)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Lime500),
                            modifier = Modifier.testTag("onboarding_submit_next_button")
                        ) {
                            Text(
                                text = if (step == 4) "Criar Meu Perfil" else "Avançar",
                                color = Slate900,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================
@Composable
fun MyRoutineScreen(viewModel: WorkoutViewModel) {
    val selectedPlanType by viewModel.selectedPlanType.collectAsStateWithLifecycle()
    val aiCoachPlan by viewModel.aiCoachPlan.collectAsStateWithLifecycle()
    val userCustomPlan by viewModel.userCustomPlan.collectAsStateWithLifecycle()
    val completedSessions by viewModel.completedSessions.collectAsStateWithLifecycle()

    val activePlan = if (selectedPlanType == "AI") aiCoachPlan else userCustomPlan
    
    // Tracks state to show explanations of Curated Exercises
    var exerciseDetailToShow by remember { mutableStateOf<Exercise?>(null) }

    // Dialog state controllers for custom adjustments
    var showAddExerciseDialogBySplitName by remember { mutableStateOf<String?>(null) }
    var customExName by remember { mutableStateOf("") }
    var customExSets by remember { mutableStateOf("3") }
    var customExReps by remember { mutableStateOf("10 a 12 reps") }
    var customExRest by remember { mutableStateOf("60") }
    var customExNotes by remember { mutableStateOf("Foco em cadência lenta.") }

    var showAddSplitDialog by remember { mutableStateOf(false) }
    var customSplitName by remember { mutableStateOf("") }
    var customSplitDesc by remember { mutableStateOf("") }

    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 96.dp, top = 8.dp)
    ) {
        // Active Program details header in full Parallax Glass Glow card
        item {
            ParallaxHeader(lazyListState = scrollState) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassCard(
                            glowColor = if (selectedPlanType == "AI") Lime500 else Color(0xFF38BDF8),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "GERENCIADOR ATIVO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = if (selectedPlanType == "AI") Lime400 else Color(0xFF38BDF8),
                                letterSpacing = 1.sp
                            )
                            
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (selectedPlanType == "AI") Lime500.copy(alpha = 0.15f) else Color(0xFF38BDF8).copy(alpha = 0.15f), 
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    if (selectedPlanType == "AI") "IA ATIVA" else "MEU TREINO",
                                    color = if (selectedPlanType == "AI") Lime400 else Color(0xFF38BDF8),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = activePlan.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate50
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (selectedPlanType == "AI") 
                                "Plano biomecânico focado em máxima hipertrofia com proteção articular ativa." 
                                else "Crie divisões e monte seu cronograma com total flexibilidade.",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }
                }
            }
        }

        // TAB SELECTOR SWITCH with glass border
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(shape = RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val isAi = selectedPlanType == "AI"
                Button(
                    onClick = { viewModel.selectPlanType("AI") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAi) Lime500 else Color.Transparent,
                        contentColor = if (isAi) Slate900 else Slate400
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(38.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.SmartToy, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Prescritor IA", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { viewModel.selectPlanType("CUSTOM") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isAi) Color(0xFF0EA5E9) else Color.Transparent,
                        contentColor = if (!isAi) Slate900 else Slate400
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(38.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Meu Treino Custom", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Option to copy AI plan to Custom slot
        if (selectedPlanType == "AI") {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().glassCard(shape = RoundedCornerShape(12.dp), glowColor = Lime500)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Text("Gostou desta prescrição da IA?", color = Slate50, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Copie este treino para ajustá-lo, adicionar ou deletar seus exercícios livremente!", color = Slate400, fontSize = 10.sp)
                        }
                        Button(
                            onClick = {
                                viewModel.saveUserCustomPlan(aiCoachPlan.copy(title = "Cópia Editável do Coach IA"))
                                viewModel.selectPlanType("CUSTOM")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Lime500),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Slate900, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ajustar Treino", color = Slate900, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        } else {
            // Options inside Custom mode to add new split division
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Divisões Atuais do Treino", color = Slate400, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Button(
                        onClick = { showAddSplitDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0EA5E9)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Slate900, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Nova Divisão (ABC...)", color = Slate900, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Coach Bio Note for AI mode only
        if (selectedPlanType == "AI") {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassCard(shape = RoundedCornerShape(16.dp), glowColor = Lime500)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Lime500.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SupportAgent, contentDescription = null, tint = Lime500, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Análise e Coach Nutricional IA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Slate50
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = activePlan.coachNotes,
                            fontSize = 12.sp,
                            color = Slate400,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // RENDER EVERY WORKOUT SPLIT DIVISION SEPARATELY
        if (activePlan.splits.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().glassCard(shape = RoundedCornerShape(12.dp))
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "Você não possui nenhuma divisão de treinos ativa. Clique no botão de nova divisão acima ou peça ajuda ao Coach IA!",
                            color = Slate400,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            activePlan.splits.forEachIndexed { sIndex, split ->
                item {
                    val glowCol = if (selectedPlanType == "AI") Lime500 else Color(0xFF0EA5E9)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassCard(shape = RoundedCornerShape(16.dp), glowColor = glowCol)
                            .testTag("split_card_$sIndex")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Split metadata header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = split.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate50
                                    )
                                    Text(
                                        text = split.description.ifBlank { "Personalização de volume e cadência biomecânica." },
                                        fontSize = 11.sp,
                                        color = Slate400,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Slate900, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                 ) {
                                    Text("${split.exercises.size} Exercícios", fontSize = 10.sp, color = if (selectedPlanType == "AI") Lime400 else Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Render exercises list
                            split.exercises.forEachIndexed { num, ex ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(Slate900.copy(alpha = 0.55f), RoundedCornerShape(12.dp))
                                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${num + 1}. ${ex.name}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate50,
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            if (selectedPlanType == "CUSTOM") {
                                                // Inline Sets Series Adjustments
                                                IconButton(
                                                    onClick = {
                                                        val updatedSplits = userCustomPlan.splits.map { currentSplit ->
                                                            if (currentSplit.name == split.name) {
                                                                val currentExs = currentSplit.exercises.toMutableList()
                                                                val itemEx = currentExs[num]
                                                                currentExs[num] = itemEx.copy(sets = (itemEx.sets - 1).coerceAtLeast(1))
                                                                currentSplit.copy(exercises = currentExs)
                                                            } else {
                                                                currentSplit
                                                            }
                                                        }
                                                        viewModel.saveUserCustomPlan(userCustomPlan.copy(splits = updatedSplits))
                                                    },
                                                    modifier = Modifier.size(22.dp)
                                                ) {
                                                    Icon(Icons.Default.Remove, contentDescription = "Diminuir Séries", tint = Slate400, modifier = Modifier.size(12.dp))
                                                }
                                                
                                                Text(
                                                    text = "${ex.sets}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF38BDF8)
                                                )

                                                IconButton(
                                                    onClick = {
                                                        val updatedSplits = userCustomPlan.splits.map { currentSplit ->
                                                            if (currentSplit.name == split.name) {
                                                                val currentExs = currentSplit.exercises.toMutableList()
                                                                val itemEx = currentExs[num]
                                                                currentExs[num] = itemEx.copy(sets = itemEx.sets + 1)
                                                                currentSplit.copy(exercises = currentExs)
                                                            } else {
                                                                currentSplit
                                                            }
                                                        }
                                                        viewModel.saveUserCustomPlan(userCustomPlan.copy(splits = updatedSplits))
                                                    },
                                                    modifier = Modifier.size(22.dp)
                                                ) {
                                                    Icon(Icons.Default.Add, contentDescription = "Aumentar Séries", tint = Slate400, modifier = Modifier.size(12.dp))
                                                }
                                                
                                                Text(" s", fontSize = 11.sp, color = Slate400)
                                                
                                                Spacer(modifier = Modifier.width(6.dp))
                                                
                                                Text(
                                                    text = ex.reps,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Slate300
                                                )
                                            } else {
                                                Text(
                                                    text = "${ex.sets}s × ${ex.reps} (${ex.restSeconds}s rec)",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Lime400
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // Explain button
                                        Button(
                                            onClick = {
                                                val databaseMatch = CuratedExercises.list.find { it.id == ex.id || it.name.contains(ex.name, ignoreCase = true) }
                                                if (databaseMatch != null) {
                                                    exerciseDetailToShow = databaseMatch
                                                } else {
                                                    exerciseDetailToShow = Exercise(
                                                        id = ex.id,
                                                        name = ex.name,
                                                        category = "Geral",
                                                        primaryMuscle = "Foco Geral",
                                                        secondaryMuscles = "Core, Estabilidade",
                                                        equipmentNeeded = "Halteres/Peso Corporal",
                                                        difficultyLevel = "Iniciante",
                                                        recommendedObjective = "Consistência física",
                                                        executionSteps = listOf("Realizar o movimento com foco e amplitude confortável.", "Mantenha postura firme."),
                                                        commonErrors = listOf("Fazer de forma rápida sem cadência."),
                                                        postureTips = listOf("Contraia o abdômen."),
                                                        smartSubstitutions = listOf("Tente outro exercício similar."),
                                                        easierVariation = "Amplitude reduzida confortável",
                                                        harderVariation = "Aumente as repetições de forma lenta",
                                                        safetyWarnings = listOf("Se doer, pare e mude."),
                                                        tags = listOf("geral"),
                                                        primaryFocus = "Geral",
                                                        iconDescription = "Pernas"
                                                    )
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.weight(1f).height(28.dp)
                                        ) {
                                            Icon(Icons.Default.Info, contentDescription = null, tint = Slate50, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Explicação", color = Slate50, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }

                                        if (selectedPlanType == "CUSTOM") {
                                            // Delete custom exercise button
                                            Button(
                                                onClick = {
                                                    val updatedSplits = userCustomPlan.splits.map { currentSplit ->
                                                        if (currentSplit.name == split.name) {
                                                            val currentExs = currentSplit.exercises.toMutableList()
                                                            if (num in currentExs.indices) {
                                                                currentExs.removeAt(num)
                                                            }
                                                            currentSplit.copy(exercises = currentExs)
                                                        } else {
                                                            currentSplit
                                                        }
                                                    }
                                                    viewModel.saveUserCustomPlan(userCustomPlan.copy(splits = updatedSplits))
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.15f)),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                modifier = Modifier.weight(1f).height(28.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = null, tint = Crimson400, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Excluir", color = Crimson400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        } else {
                                            // Swap exercise button
                                            Button(
                                                onClick = {
                                                    viewModel.swapPlanExercise(split.name, num)
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                modifier = Modifier.weight(1f).height(28.dp)
                                            ) {
                                                Icon(Icons.Default.SwapCalls, contentDescription = null, tint = Lime400, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Trocar Exercício", color = Lime400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (selectedPlanType == "CUSTOM") {
                                    // Button to insert exercise into specific split
                                    Button(
                                        onClick = { showAddExerciseDialogBySplitName = split.name },
                                        colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).height(38.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Adicionar Exercício", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // Trigger Workout Routine Tracker Button
                                Button(
                                    onClick = { viewModel.startWorkout(split) },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedPlanType == "AI") Lime500 else Color(0xFF0EA5E9)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1.2f).height(38.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Slate900, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Iniciar Treino", color = Slate900, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section log history of workouts accomplished
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Histórico de Treinos",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate50
                )
                if (completedSessions.isNotEmpty()) {
                    Text(
                        text = "Limpar Tudo",
                        color = Crimson400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.clearHistory() }
                            .padding(4.dp)
                            .testTag("clear_history_log_button")
                    )
                }
            }
        }

        if (completedSessions.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate800),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Sua jornada começa agora!",
                            color = Slate50,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            "Ao concluir seu primeiro treino do dia, o resumo de duração, séries e exercícios será listado aqui.",
                            color = Slate400,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(completedSessions) { session ->
                CompletedSessionRow(session = session)
            }
        }
    }

    // Modal Details overlay support in routine display pre-workout
    AnimatedVisibility(
        visible = exerciseDetailToShow != null,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = Modifier.fillMaxSize()
    ) {
        exerciseDetailToShow?.let { ex ->
            Box(modifier = Modifier.fillMaxSize().background(Slate900)) {
                ExerciseDetailModal(
                    exercise = ex,
                    onDismiss = { exerciseDetailToShow = null }
                )
            }
        }
    }

    // Modal Dialog to ADD exercise to a CUSTOM split
    showAddExerciseDialogBySplitName?.let { targetSplitName ->
        AlertDialog(
            onDismissRequest = { showAddExerciseDialogBySplitName = null },
            title = { Text("Adicionar Novo Exercício", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            containerColor = Slate850,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Destinado a: $targetSplitName", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = customExName,
                        onValueChange = { customExName = it },
                        label = { Text("Nome do Exercício") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8), focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customExSets,
                        onValueChange = { customExSets = it },
                        label = { Text("Séries (ex: 3 ou 4)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8), focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customExReps,
                        onValueChange = { customExReps = it },
                        label = { Text("Repetições (ex: 10 a 12 reps)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8), focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customExRest,
                        onValueChange = { customExRest = it },
                        label = { Text("Tempo de Descanso em seg. (ex: 60)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8), focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customExNotes,
                        onValueChange = { customExNotes = it },
                        label = { Text("Notas de Execução") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8), focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val numSets = customExSets.toIntOrNull() ?: 3
                        val restVal = customExRest.toIntOrNull() ?: 60
                        if (customExName.isNotBlank()) {
                            val updatedSplits = userCustomPlan.splits.map { split ->
                                if (split.name == targetSplitName) {
                                    val currentExs = split.exercises.toMutableList()
                                    currentExs.add(
                                        WorkoutExercise(
                                            id = customExName.lowercase().replace(" ", "_"),
                                            name = customExName,
                                            sets = numSets,
                                            reps = customExReps,
                                            restSeconds = restVal,
                                            notes = customExNotes
                                        )
                                    )
                                    split.copy(exercises = currentExs)
                                } else {
                                    split
                                }
                            }
                            viewModel.saveUserCustomPlan(userCustomPlan.copy(splits = updatedSplits))
                            
                            // Reset state & close
                            customExName = ""
                            customExSets = "3"
                            customExReps = "10 a 12 reps"
                            customExRest = "60"
                            customExNotes = "Foco em cadência lenta."
                            showAddExerciseDialogBySplitName = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0EA5E9))
                ) {
                    Text("Adicionar", color = Slate900, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddExerciseDialogBySplitName = null }) {
                    Text("Cancelar", color = Slate400)
                }
            }
        )
    }

    // Modal Dialog to ADD split division to a CUSTOM plan
    if (showAddSplitDialog) {
        AlertDialog(
            onDismissRequest = { showAddSplitDialog = false },
            title = { Text("Criar Nova Divisão de Treino", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            containerColor = Slate850,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = customSplitName,
                        onValueChange = { customSplitName = it },
                        label = { Text("Nome (ex: Treino D: Pernas Foco Posterior)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8), focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customSplitDesc,
                        onValueChange = { customSplitDesc = it },
                        label = { Text("Descrição do Foco Muscular") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF38BDF8), focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customSplitName.isNotBlank()) {
                            val currentSplits = userCustomPlan.splits.toMutableList()
                            currentSplits.add(
                                WorkoutSplit(
                                    name = customSplitName,
                                    description = customSplitDesc,
                                    exercises = emptyList()
                                )
                            )
                            viewModel.saveUserCustomPlan(userCustomPlan.copy(splits = currentSplits))
                            
                            // Reset state & close
                            customSplitName = ""
                            customSplitDesc = ""
                            showAddSplitDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0EA5E9))
                ) {
                    Text("Criar", color = Slate900, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSplitDialog = false }) {
                    Text("Cancelar", color = Slate400)
                }
            }
        )
    }
}

@Composable
fun CompletedSessionRow(session: CompletedSession) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Lime500, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Slate900, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.splitName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Slate50
                )
                Text(
                    text = "Programa: ${session.routineTitle}",
                    fontSize = 11.sp,
                    color = Slate400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = Lime400, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = formatSecondsToMinutes(session.durationSeconds),
                            fontSize = 11.sp,
                            color = Slate50,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Lime400, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${session.exercisesCompleted}/${session.totalExercises} Executados",
                            fontSize = 11.sp,
                            color = Slate50,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = session.dateString,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400
            )
        }
    }
}

// ==========================================
// 5. ACTIVE WORKOUT SESSION TRACKER OVERLAY
// ==========================================
@Composable
fun ActiveWorkoutTrackerScreen(
    viewModel: WorkoutViewModel,
    split: WorkoutSplit,
    onDismiss: () -> Unit
) {
    val timerSeconds by viewModel.workoutTimerSeconds.collectAsStateWithLifecycle()
    val checkedSets by viewModel.checkedSets.collectAsStateWithLifecycle()
    val setLoads by viewModel.setLoads.collectAsStateWithLifecycle()
    val setReps by viewModel.setReps.collectAsStateWithLifecycle()
    val exerciseRatings by viewModel.exerciseRatings.collectAsStateWithLifecycle()
    val exercisePain by viewModel.exercisePain.collectAsStateWithLifecycle()
    val restRemaining by viewModel.restSecondsRemaining.collectAsStateWithLifecycle()
    val isRestActive by viewModel.isRestTimerActive.collectAsStateWithLifecycle()

    var showCancelAlert by remember { mutableStateOf(false) }
    var exerciseDetailToShow by remember { mutableStateOf<Exercise?>(null) }
    
    // Safety, pain and smart substitutions states
    var showPainDialogIndex by remember { mutableStateOf<Int?>(null) }
    var showPainDetailTextForIndex by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var exerciseToSubstituteIndex by remember { mutableStateOf<Int?>(null) }
    var substitutionFilterCriterionState by remember { mutableStateOf("Mesmo Músculo") }

    Surface(
        color = Slate900,
        modifier = Modifier
            .fillMaxSize()
            .testTag("active_workout_runner")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header timer and cancel trigger
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showCancelAlert = true },
                    modifier = Modifier
                        .background(Slate800, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cancelar Treino", tint = Crimson400)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "DURAÇÃO ATIVA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Slate400,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = formatSecondsToMinutes(timerSeconds),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Lime500,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.testTag("active_workout_timer")
                    )
                }

                Button(
                    onClick = { viewModel.completeWorkout() },
                    colors = ButtonDefaults.buttonColors(containerColor = Lime500),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("complete_active_workout_button")
                ) {
                    Text("Concluir", color = Slate900, fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = split.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate50
                )
                Text(
                    text = "Aperte o checkbox em cada série para marcar o progresso e o cronômetro inteligente abrirá.",
                    fontSize = 12.sp,
                    color = Slate400,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Exercise tracking list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                itemsIndexed(split.exercises) { exIndex, ex ->
                    val ratingSelected = exerciseRatings[exIndex] ?: ""
                    val painSelected = exercisePain[exIndex] ?: ""

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate800),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.border(1.dp, Slate700, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Exercise name and details
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ex.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Lime400,
                                    modifier = Modifier.weight(1f)
                                )
                                Box(
                                    modifier = Modifier
                                        .background(Slate900, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        "${ex.sets} séries × ${ex.reps}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate50
                                    )
                                }
                            }

                            // Interactive Quick Actions row inside Active Tracker (Ver Explicação + Trocar Exercício!)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val databaseMatch = CuratedExercises.list.find { it.id == ex.id || it.name.contains(ex.name, ignoreCase = true) }
                                        exerciseDetailToShow = databaseMatch ?: Exercise(
                                            id = ex.id,
                                            name = ex.name,
                                            category = "Treino",
                                            primaryMuscle = "Geral",
                                            secondaryMuscles = "Core",
                                            equipmentNeeded = "Acessórios",
                                            difficultyLevel = "Iniciante",
                                            recommendedObjective = "Consistência",
                                            executionSteps = listOf("Repita as execuções de forma consistente."),
                                            commonErrors = listOf("Evite pressa."),
                                            postureTips = listOf("Mantenha a postura."),
                                            smartSubstitutions = listOf(),
                                            easierVariation = "Menor carga",
                                            harderVariation = "Maior carga e cadência",
                                            safetyWarnings = listOf(),
                                            tags = listOf("geral"),
                                            primaryFocus = "Treino",
                                            iconDescription = "Pernas"
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.weight(1f).height(28.dp)
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = Slate50, modifier = Modifier.size(10.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Explicação", color = Slate50, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        exerciseToSubstituteIndex = exIndex
                                        substitutionFilterCriterionState = "Mesmo Músculo"
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.weight(1f).height(28.dp)
                                ) {
                                    Icon(Icons.Default.SwapCalls, contentDescription = null, tint = Lime400, modifier = Modifier.size(10.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Trocar Exercício", color = Lime400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Personalized dynamic notes
                            if (ex.notes.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .background(Lime500.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                                        .border(1.dp, Lime500.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(
                                            Icons.Default.SupportAgent,
                                            contentDescription = null,
                                            tint = Lime500,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = ex.notes,
                                            fontSize = 11.sp,
                                            color = Slate50,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Interactive input row for each set
                            for (setIndex in 0 until ex.sets) {
                                val setKey = "${exIndex}_$setIndex"
                                val isChecked = checkedSets[setKey] ?: false
                                val currentLoad = setLoads[setKey] ?: ""
                                val currentRepGoal = setReps[setKey] ?: ""

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isChecked) Lime500.copy(alpha = 0.05f) else Slate900)
                                        .border(
                                            1.dp,
                                            if (isChecked) Lime500.copy(alpha = 0.3f) else Slate700,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "SÉRIE ${setIndex + 1}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isChecked) Lime400 else Slate50,
                                        modifier = Modifier.weight(1f)
                                    )

                                    // Carga (kg) input
                                    CompactInputField(
                                        value = currentLoad,
                                        onValueChange = { viewModel.updateSetLoad(exIndex, setIndex, it) },
                                        placeholder = "Carga",
                                        widthDp = 64
                                    )

                                    Text("kg", fontSize = 11.sp, color = Slate400)

                                    // Reps input
                                    CompactInputField(
                                        value = currentRepGoal,
                                        onValueChange = { viewModel.updateSetReps(exIndex, setIndex, it) },
                                        placeholder = "Reps",
                                        widthDp = 50
                                    )

                                    // Check indicator
                                    IconButton(
                                        onClick = { viewModel.toggleSetCheck(exIndex, setIndex, ex.restSeconds) },
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(
                                                if (isChecked) Lime500 else Slate850,
                                                CircleShape
                                            )
                                            .border(
                                                1.dp,
                                                if (isChecked) Lime500 else Slate600,
                                                CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Confirmar série",
                                            tint = if (isChecked) Slate900 else Slate400,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }

                            // Feedback rating & pain reporting footer inside card
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Pain reporting badge
                                Column(modifier = Modifier.weight(1.5f)) {
                                    if (painSelected.isEmpty()) {
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .clickable {
                                                    showPainDialogIndex = exIndex
                                                }
                                                .background(Slate900)
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = null,
                                                tint = Slate400,
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Sinto dor", fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .clickable {
                                                    showPainDialogIndex = exIndex
                                                }
                                                .background(Crimson400.copy(alpha = 0.15f))
                                                .border(1.dp, Crimson400, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = null,
                                                tint = Crimson400,
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Dor: $painSelected", fontSize = 11.sp, color = Crimson400, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // Effort segment difficulty selector
                                Row(
                                    modifier = Modifier
                                        .background(Slate900, RoundedCornerShape(8.dp))
                                        .border(1.dp, Slate700, RoundedCornerShape(8.dp))
                                        .padding(2.dp),
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    listOf("Fácil", "Médio", "Difícil").forEach { rating ->
                                        val isSelected = ratingSelected == rating
                                        val bgSelectedColor = when (rating) {
                                            "Fácil" -> Emerald500
                                            "Médio" -> Amber500
                                            else -> Crimson400
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) bgSelectedColor else Color.Transparent)
                                                .clickable { viewModel.setExerciseRating(exIndex, rating) }
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = rating,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Slate900 else Slate400
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

        // Animated overlay of Resting interval count down (At the bottom!)
        AnimatedVisibility(
            visible = isRestActive,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Slate900.copy(alpha = 0.95f), Slate900)
                            )
                        )
                        .padding(bottom = 32.dp, top = 64.dp, start = 16.dp, end = 16.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate800),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Lime500, RoundedCornerShape(20.dp))
                            .testTag("rest_timer_panel")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(Lime500.copy(alpha = 0.1f), CircleShape)
                                        .border(2.dp, Lime500, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Pause, contentDescription = null, tint = Lime500)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        "DESCANSO INTELIGENTE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Lime400
                                    )
                                    Text(
                                        text = "$restRemaining segundos",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Slate50,
                                        modifier = Modifier.testTag("rest_timer_countdown")
                                    )
                                }
                            }

                            Button(
                                onClick = { viewModel.skipRestTimer() },
                                colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Pular", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Active Explanation detail modal drawer
        AnimatedVisibility(
            visible = exerciseDetailToShow != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            exerciseDetailToShow?.let { ex ->
                Box(modifier = Modifier.fillMaxSize().background(Slate900)) {
                    ExerciseDetailModal(
                        exercise = ex,
                        onDismiss = { exerciseDetailToShow = null }
                    )
                }
            }
        }

        // Dialog to select pain region
        if (showPainDialogIndex != null) {
            val exIndex = showPainDialogIndex!!
            val ex = split.exercises[exIndex]
            
            AlertDialog(
                onDismissRequest = { showPainDialogIndex = null },
                containerColor = Slate800,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Amber500, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Relatar Desconforto", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Onde você sentiu dor ou desconforto ao realizar \"${ex.name}\"?",
                            color = Slate100,
                            fontSize = 14.sp
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Joelhos", "Lombar").forEach { region ->
                                Button(
                                    onClick = {
                                        viewModel.reportExercisePain(exIndex, region)
                                        showPainDetailTextForIndex = Pair(exIndex, region)
                                        showPainDialogIndex = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(region, color = Slate50, fontSize = 12.sp)
                                }
                            }
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Ombros", "Outro").forEach { region ->
                                Button(
                                    onClick = {
                                        viewModel.reportExercisePain(exIndex, region)
                                        showPainDetailTextForIndex = Pair(exIndex, region)
                                        showPainDialogIndex = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(region, color = Slate50, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPainDialogIndex = null }) {
                        Text("Cancelar", color = Slate400, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Warning and proactive substitution trigger for pain
        if (showPainDetailTextForIndex != null) {
            val (exIndex, region) = showPainDetailTextForIndex!!
            val ex = split.exercises[exIndex]
            
            val warningMsg = when (region) {
                "Joelhos" -> "Atenção: Dor no joelho detectada! Caso sinta dor persistente ou pontadas agudas, pare imediatamente. Recomendamos trocar por uma alternativa de menor impacto para a articulação do joelho."
                "Lombar" -> "Atenção: Desconforto lombar relatado! Evite sobrecarga axial na coluna vertebral. Mantenha o cole contraído (bracing) e costas retas. Sugerimos substituir por variantes que poupam a lombar."
                "Ombros" -> "Atenção: Sensibilidade no ombro! Reduza a amplitude se necessário, utilize pegadas neutras que evitem impacto subacromial e mantenha as escápulas aduzidas."
                else -> "Atenção: Siga sempre as indicações de segurança. Se a dor persistir, pare o exercício de imediato e evite lesões."
            }
            
            AlertDialog(
                onDismissRequest = { showPainDetailTextForIndex = null },
                containerColor = Slate800,
                icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Crimson400, modifier = Modifier.size(36.dp)) },
                title = { Text("Alerta de Segurança", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                text = {
                    Column {
                        Text(warningMsg, color = Slate200, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Deseja abrir os Substitutos Inteligentes para trocar este exercício por uma alternativa biomecanicamente segura para $region?", color = Lime400, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val criterionPreset = when (region) {
                                "Joelhos" -> "Evitar Joelho"
                                "Lombar" -> "Evitar Lombar"
                                "Ombros" -> "Evitar Ombro"
                                else -> "Menor Impacto / Seguro"
                            }
                            exerciseToSubstituteIndex = exIndex
                            substitutionFilterCriterionState = criterionPreset
                            showPainDetailTextForIndex = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Lime500)
                    ) {
                        Text("Ver Substitutos", color = Slate900, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPainDetailTextForIndex = null }) {
                        Text("Continuar Treino", color = Slate50)
                    }
                }
            )
        }

        // Smart Substitution Dialog (choice selector modal)
        if (exerciseToSubstituteIndex != null) {
            val exIndex = exerciseToSubstituteIndex!!
            val workoutEx = split.exercises[exIndex]
            val dbMatch = CuratedExercises.list.find { it.id == workoutEx.id || it.name.equals(workoutEx.name, ignoreCase = true) }
            
            val criteriaList = listOf(
                "Mesmo Músculo",
                "Mais Fácil",
                "Mais Difícil",
                "Sem Máquina (Casa)",
                "Com Halteres",
                "Menor Impacto / Seguro",
                "Evitar Joelho",
                "Evitar Ombro",
                "Evitar Lombar"
            )
            
            val filteredList = getFilteredSubstitutions(workoutEx, dbMatch, substitutionFilterCriterionState)
            
            AlertDialog(
                onDismissRequest = { exerciseToSubstituteIndex = null },
                containerColor = Slate800,
                title = {
                    Column {
                        Text(
                            text = "Substitutos Inteligentes",
                            color = Slate50,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Original: ${workoutEx.name}",
                            color = Slate400,
                            fontSize = 12.sp
                        )
                    }
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Horizontally scrollable Chips for criteria selection
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            criteriaList.forEach { criterion ->
                                val isSelected = substitutionFilterCriterionState == criterion
                                val chipBg = if (isSelected) Lime500 else Slate900
                                val chipText = if (isSelected) Slate900 else Slate300
                                val chipBorder = if (isSelected) Lime500 else Slate700
                                
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(chipBg)
                                        .border(1.dp, chipBorder, RoundedCornerShape(16.dp))
                                        .clickable { substitutionFilterCriterionState = criterion }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = criterion,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = chipText
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        if (filteredList.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Nenhuma alternativa encontrada para esta categoria.",
                                    color = Slate400,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredList) { replacement ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Slate900),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                                            .clickable {
                                                viewModel.swapActiveWorkoutExerciseWithChoice(
                                                    exerciseIndex = exIndex,
                                                    replacement = replacement,
                                                    reason = substitutionFilterCriterionState
                                                )
                                                exerciseToSubstituteIndex = null
                                            }
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = replacement.name,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = Lime400,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .background(Slate800, RoundedCornerShape(6.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        replacement.difficultyLevel,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Slate50
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Equipamento: ${replacement.equipmentNeeded} • Foco: ${replacement.primaryMuscle}",
                                                color = Slate400,
                                                fontSize = 11.sp
                                            )
                                            if (replacement.safetyWarnings.isNotEmpty() || replacement.postureTips.isNotEmpty()) {
                                                Text(
                                                    text = "💡 " + (replacement.postureTips.firstOrNull() ?: replacement.safetyWarnings.firstOrNull() ?: ""),
                                                    color = Slate300,
                                                    fontSize = 10.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.padding(top = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { exerciseToSubstituteIndex = null }) {
                        Text("Fechar", color = Slate400, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Quit alert confirmation dialog
        if (showCancelAlert) {
            AlertDialog(
                onDismissRequest = { showCancelAlert = false },
                containerColor = Slate800,
                title = { Text("Cancelar Treino?", color = Crimson400, fontWeight = FontWeight.Bold) },
                text = { Text("Você perderá o progresso do cronômetro desta sessão. Deseja realmente parar o treino agora?", color = Slate50) },
                confirmButton = {
                    Button(
                        onClick = {
                            showCancelAlert = false
                            onDismiss() // Go back
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Crimson400)
                    ) {
                        Text("Sim, parar", color = Slate50, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCancelAlert = false }) {
                        Text("Continuar Treinando", color = Slate50)
                    }
                }
            )
        }
    }
}

// Compact helper input composable
@Composable
fun CompactInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    widthDp: Int
) {
    var localValue by remember(value) { mutableStateOf(value) }

    // Smoothly debounce updates to parent StateFlow to avoid recomposition storms
    LaunchedEffect(localValue) {
        if (localValue != value) {
            delay(400)
            onValueChange(localValue)
        }
    }

    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .width(widthDp.dp)
            .height(34.dp)
            .background(Slate800, RoundedCornerShape(6.dp))
            .border(1.dp, Slate700, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.text.BasicTextField(
            value = localValue,
            onValueChange = { newValue ->
                localValue = newValue
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onValueChange(localValue)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier.onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    onValueChange(localValue)
                }
            },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Slate50,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Lime500),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (localValue.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 11.sp,
                            color = Slate500,
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

// Biomechanical substitution algorithm
fun getFilteredSubstitutions(originalEx: WorkoutExercise, originalDb: Exercise?, criterion: String): List<Exercise> {
    val all = CuratedExercises.list.filter { it.id != originalEx.id && !it.name.equals(originalEx.name, ignoreCase = true) }
    val originalCategory = originalDb?.category ?: "Pernas"
    val originalMuscle = originalDb?.primaryMuscle ?: ""
    
    return when (criterion) {
        "Mesmo Músculo" -> {
            all.filter { it.category.equals(originalCategory, ignoreCase = true) || it.primaryMuscle.equals(originalMuscle, ignoreCase = true) }
        }
        "Mais Fácil" -> {
            all.filter { 
                (it.category.equals(originalCategory, ignoreCase = true) && (it.difficultyLevel == "Iniciante" || it.difficultyLevel == "Intermediário")) ||
                (originalDb?.easierVariation?.isNotBlank() == true && it.name.contains(originalDb.easierVariation.split(" ou ").first().split(" (").first(), ignoreCase = true))
            }
        }
        "Mais Difícil" -> {
            all.filter { 
                (it.category.equals(originalCategory, ignoreCase = true) && (it.difficultyLevel == "Avançado" || it.difficultyLevel == "Intermediário")) ||
                (originalDb?.harderVariation?.isNotBlank() == true && it.name.contains(originalDb.harderVariation.split(" ou ").first().split(" (").first(), ignoreCase = true))
            }
        }
        "Sem Máquina (Casa)" -> {
            all.filter { 
                it.category.equals(originalCategory, ignoreCase = true) && 
                !it.equipmentNeeded.contains("Máquina", ignoreCase = true) && 
                !it.equipmentNeeded.contains("Polia", ignoreCase = true)
            }
        }
        "Com Halteres" -> {
            all.filter { 
                it.category.equals(originalCategory, ignoreCase = true) && 
                (it.equipmentNeeded.contains("Halter", ignoreCase = true) || it.tags.any { t -> t.contains("halter", ignoreCase = true) })
            }
        }
        "Menor Impacto / Seguro" -> {
            all.filter { 
                it.category.equals(originalCategory, ignoreCase = true) && 
                (it.tags.contains("reabilitacao") || it.tags.contains("joelho") || it.tags.contains("lombar") || it.difficultyLevel == "Iniciante" || it.name.contains("Parede", ignoreCase = true))
            }
        }
        "Evitar Joelho" -> {
            all.filter { 
                it.category.equals(originalCategory, ignoreCase = true) && 
                !it.tags.contains("joelho") && 
                !it.name.contains("Búlgaro", ignoreCase = true) && 
                !it.name.contains("Agachamento Livre", ignoreCase = true)
            }
        }
        "Evitar Ombro" -> {
            all.filter { 
                (it.category.equals(originalCategory, ignoreCase = true) || it.category == "Ombros" || it.category == "Peito") && 
                !it.tags.contains("ombro") && 
                !it.name.contains("Desenvolvimento", ignoreCase = true)
            }
        }
        "Evitar Lombar" -> {
            all.filter { 
                it.category.equals(originalCategory, ignoreCase = true) && 
                !it.tags.contains("lombar") && 
                !it.name.contains("RDL", ignoreCase = true) && 
                !it.name.contains("Terra", ignoreCase = true)
            }
        }
        else -> all.filter { it.category.equals(originalCategory, ignoreCase = true) }
    }
}


// ==========================================
// 6. WORKOUT CELEBRATION CONGRATULATIONS
// ==========================================
@Composable
fun WorkoutCelebrationOverlay(
    session: CompletedSession,
    onDismiss: () -> Unit
) {
    Surface(
        color = Slate900,
        modifier = Modifier
            .fillMaxSize()
            .testTag("workout_celebration_overlay")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Lime500,
                modifier = Modifier.size(96.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TREINO CONCLUÍDO!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Slate50,
                letterSpacing = 2.sp
            )

            Text(
                text = "Seu corpo agradece o compromisso de hoje.",
                fontSize = 14.sp,
                color = Lime400,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Slate800),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate700, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = session.splitName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Slate50
                    )
                    Text(
                        text = "Programa: ${session.routineTitle}",
                        fontSize = 12.sp,
                        color = Slate400,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("DURAÇÃO", fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Bold)
                            Text(
                                text = formatSecondsToMinutes(session.durationSeconds),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Lime400
                            )
                        }

                        VerticalDividerCustom()

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("EXERCÍCIOS", fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${session.exercisesCompleted} / ${session.totalExercises}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Lime400
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Lime500),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("celebration_dismiss_button")
            ) {
                Text("Fechar e Continuar", color = Slate900, fontWeight = FontWeight.Black, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun VerticalDividerCustom() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(Slate700)
    )
}

// Helper formatting seconds to string chronometer
fun formatSecondsToMinutes(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}

@Composable
fun PersonalizerQuizDialog(
    current: UserProfile,
    viewModel: WorkoutViewModel,
    onSave: (UserProfile) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedThemeCode by viewModel.selectedThemeCode.collectAsStateWithLifecycle()
    val highRefreshEnabled by viewModel.highRefreshEnabled.collectAsStateWithLifecycle()

    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current
    var name by remember { mutableStateOf(current.name) }
    var ageStr by remember { mutableStateOf(current.age.toString()) }
    var weightStr by remember { mutableStateOf(current.weight.toString()) }
    var heightStr by remember { mutableStateOf(current.height.toString()) }
    var objective by remember { mutableStateOf(current.objective) }
    var level by remember { mutableStateOf(current.level) }
    var location by remember { mutableStateOf(current.location) }
    
    val equipmentsList = listOf("Halteres", "Barra", "Máquinas articuladas", "Peso Corporal", "Polias e Cabos", "Bandas Elásticas")
    val selectedEquipments = remember { mutableStateListOf<String>().apply { addAll(current.equipments) } }
    
    var limitations by remember { mutableStateOf(current.limitations) }
    var frequency by remember { mutableStateOf(current.frequency) }
    var durationMinutes by remember { mutableStateOf(current.durationMinutes) }
    var preferences by remember { mutableStateOf(current.preferences) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Slate800,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag("quiz_settings_pane"),
        title = {
            Text(
                "Ajustar Perfil Biomecânico",
                color = Slate50,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth().height(420.dp)
            ) {
                item {
                    Text("Nome", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Lime500,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Slate50,
                            unfocusedTextColor = Slate50
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text("Idade & Peso & Altura", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = ageStr,
                            onValueChange = { ageStr = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Idade") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Slate50, unfocusedTextColor = Slate50),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = weightStr,
                            onValueChange = { weightStr = it },
                            label = { Text("Peso(kg)") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Slate50, unfocusedTextColor = Slate50),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = heightStr,
                            onValueChange = { heightStr = it },
                            label = { Text("Alt(cm)") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Slate50, unfocusedTextColor = Slate50),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Text("Qual o seu objetivo principal?", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "Hipertrofia" to "Hipertrofia (Ganho de Massa)",
                            "Emagrecimento" to "Emagrecimento & Definição",
                            "Força" to "Força (Aumento de Cargas)",
                            "Resistência" to "Resistência & Cardio",
                            "Mobilidade" to "Mobilidade & Flexibilidade",
                            "Saúde Geral" to "Saúde Geral & Fortalecimento"
                        ).forEach { (key, label) ->
                            SettingSelectableRow(
                                selected = objective == key,
                                title = label,
                                onClick = { objective = key }
                            )
                        }
                    }
                }

                item {
                    Text("Nível:", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Iniciante", "Intermediário", "Avançado").forEach { lvl ->
                            val active = level == lvl
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (active) Lime500 else Slate900)
                                    .clickable { level = lvl }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(lvl, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Slate900 else Slate50)
                            }
                        }
                    }
                }

                item {
                    Text("Local de Treino:", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Casa", "Academia", "Ambos").forEach { loc ->
                            val active = location == loc
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (active) Lime500 else Slate900)
                                    .clickable { location = loc }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(loc, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Slate900 else Slate50)
                            }
                        }
                    }
                }

                item {
                    Text("Equipamentos Disponíveis:", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        equipmentsList.forEach { eq ->
                            val isChecked = selectedEquipments.contains(eq)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, if (isChecked) Lime500 else Slate700, RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (isChecked) selectedEquipments.remove(eq) else selectedEquipments.add(eq)
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { if (isChecked) selectedEquipments.remove(eq) else selectedEquipments.add(eq) }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(eq, fontSize = 12.sp, color = Slate50)
                            }
                        }
                    }
                }

                item {
                    Text("Freq. semanal (dias):", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(2, 3, 4, 5, 6).forEach { d ->
                            val active = frequency == d
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (active) Lime500 else Slate900)
                                    .clickable { frequency = d }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$d dias", fontSize = 11.sp, color = if (active) Slate900 else Slate50)
                            }
                        }
                    }
                }

                item {
                    Text("Lesões ou Limitações:", color = Amber400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Nenhuma", "Lombar", "Joelho", "Ombro", "Outros").forEach { lim ->
                            val active = limitations == lim
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, if (active) Amber400 else Slate700, RoundedCornerShape(8.dp))
                                    .clickable { limitations = lim }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = active, onClick = { limitations = lim })
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(lim, fontSize = 12.sp, color = Slate50)
                            }
                        }
                    }
                }

                item {
                    Text("Anotações de Preferências:", color = Slate400, fontSize = 12.sp)
                    OutlinedTextField(
                        value = preferences,
                        onValueChange = { preferences = it },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Slate50, unfocusedTextColor = Slate50),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Paleta de cores do Aplicativo (personalização voluntária)
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Paleta de Cores de Destaque", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(ThemeConfig.palettes) { p ->
                            val isSelected = selectedThemeCode == p.code
                            Box(
                                modifier = Modifier
                                    .size(width = 115.dp, height = 48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) p.primary.copy(alpha = 0.15f) else Slate900)
                                    .border(1.dp, if (isSelected) p.primary else Slate700, RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.updateSelectedTheme(p.code)
                                    }
                                    .padding(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(p.primary, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(p.name, fontSize = 9.sp, color = Slate50, fontWeight = FontWeight.Bold, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                // Taxa de atualizacao adaptativa de tela
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Slate900)
                            .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 6.dp)) {
                            Text("Fluidez Máxima (90Hz / 120Hz)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate50)
                            Text("Forçar a maior taxa do dispositivo.", fontSize = 9.sp, color = Slate400)
                        }
                        Switch(
                            checked = highRefreshEnabled,
                            onCheckedChange = { viewModel.updateHighRefreshEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Lime500,
                                checkedTrackColor = Lime500.copy(alpha = 0.4f),
                                uncheckedThumbColor = Slate400,
                                uncheckedTrackColor = Slate900
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val ageVal = ageStr.toIntOrNull() ?: current.age
                    val weightVal = weightStr.toDoubleOrNull() ?: current.weight
                    val heightVal = heightStr.toDoubleOrNull() ?: current.height
                    onSave(
                        UserProfile(
                            name = name.ifBlank { current.name },
                            age = ageVal,
                            weight = weightVal,
                            height = heightVal,
                            objective = objective,
                            level = level,
                            location = location,
                            equipments = selectedEquipments.toList(),
                            frequency = frequency,
                            durationMinutes = durationMinutes,
                            limitations = limitations,
                            preferences = preferences
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Lime500),
                modifier = Modifier.testTag("save_quiz_profile_button")
            ) {
                Text("Salvar", color = Slate900, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Slate400)
            }
        }
    )
}

@Composable
fun SettingSelectableRow(selected: Boolean, title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Lime500.copy(alpha = 0.1f) else Slate900)
            .border(1.dp, if (selected) Lime400 else Slate700, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        CenterRow {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = Lime500, unselectedColor = Slate400)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) Lime400 else Slate50
            )
        }
    }
}

// ==========================================
// 5. NUTRITION SCREEN (FOOD DIARY & GEMINI)
// ==========================================
data class PredefinedFood(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionScreen(viewModel: WorkoutViewModel) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val foodDiary by viewModel.foodDiary.collectAsStateWithLifecycle()
    val isAnalyzingFood by viewModel.isAnalyzingFood.collectAsStateWithLifecycle()
    val foodAnalysisResult by viewModel.foodAnalysisResult.collectAsStateWithLifecycle()

    var foodSearchQuery by remember { mutableStateOf("") }
    var geminiFoodQuery by remember { mutableStateOf("") }
    
    // Manual add form state
    var showManualAddDialog by remember { mutableStateOf(false) }
    var manualName by remember { mutableStateOf("") }
    var manualCalories by remember { mutableStateOf("") }
    var manualProtein by remember { mutableStateOf("") }
    var manualCarbs by remember { mutableStateOf("") }
    var manualFats by remember { mutableStateOf("") }

    // Custom quick-add from Gemini result state
    var quickAddFromGeminiName by remember { mutableStateOf("") }
    var quickAddFromGeminiCal by remember { mutableStateOf("") }
    var quickAddFromGeminiProt by remember { mutableStateOf("") }
    var quickAddFromGeminiCarb by remember { mutableStateOf("") }
    var quickAddFromGeminiFat by remember { mutableStateOf("") }

    // Database of Portuguese common fitness foods
    val predefinedFoods = remember {
        listOf(
            PredefinedFood("Peito de Frango Grelhado (100g)", 165, 31, 0, 4),
            PredefinedFood("Arroz Branco Cozido (100g)", 130, 2, 28, 0),
            PredefinedFood("Arroz Integral Cozido (100g)", 111, 3, 23, 1),
            PredefinedFood("Feijão Preto Cozido (100g)", 76, 5, 14, 0),
            PredefinedFood("Ovo Cozido Inteiro (1 unid)", 78, 6, 1, 5),
            PredefinedFood("Pão Integral (1 fatia / 25g)", 65, 3, 12, 1),
            PredefinedFood("Banana Prata (1 unid / 80g)", 89, 1, 23, 0),
            PredefinedFood("Maçã Fuji (1 unid / 130g)", 52, 0, 14, 0),
            PredefinedFood("Azeite de Oliva (1 colher / 13ml)", 119, 0, 0, 13),
            PredefinedFood("Whey Protein Concentrado (30g)", 120, 24, 3, 2),
            PredefinedFood("Carne Moída Patinho (100g)", 219, 26, 0, 12),
            PredefinedFood("Pasta de Amendoim (1 colher / 15g)", 90, 4, 3, 8),
            PredefinedFood("Batata Doce Cozida (100g)", 86, 2, 20, 0),
            PredefinedFood("Tapioca Goma Cozida (50g)", 120, 0, 29, 0),
            PredefinedFood("Queijo Minas Frescal (30g)", 73, 6, 1, 5),
            PredefinedFood("Iogurte Natural Desnatado (170g)", 70, 7, 10, 0),
            PredefinedFood("Aveia em Flocos Inteiros (30g)", 110, 4, 17, 2),
            PredefinedFood("Leite Desnatado Líquido (200ml)", 70, 6, 10, 0)
        )
    }

    // Dynamic nutritional equations based on active user metrics
    val targetCalories = remember(profile) {
        val base = (10 * profile.weight) + (6.25 * profile.height) - (5 * profile.age) + 5
        when (profile.objective) {
            "Emagrecimento" -> (base * 1.25 - 400).toInt().coerceAtLeast(1200)
            "Hipertrofia" -> (base * 1.35 + 400).toInt()
            else -> (base * 1.3).toInt()
        }
    }

    // Split target macro splits according to fitness science (2g/kg Prot)
    val targetProtein = remember(profile) { (profile.weight * 2.0).toInt().coerceAtLeast(60) }
    val targetFat = remember(profile) { (profile.weight * 0.9).toInt().coerceAtLeast(40) }
    val targetCarbs = remember(targetCalories, targetProtein, targetFat) {
        ((targetCalories - (targetProtein * 4) - (targetFat * 9)) / 4).coerceAtLeast(100)
    }

    val consumedCalories = foodDiary.sumOf { it.calories }
    val consumedProtein = foodDiary.sumOf { it.proteins }
    val consumedCarbs = foodDiary.sumOf { it.carbs }
    val consumedFats = foodDiary.sumOf { it.fats }

    val caloriesProgress = if (targetCalories > 0) consumedCalories.toFloat() / targetCalories else 0f
    val proteinProgress = if (targetProtein > 0) consumedProtein.toFloat() / targetProtein else 0f
    val carbsProgress = if (targetCarbs > 0) consumedCarbs.toFloat() / targetCarbs else 0f
    val fatsProgress = if (targetFat > 0) consumedFats.toFloat() / targetFat else 0f

    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 96.dp, top = 8.dp)
    ) {
        // Transparent, glassmorphic title banner with parallax scrolling
        item {
            ParallaxHeader(lazyListState = scrollState) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassCard(glowColor = Lime500, shape = RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MONITOR CLÍNICO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Lime400,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Diário de Nutrição",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate50
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Equações dinâmicas com ajustes para $targetCalories kcal diárias.",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(Lime500.copy(alpha = 0.15f), CircleShape)
                                .border(1.dp, Lime500.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Restaurant, contentDescription = null, tint = Lime500, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }

        // 📊 MAIN PROGRESS CARD - Styled beautifully as liquid glass
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(shape = RoundedCornerShape(16.dp), glowColor = Lime500)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Consumo Energético", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$consumedCalories",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Lime400
                               )
                                Text(
                                    text = " / $targetCalories kcal",
                                    fontSize = 14.sp,
                                    color = Slate300,
                                    modifier = Modifier.padding(bottom = 3.dp, start = 4.dp)
                                )
                            }
                        }
                        
                        Box(
                            modifier = Modifier
                                .background(
                                    if (consumedCalories <= targetCalories) Lime500.copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (consumedCalories <= targetCalories) {
                                    "Faltam ${targetCalories - consumedCalories} kcal"
                                } else {
                                    "Excesso de ${consumedCalories - targetCalories} kcal"
                                },
                                color = if (consumedCalories <= targetCalories) Lime400 else Color(0xFFF87171),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    LinearProgressIndicator(
                        progress = { caloriesProgress.coerceAtMost(1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape),
                        color = if (consumedCalories <= targetCalories) Lime500 else Color(0xFFEF4444),
                        trackColor = Slate900
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Distribuição de Macronutrientes Genuínos", color = Slate300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Protein
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Proteínas", color = Slate400, fontSize = 10.sp)
                                Text("${consumedProtein}g/${targetProtein}g", color = Slate50, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { proteinProgress.coerceAtMost(1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                                color = Color(0xFF38BDF8),
                                trackColor = Slate900
                            )
                        }
                        
                        // Carbs
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Carboidratos", color = Slate400, fontSize = 10.sp)
                                Text("${consumedCarbs}g/${targetCarbs}g", color = Slate50, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { carbsProgress.coerceAtMost(1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                                color = Color(0xFFFBBF24),
                                trackColor = Slate900
                            )
                        }

                        // Fats
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Gorduras", color = Slate400, fontSize = 10.sp)
                                Text("${consumedFats}g/${targetFat}g", color = Slate50, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { fatsProgress.coerceAtMost(1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                                color = Color(0xFFF87171),
                                trackColor = Slate900
                            )
                        }
                    }
                }
            }
        }

        // 🔍 BRAZILIAN FOOD DIRECTORY SEARCH (VASTA BUSCA DE ALIMENTOS)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Vasto Acervo de Alimentos (Rápido)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate50
                    )
                    TextButton(
                        onClick = { showManualAddDialog = true },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Lime400, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Adicionar Manual", color = Lime400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                
                OutlinedTextField(
                    value = foodSearchQuery,
                    onValueChange = { foodSearchQuery = it },
                    placeholder = { Text("Pesquise no acervo... (ex: frango, arroz, ovo, leite)", fontSize = 12.sp, color = Slate500) },
                    modifier = Modifier.fillMaxWidth().testTag("food_search_input"),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp)) },
                    trailingIcon = {
                        if (foodSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { foodSearchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Lime500,
                        unfocusedBorderColor = Slate700,
                        focusedContainerColor = Slate800,
                        unfocusedContainerColor = Slate800,
                        focusedTextColor = Slate50,
                        unfocusedTextColor = Slate50
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // RENDER PREDEFINED SEARCH RESULTS
        val filteredFoods = predefinedFoods.filter {
            it.name.contains(foodSearchQuery, ignoreCase = true)
        }
        
        if (filteredFoods.isEmpty()) {
            item {
                Text("Nenhum alimento comum encontrado para seu filtro. Tente buscar com o Gemini abaixo!", color = Slate500, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 4.dp))
            }
        } else {
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredFoods.forEach { food ->
                        Box(
                            modifier = Modifier
                                .background(Slate800, RoundedCornerShape(12.dp))
                                .border(1.dp, Slate700, RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.addFoodItem(
                                        name = food.name,
                                        calories = food.calories,
                                        proteins = food.protein,
                                        carbs = food.carbs,
                                        fats = food.fat
                                    )
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(food.name, color = Slate50, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("${food.calories} kcal • P: ${food.protein}g • C: ${food.carbs}g • G: ${food.fat}g", color = Slate400, fontSize = 9.sp)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.AddCircle, contentDescription = "Quick Add", tint = Lime400, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // 🤖 GEMINI AI FOOD WEIGHT & PORTION RESEARCH
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Slate800.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Lime500.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(Lime500.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = null, tint = Lime500, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Nutricionista e Pesquisador Gemini IA", color = Slate50, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Descreva o que comeu sem se preocupar com pesos. O Gemini pesquisará as tabelas, estimará a quantidade correta das porções, calorias e mandará para registro imediato!",
                        color = Slate400,
                        fontSize = 10.sp,
                        lineHeight = 15.sp
                    )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    OutlinedTextField(
                        value = geminiFoodQuery,
                        onValueChange = { geminiFoodQuery = it },
                        placeholder = { Text("Ex: 1 concha cheia de estrogonofe de carne, 3 colheres de sopa arroz branco e batata palha", fontSize = 11.sp, color = Slate500) },
                        modifier = Modifier.fillMaxWidth().testTag("gemini_food_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Lime500,
                            unfocusedBorderColor = Slate700,
                            focusedContainerColor = Slate900,
                            unfocusedContainerColor = Slate900,
                            focusedTextColor = Slate50,
                            unfocusedTextColor = Slate50
                        ),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 4
                    )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Button(
                        onClick = {
                            viewModel.analyzeFoodWithGemini(geminiFoodQuery)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Lime500),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isAnalyzingFood && geminiFoodQuery.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("gemini_food_button")
                    ) {
                        if (isAnalyzingFood) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Slate900, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Consertando e Pesquisando...", color = Slate900, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Slate900, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Estimar e Pesquisar Nutrientes", color = Slate900, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }

                    // Render Gemini Analysis Results Form if available
                    foodAnalysisResult?.let { rawResult ->
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Slate900, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Análise do Nutricionista IA:", color = Lime400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    IconButton(
                                        onClick = { viewModel.clearFoodAnalysis() },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Slate400, modifier = Modifier.size(12.dp))
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(6.dp))
                                
                                val cleanedResultToShow = remember(rawResult) {
                                    rawResult.lines()
                                        .filterNot { it.trim().startsWith("[RESULT]") }
                                        .joinToString("\n")
                                        .trim()
                                }
                                Text(
                                    text = cleanedResultToShow,
                                    color = Slate200,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Interactive registration widget pre-filled
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Divider(modifier = Modifier.weight(1f), color = Slate700)
                                    Text(" Registrar no Diário ", color = Slate500, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Divider(modifier = Modifier.weight(1f), color = Slate700)
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))

                                // Auto pre-fill names if they are blank so users can easily log
                                LaunchedEffect(rawResult) {
                                    quickAddFromGeminiName = geminiFoodQuery.take(30)
                                    // Parse [RESULT];cal;prot;carb;fat if present anywhere in the line
                                    val resultLine = rawResult.lines().firstOrNull { it.contains("[RESULT]") }
                                    if (resultLine != null) {
                                        val startIndex = resultLine.indexOf("[RESULT]")
                                        val cleanLine = resultLine.substring(startIndex)
                                        val parts = cleanLine.split(";")
                                        if (parts.size >= 5) {
                                            quickAddFromGeminiCal = parts[1].trim()
                                            quickAddFromGeminiProt = parts[2].trim()
                                            quickAddFromGeminiCarb = parts[3].trim()
                                            quickAddFromGeminiFat = parts[4].trim()
                                        }
                                    } else {
                                        // Fallback to simple regex/defaults
                                        val numberRegex = "\\b(\\d{3,4})\\b".toRegex()
                                        val match = numberRegex.find(rawResult)
                                        quickAddFromGeminiCal = match?.value ?: "350"
                                        quickAddFromGeminiProt = "15"
                                        quickAddFromGeminiCarb = "40"
                                        quickAddFromGeminiFat = "8"
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedTextField(
                                        value = quickAddFromGeminiName,
                                        onValueChange = { quickAddFromGeminiName = it },
                                        label = { Text("Nome / Desc.", fontSize = 9.sp) },
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate800, unfocusedContainerColor = Slate800),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1.8f),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = quickAddFromGeminiCal,
                                        onValueChange = { quickAddFromGeminiCal = it },
                                        label = { Text("kcal", fontSize = 9.sp) },
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate800, unfocusedContainerColor = Slate800),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    OutlinedTextField(
                                        value = quickAddFromGeminiProt,
                                        onValueChange = { quickAddFromGeminiProt = it },
                                        label = { Text("Prot (g)", fontSize = 8.sp) },
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate800, unfocusedContainerColor = Slate800),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = quickAddFromGeminiCarb,
                                        onValueChange = { quickAddFromGeminiCarb = it },
                                        label = { Text("Carb (g)", fontSize = 8.sp) },
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate800, unfocusedContainerColor = Slate800),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = quickAddFromGeminiFat,
                                        onValueChange = { quickAddFromGeminiFat = it },
                                        label = { Text("Gord (g)", fontSize = 8.sp) },
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate800, unfocusedContainerColor = Slate800),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                
                                Button(
                                    onClick = {
                                        val cVals = quickAddFromGeminiCal.toIntOrNull() ?: 350
                                        val pVals = quickAddFromGeminiProt.toIntOrNull() ?: 15
                                        val cbVals = quickAddFromGeminiCarb.toIntOrNull() ?: 40
                                        val fVals = quickAddFromGeminiFat.toIntOrNull() ?: 8
                                        if (quickAddFromGeminiName.isNotBlank()) {
                                            viewModel.addFoodItem(
                                                name = quickAddFromGeminiName,
                                                calories = cVals,
                                                proteins = pVals,
                                                carbs = cbVals,
                                                fats = fVals
                                            )
                                            geminiFoodQuery = ""
                                            viewModel.clearFoodAnalysis()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().height(34.dp)
                                ) {
                                    Text("Confirmar e Registrar no Diário", color = Slate50, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 🍳 LOGGED FOOD LIST (DIÁRIO DE ALIMENTOS CONSUMIDOS)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Refeições Registradas Hoje",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate50
                )
                if (foodDiary.isNotEmpty()) {
                    Text(
                        text = "Limpar Tudo",
                        color = Crimson400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.clearFoodDiary() }
                            .padding(4.dp)
                    )
                }
            }
        }

        if (foodDiary.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate800),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.EmojiFoodBeverage, contentDescription = null, tint = Slate400, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Diário Vazio", color = Slate300, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Adicione os alimentos consumidos da lista rápida acima ou fale com o Gemini para começar!", color = Slate500, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        } else {
            items(foodDiary) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate800),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, color = Slate50, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${item.dateString} • P: ${item.proteins}g • C: ${item.carbs}g • G: ${item.fats}g", color = Slate400, fontSize = 10.sp)
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(Slate900, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("${item.calories} kcal", color = Lime400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            IconButton(
                                onClick = { viewModel.removeFoodItem(item.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Crimson400, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog to MANUAL add food item
    if (showManualAddDialog) {
        AlertDialog(
            onDismissRequest = { showManualAddDialog = false },
            title = { Text("Registrar Alimento Manual", color = Slate50, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            containerColor = Slate850,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = manualName,
                        onValueChange = { manualName = it },
                        label = { Text("Ex: Filé de Frango Grelhado") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = manualCalories,
                        onValueChange = { manualCalories = it },
                        label = { Text("Calorias (kcal)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = manualProtein,
                        onValueChange = { manualProtein = it },
                        label = { Text("Proteínas (g)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = manualCarbs,
                        onValueChange = { manualCarbs = it },
                        label = { Text("Carboidratos (g)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = manualFats,
                        onValueChange = { manualFats = it },
                        label = { Text("Gorduras (g)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Lime500, focusedTextColor = Slate50, unfocusedTextColor = Slate50, focusedContainerColor = Slate900, unfocusedContainerColor = Slate900),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cValue = manualCalories.toIntOrNull() ?: 0
                        val pValue = manualProtein.toIntOrNull() ?: 0
                        val cbValue = manualCarbs.toIntOrNull() ?: 0
                        val fValue = manualFats.toIntOrNull() ?: 0
                        if (manualName.isNotBlank()) {
                            viewModel.addFoodItem(
                                name = manualName,
                                calories = cValue,
                                proteins = pValue,
                                carbs = cbValue,
                                fats = fValue
                            )
                            // Reset & close
                            manualName = ""
                            manualCalories = ""
                            manualProtein = ""
                            manualCarbs = ""
                            manualFats = ""
                            showManualAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Lime500)
                ) {
                    Text("Adicionar", color = Slate900, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualAddDialog = false }) {
                    Text("Cancelar", color = Slate400)
                }
            }
        )
    }
}
