package com.example.ui

import android.app.Application
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiClient
import com.example.data.*
import com.example.ui.theme.ThemeConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed interface AiGenerationState {
    object Idle : AiGenerationState
    object Loading : AiGenerationState
    data class Success(val plan: WorkoutPlan) : AiGenerationState
    data class Error(val message: String) : AiGenerationState
}

data class ChatMessage(
    val sender: String, // "user" or "coach"
    val content: String,
    val timeString: String
)

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = WorkoutPreferences(application)

    // Core States
    private val _userProfile = MutableStateFlow(prefs.getUserProfile())
    val userProfile = _userProfile.asStateFlow()

    private val _activePlan = MutableStateFlow(prefs.getActivePlan())
    val activePlan = _activePlan.asStateFlow()

    private val _completedSessions = MutableStateFlow(prefs.getCompletedSessions())
    val completedSessions = _completedSessions.asStateFlow()

    // Separate Workout Plans State Flow
    private val _userCustomPlan = MutableStateFlow(prefs.getUserCustomPlan())
    val userCustomPlan = _userCustomPlan.asStateFlow()

    private val _aiCoachPlan = MutableStateFlow(prefs.getAiCoachPlan())
    val aiCoachPlan = _aiCoachPlan.asStateFlow()

    private val _selectedPlanType = MutableStateFlow(prefs.getSelectedPlanType())
    val selectedPlanType = _selectedPlanType.asStateFlow()

    // Dynamic Theme State Flow
    private val _selectedThemeCode = MutableStateFlow(prefs.getSelectedTheme())
    val selectedThemeCode = _selectedThemeCode.asStateFlow()

    // Refresh rate setting state
    private val _highRefreshEnabled = MutableStateFlow(prefs.getHighRefreshEnabled())
    val highRefreshEnabled = _highRefreshEnabled.asStateFlow()

    // Food diary State Flow
    private val _foodDiary = MutableStateFlow(prefs.getFoodDiary())
    val foodDiary = _foodDiary.asStateFlow()

    // Food Calorie Analysis via Gemini State Flow
    private val _isAnalyzingFood = MutableStateFlow(false)
    val isAnalyzingFood = _isAnalyzingFood.asStateFlow()

    private val _foodAnalysisResult = MutableStateFlow<String?>(null)
    val foodAnalysisResult = _foodAnalysisResult.asStateFlow()

    // AI Generation State
    private val _aiState = MutableStateFlow<AiGenerationState>(AiGenerationState.Idle)
    val aiState = _aiState.asStateFlow()

    // Chat History
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("coach", "Olá! Sou o Coach Forte. Aqui você pode tirar dúvidas sobre execuções, pedir substitutos para seu treino, sugerir novas calorias e alimentos, ou mandar eu atualizar e personalizar seus treinos. Como posso te auxiliar hoje?", getCurrentTime())
    ))
    val chatMessages = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading = _isChatLoading.asStateFlow()

    // --- Active Workout Tracker States ---
    private val _isWorkoutRunning = MutableStateFlow(false)
    val isWorkoutRunning = _isWorkoutRunning.asStateFlow()

    private val _activeSplit = MutableStateFlow<WorkoutSplit?>(null)
    val activeSplit = _activeSplit.asStateFlow()

    private val _workoutTimerSeconds = MutableStateFlow(0L)
    val workoutTimerSeconds = _workoutTimerSeconds.asStateFlow()

    // Tracks checked sets: "exerciseIndex_setIndex" -> Boolean
    private val _checkedSets = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val checkedSets = _checkedSets.asStateFlow()

    // Tracks loads: "exerciseIndex_setIndex" -> String (e.g. "12 kg")
    private val _setLoads = MutableStateFlow<Map<String, String>>(emptyMap())
    val setLoads = _setLoads.asStateFlow()

    // Tracks reps performed: "exerciseIndex_setIndex" -> String (e.g. "10")
    private val _setReps = MutableStateFlow<Map<String, String>>(emptyMap())
    val setReps = _setReps.asStateFlow()

    // Tracks exercise difficulty rating: exerciseIndex -> String ("Fácil", "Médio", "Difícil")
    private val _exerciseRatings = MutableStateFlow<Map<Int, String>>(emptyMap())
    val exerciseRatings = _exerciseRatings.asStateFlow()

    // Tracks exercise pain/discomfort report: exerciseIndex -> String (e.g., "Joelhos")
    private val _exercisePain = MutableStateFlow<Map<Int, String>>(emptyMap())
    val exercisePain = _exercisePain.asStateFlow()

    // Rest timer state
    private val _restSecondsRemaining = MutableStateFlow(0)
    val restSecondsRemaining = _restSecondsRemaining.asStateFlow()
    
    private val _isRestTimerActive = MutableStateFlow(false)
    val isRestTimerActive = _isRestTimerActive.asStateFlow()

    // Celebration
    private val _showWorkoutCelebration = MutableStateFlow(false)
    val showWorkoutCelebration = _showWorkoutCelebration.asStateFlow()

    private var timerJob: Job? = null
    private var restJob: Job? = null

    init {
        // Log setup configurations
        Log.d("WorkoutViewModel", "Loaded active workouts and sessions correctly.")
    }

    // --- Profile Operations ---
    fun updateProfile(profile: UserProfile) {
        _userProfile.value = profile
        prefs.saveUserProfile(profile)
    }

    // --- Dynamic Theme Customizer ---
    fun updateSelectedTheme(theme: String) {
        _selectedThemeCode.value = theme
        prefs.saveSelectedTheme(theme)
        
        val paletteObj = ThemeConfig.palettes.find { it.code == theme }
        if (paletteObj != null) {
            ThemeConfig.primaryColor = paletteObj.primary
            ThemeConfig.secondaryColor = paletteObj.secondary
        }
    }

    // --- Dynamic Refresh Rate Customizer ---
    fun updateHighRefreshEnabled(enabled: Boolean) {
        _highRefreshEnabled.value = enabled
        prefs.saveHighRefreshEnabled(enabled)
    }

    // --- Food Diary Tracking Operations ---
    fun addFoodItem(name: String, calories: Int, proteins: Int = 0, carbs: Int = 0, fats: Int = 0) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = sdf.format(Date())
        val item = FoodDiaryItem(
            id = UUID.randomUUID().toString(),
            name = name,
            dateString = "Hoje, $timeString",
            calories = calories,
            proteins = proteins,
            carbs = carbs,
            fats = fats
        )
        prefs.addFoodDiaryItem(item)
        _foodDiary.value = prefs.getFoodDiary()
    }

    fun removeFoodItem(id: String) {
        prefs.removeFoodDiaryItem(id)
        _foodDiary.value = prefs.getFoodDiary()
    }

    fun clearFoodDiary() {
        prefs.clearFoodDiary()
        _foodDiary.value = emptyList()
    }

    fun analyzeFoodWithGemini(foodQuery: String) {
        if (foodQuery.isBlank()) return
        _isAnalyzingFood.value = true
        _foodAnalysisResult.value = null
        viewModelScope.launch {
            try {
                val result = GeminiClient.queryFoodCalories(foodQuery)
                _foodAnalysisResult.value = result
            } catch (e: Exception) {
                _foodAnalysisResult.value = "Erro ao conectar com o Gemini: ${e.localizedMessage}"
            } finally {
                _isAnalyzingFood.value = false
            }
        }
    }

    fun clearFoodAnalysis() {
        _foodAnalysisResult.value = null
    }

    // --- Separate Workout Tracks Operations ---
    fun selectPlanType(type: String) {
        _selectedPlanType.value = type
        prefs.saveSelectedPlanType(type)
        val active = if (type == "AI") _aiCoachPlan.value else _userCustomPlan.value
        _activePlan.value = active
        prefs.saveActivePlan(active)
    }

    fun saveUserCustomPlan(plan: WorkoutPlan) {
        _userCustomPlan.value = plan
        prefs.saveUserCustomPlan(plan)
        if (_selectedPlanType.value == "CUSTOM") {
            _activePlan.value = plan
            prefs.saveActivePlan(plan)
        }
    }

    fun saveAiCoachPlan(plan: WorkoutPlan) {
        _aiCoachPlan.value = plan
        prefs.saveAiCoachPlan(plan)
        if (_selectedPlanType.value == "AI") {
            _activePlan.value = plan
            prefs.saveActivePlan(plan)
        }
    }

    // --- Save custom/AI generated plan ---
    fun savePlan(plan: WorkoutPlan) {
        // Keeps backwards compatibility while routing correctly
        if (plan.isCustomAiGenerated) {
            saveAiCoachPlan(plan)
        } else {
            saveUserCustomPlan(plan)
        }
    }

    // --- Clear Session History ---
    fun clearHistory() {
        prefs.clearCompletedSessions()
        _completedSessions.value = emptyList()
    }

    // --- AI Generator Command (Now launches to a separated AI plan track) ---
    fun generateAiWorkout() {
        _aiState.value = AiGenerationState.Loading
        viewModelScope.launch {
            try {
                val plan = GeminiClient.generateWorkout(_userProfile.value)
                if (plan != null) {
                    _aiState.value = AiGenerationState.Success(plan)
                    // Auto-save the plan to the AI Coach separate slot
                    saveAiCoachPlan(plan)
                    // Automatically switch selector to "AI" track when generated!
                    selectPlanType("AI")
                } else {
                    _aiState.value = AiGenerationState.Error(
                        "Certifique-se de configurar sua chave GEMINI_API_KEY no painel de Secrets ou tente novamente!"
                    )
                }
            } catch (e: Exception) {
                _aiState.value = AiGenerationState.Error("Falha de conexão: ${e.localizedMessage}")
            }
        }
    }

    fun dismissAiState() {
        _aiState.value = AiGenerationState.Idle
    }

    // --- AI Chat Service ---
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        
        val userMsg = ChatMessage("user", text, getCurrentTime())
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            try {
                // Compile conversation history context
                val historyContext = _chatMessages.value.takeLast(4).map { 
                    "${if (it.sender == "user") "Usuário" else "Coach"}: ${it.content}"
                }
                val result = GeminiClient.askCoach(text, _userProfile.value, historyContext)
                _chatMessages.value = _chatMessages.value + ChatMessage("coach", result, getCurrentTime())
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage("coach", "Desculpe, falhei ao processar. Detalhe: ${e.localizedMessage}", getCurrentTime())
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    // --- Workout Execution Tracking ---
    fun startWorkout(split: WorkoutSplit) {
        _activeSplit.value = split
        _checkedSets.value = emptyMap()
        _workoutTimerSeconds.value = 0L
        _restSecondsRemaining.value = 0
        _isRestTimerActive.value = false
        _isWorkoutRunning.value = true
        _showWorkoutCelebration.value = false
        
        // Populate default values for weights and reps
        val initialLoads = mutableMapOf<String, String>()
        val initialReps = mutableMapOf<String, String>()
        _exerciseRatings.value = emptyMap()
        _exercisePain.value = emptyMap()
        
        split.exercises.forEachIndexed { exIndex, ex ->
            val cleanRep = ex.reps.replace(Regex("[^0-9\\-]"), "").takeIf { it.isNotEmpty() } ?: "12"
            for (setIndex in 0 until ex.sets) {
                val key = "${exIndex}_$setIndex"
                initialLoads[key] = ""
                initialReps[key] = cleanRep
            }
        }
        _setLoads.value = initialLoads
        _setReps.value = initialReps

        // Start live chronometer
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _workoutTimerSeconds.value++
            }
        }
    }

    fun updateSetLoad(exerciseIndex: Int, setIndex: Int, load: String) {
        val key = "${exerciseIndex}_$setIndex"
        val current = _setLoads.value.toMutableMap()
        current[key] = load
        _setLoads.value = current
    }

    fun updateSetReps(exerciseIndex: Int, setIndex: Int, reps: String) {
        val key = "${exerciseIndex}_$setIndex"
        val current = _setReps.value.toMutableMap()
        current[key] = reps
        _setReps.value = current
    }

    fun setExerciseRating(exerciseIndex: Int, rating: String) {
        val current = _exerciseRatings.value.toMutableMap()
        current[exerciseIndex] = rating
        _exerciseRatings.value = current
    }

    fun reportExercisePain(exerciseIndex: Int, painArea: String) {
        val current = _exercisePain.value.toMutableMap()
        current[exerciseIndex] = painArea
        _exercisePain.value = current
    }

    fun toggleSetCheck(exerciseIndex: Int, setIndex: Int, restTimeSeconds: Int) {
        val key = "${exerciseIndex}_$setIndex"
        val current = _checkedSets.value.toMutableMap()
        val wasChecked = current[key] ?: false
        val newChecked = !wasChecked
        
        current[key] = newChecked
        _checkedSets.value = current

        // Standard fitness UX: trigger a resting timer when checking a set completed!
        if (newChecked) {
            startRestTimer(restTimeSeconds)
        } else {
            // Cancel rest timer if unchecked
            cancelRestTimer()
        }
    }

    private fun startRestTimer(duration: Int) {
        restJob?.cancel()
        _restSecondsRemaining.value = duration
        _isRestTimerActive.value = true
        
        restJob = viewModelScope.launch {
            while (_restSecondsRemaining.value > 0) {
                delay(1000)
                _restSecondsRemaining.value--
            }
            _isRestTimerActive.value = false
        }
    }

    fun skipRestTimer() {
        cancelRestTimer()
    }

    private fun cancelRestTimer() {
        restJob?.cancel()
        _restSecondsRemaining.value = 0
        _isRestTimerActive.value = false
    }

    fun cancelActiveWorkout() {
        timerJob?.cancel()
        restJob?.cancel()
        _isWorkoutRunning.value = false
        _activeSplit.value = null
        _checkedSets.value = emptyMap()
    }

    fun swapActiveWorkoutExercise(exerciseIndex: Int) {
        val split = _activeSplit.value ?: return
        val currentEx = split.exercises.getOrNull(exerciseIndex) ?: return

        val dbMatch = CuratedExercises.list.find { it.id == currentEx.id || it.name.equals(currentEx.name, ignoreCase = true) }
        val options = dbMatch?.smartSubstitutions ?: listOf()
        
        if (options.isNotEmpty()) {
            // Find a replacement from smartSubstitutions
            val replacementName = options.random()
            val dbReplacement = CuratedExercises.list.find { it.name.contains(replacementName, ignoreCase = true) || replacementName.contains(it.name, ignoreCase = true) }
            
            val updatedEx = currentEx.copy(
                id = dbReplacement?.id ?: "sub_${System.currentTimeMillis()}",
                name = dbReplacement?.name ?: replacementName,
                notes = "Alternado de: ${currentEx.name} (Ajuste de segurança)"
            )
            val newList = split.exercises.toMutableList()
            newList[exerciseIndex] = updatedEx
            _activeSplit.value = split.copy(exercises = newList)
            
            // Clear checkmarks for this exercise
            val currentChecked = _checkedSets.value.toMutableMap()
            for (setIndex in 0 until currentEx.sets) {
                currentChecked.remove("${exerciseIndex}_$setIndex")
            }
            _checkedSets.value = currentChecked
        }
    }

    fun swapActiveWorkoutExerciseWithChoice(exerciseIndex: Int, replacement: Exercise, reason: String = "") {
        val split = _activeSplit.value ?: return
        val currentEx = split.exercises.getOrNull(exerciseIndex) ?: return
        
        val updatedEx = currentEx.copy(
            id = replacement.id,
            name = replacement.name,
            notes = "Substituído por: ${replacement.name} ($reason)"
        )
        val newList = split.exercises.toMutableList()
        newList[exerciseIndex] = updatedEx
        _activeSplit.value = split.copy(exercises = newList)
        
        // Clear checkmarks, loads, reps for this exercise
        val currentChecked = _checkedSets.value.toMutableMap()
        val currentLoads = _setLoads.value.toMutableMap()
        val currentReps = _setReps.value.toMutableMap()
        for (setIndex in 0 until currentEx.sets) {
            currentChecked.remove("${exerciseIndex}_$setIndex")
            currentLoads.remove("${exerciseIndex}_$setIndex")
            currentReps.remove("${exerciseIndex}_$setIndex")
        }
        _checkedSets.value = currentChecked
        
        val cleanRep = currentEx.reps.replace(Regex("[^0-9\\-]"), "").takeIf { it.isNotEmpty() } ?: "12"
        for (setIndex in 0 until currentEx.sets) {
            val key = "${exerciseIndex}_$setIndex"
            currentLoads[key] = ""
            currentReps[key] = cleanRep
        }
        _setLoads.value = currentLoads
        _setReps.value = currentReps
        
        // Remove ratings and pain for this item
        val currentRatings = _exerciseRatings.value.toMutableMap()
        currentRatings.remove(exerciseIndex)
        _exerciseRatings.value = currentRatings
        
        val currentPain = _exercisePain.value.toMutableMap()
        currentPain.remove(exerciseIndex)
        _exercisePain.value = currentPain
    }

    fun swapPlanExercise(splitName: String, exerciseIndex: Int) {
        val plan = _activePlan.value
        val targetSplitIndex = plan.splits.indexOfFirst { it.name == splitName }
        if (targetSplitIndex == -1) return
        
        val split = plan.splits[targetSplitIndex]
        val currentEx = split.exercises.getOrNull(exerciseIndex) ?: return
        
        val dbMatch = CuratedExercises.list.find { it.id == currentEx.id || it.name.equals(currentEx.name, ignoreCase = true) }
        val options = dbMatch?.smartSubstitutions ?: listOf()
        
        if (options.isNotEmpty()) {
            val replacementName = options.random()
            val dbReplacement = CuratedExercises.list.find { it.name.contains(replacementName, ignoreCase = true) || replacementName.contains(it.name, ignoreCase = true) }
            
            val updatedEx = currentEx.copy(
                id = dbReplacement?.id ?: "sub_${System.currentTimeMillis()}",
                name = dbReplacement?.name ?: replacementName,
                notes = "Alternado de: ${currentEx.name} (Ajuste pré-treino)"
            )
            
            val updatedExercises = split.exercises.toMutableList()
            updatedExercises[exerciseIndex] = updatedEx
            
            val updatedSplits = plan.splits.toMutableList()
            updatedSplits[targetSplitIndex] = split.copy(exercises = updatedExercises)
            
            val updatedPlan = plan.copy(splits = updatedSplits)
            savePlan(updatedPlan)
        }
    }

    fun completeWorkout() {
        val split = _activeSplit.value ?: return
        val totalSecs = _workoutTimerSeconds.value
        
        // Count exercises completed (having at least 1 set checked)
        var completedCount = 0
        split.exercises.forEachIndexed { exIndex, ex ->
            var hasSetCompleted = false
            for (set in 0 until ex.sets) {
                if (_checkedSets.value["${exIndex}_$set"] == true) {
                    hasSetCompleted = true
                    break
                }
            }
            if (hasSetCompleted) {
                completedCount++
            }
        }

        // Save session history
        val newSession = CompletedSession(
            id = UUID.randomUUID().toString(),
            dateString = getCurrentFormattedDate(),
            routineTitle = _activePlan.value.title,
            splitName = split.name,
            durationSeconds = totalSecs,
            exercisesCompleted = completedCount,
            totalExercises = split.exercises.size
        )

        viewModelScope.launch {
            prefs.addCompletedSession(newSession)
            _completedSessions.value = prefs.getCompletedSessions()
            
            // Finish session & celebrate
            timerJob?.cancel()
            restJob?.cancel()
            _isWorkoutRunning.value = false
            _showWorkoutCelebration.value = true
        }
    }

    fun dismissCelebration() {
        _showWorkoutCelebration.value = false
        _activeSplit.value = null
    }

    // --- Core Helpers ---
    private fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }

    private fun getCurrentFormattedDate(): String {
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale("pt", "BR"))
        return sdf.format(Date())
    }
}
