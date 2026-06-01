package com.example.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class WorkoutPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("treino_inteligente_prefs", Context.MODE_PRIVATE)
    
    // Setup Moshi with standard Kotlin adapter factory
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
        
    private val workoutPlanAdapter = moshi.adapter(WorkoutPlan::class.java)
    private val userProfileAdapter = moshi.adapter(UserProfile::class.java)
    
    private val completedSessionsListType = Types.newParameterizedType(List::class.java, CompletedSession::class.java)
    private val completedSessionsAdapter = moshi.adapter<List<CompletedSession>>(completedSessionsListType)

    private val foodDiaryListType = Types.newParameterizedType(List::class.java, FoodDiaryItem::class.java)
    private val foodDiaryAdapter = moshi.adapter<List<FoodDiaryItem>>(foodDiaryListType)

    fun getSelectedTheme(): String {
        return prefs.getString("selected_theme_palette", "LIME_CYBER") ?: "LIME_CYBER"
    }

    fun saveSelectedTheme(theme: String) {
        prefs.edit().putString("selected_theme_palette", theme).apply()
    }

    fun getFoodDiary(): List<FoodDiaryItem> {
        val json = prefs.getString("food_diary", null)
        if (json != null) {
            try {
                val items = foodDiaryAdapter.fromJson(json)
                if (items != null) return items
            } catch (e: Exception) {
                // Ignore
            }
        }
        return emptyList()
    }

    fun saveFoodDiary(items: List<FoodDiaryItem>) {
        val json = foodDiaryAdapter.toJson(items)
        prefs.edit().putString("food_diary", json).apply()
    }

    fun addFoodDiaryItem(item: FoodDiaryItem) {
        val current = getFoodDiary().toMutableList()
        current.add(0, item)
        saveFoodDiary(current)
    }

    fun removeFoodDiaryItem(id: String) {
        val current = getFoodDiary().filter { it.id != id }
        saveFoodDiary(current)
    }

    fun clearFoodDiary() {
        prefs.edit().remove("food_diary").apply()
    }

    fun saveUserCustomPlan(plan: WorkoutPlan) {
        val json = workoutPlanAdapter.toJson(plan)
        prefs.edit().putString("user_custom_workout_plan", json).apply()
    }

    fun getUserCustomPlan(): WorkoutPlan {
        val json = prefs.getString("user_custom_workout_plan", null)
        if (json != null) {
            try {
                val plan = workoutPlanAdapter.fromJson(json)
                if (plan != null) return plan
            } catch (e: Exception) {
                // Ignore
            }
        }
        val defaultPlan = createDefaultPlan()
        return defaultPlan.copy(
            title = "Meu Treino Personalizado", 
            splits = defaultPlan.splits.mapIndexed { index, split ->
                split.copy(name = split.name.replace("Treino ", "Divisão Custom "))
            }
        )
    }

    fun saveAiCoachPlan(plan: WorkoutPlan) {
        val json = workoutPlanAdapter.toJson(plan)
        prefs.edit().putString("ai_coach_workout_plan", json).apply()
    }

    fun getAiCoachPlan(): WorkoutPlan {
        val json = prefs.getString("ai_coach_workout_plan", null)
        if (json != null) {
            try {
                val plan = workoutPlanAdapter.fromJson(json)
                if (plan != null) return plan
            } catch (e: Exception) {
                // Ignore
            }
        }
        return createDefaultPlan().copy(title = "Rotina IA Biomecânica", isCustomAiGenerated = true)
    }

    fun getSelectedPlanType(): String {
        return prefs.getString("selected_plan_type", "AI") ?: "AI"
    }

    fun saveSelectedPlanType(type: String) {
        prefs.edit().putString("selected_plan_type", type).apply()
    }

    fun getHighRefreshEnabled(): Boolean {
        return prefs.getBoolean("high_refresh_rate_enabled", true)
    }

    fun saveHighRefreshEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("high_refresh_rate_enabled", enabled).apply()
    }

    fun saveActivePlan(plan: WorkoutPlan) {
        val json = workoutPlanAdapter.toJson(plan)
        prefs.edit().putString("active_workout_plan", json).apply()
    }

    fun getActivePlan(): WorkoutPlan {
        val json = prefs.getString("active_workout_plan", null)
        if (json != null) {
            try {
                val plan = workoutPlanAdapter.fromJson(json)
                if (plan != null) return plan
            } catch (e: Exception) {
                // Return default plan on error
            }
        }
        return createDefaultPlan()
    }

    fun saveUserProfile(profile: UserProfile) {
        val json = userProfileAdapter.toJson(profile)
        prefs.edit().putString("user_profile", json).apply()
    }

    fun getUserProfile(): UserProfile {
        val json = prefs.getString("user_profile", null)
        if (json != null) {
            try {
                val profile = userProfileAdapter.fromJson(json)
                if (profile != null) return profile
            } catch (e: Exception) {
                // Return fallback profile
            }
        }
        return UserProfile(
            name = "",
            age = 26,
            weight = 72.0,
            height = 175.0,
            objective = "Saúde Geral",
            level = "Iniciante",
            location = "Ambos",
            equipments = listOf("Halteres", "Peso Corporal"),
            frequency = 3,
            durationMinutes = 45,
            limitations = "Nenhuma",
            preferences = "Alternar foco em força e mobilidade"
        )
    }

    fun saveCompletedSessions(sessions: List<CompletedSession>) {
        val json = completedSessionsAdapter.toJson(sessions)
        prefs.edit().putString("completed_sessions", json).apply()
    }

    fun getCompletedSessions(): List<CompletedSession> {
        val json = prefs.getString("completed_sessions", null)
        if (json != null) {
            try {
                val sessions = completedSessionsAdapter.fromJson(json)
                if (sessions != null) return sessions
            } catch (e: Exception) {
                // Return empty on error
            }
        }
        return emptyList()
    }

    fun addCompletedSession(session: CompletedSession) {
        val current = getCompletedSessions().toMutableList()
        current.add(0, session) // Insert at the top
        saveCompletedSessions(current)
    }

    fun clearCompletedSessions() {
        prefs.edit().remove("completed_sessions").apply()
    }

    // Generates a stellar default starter training plan using our curated exercises!
    private fun createDefaultPlan(): WorkoutPlan {
        return WorkoutPlan(
            title = "Ajuste Starter: Força e Mobilidade",
            objective = "Saúde & Fortalecimento Geral",
            level = "Iniciante",
            equipment = "Halteres & Peso Corporal",
            frequencyDays = 2,
            limitations = "Nenhuma",
            splits = listOf(
                WorkoutSplit(
                    name = "Treino A: Superior e Core",
                    description = "Foco em peito, costas, ombros e controle postural.",
                    exercises = listOf(
                        WorkoutExercise(
                            id = "supino_halter",
                            name = "Supino com Halteres",
                            sets = 3,
                            reps = "10 a 12 reps",
                            restSeconds = 60,
                            notes = "Aperte as costas no banco de forma firme para estabilizar seus ombros."
                        ),
                        WorkoutExercise(
                            id = "remada_curvada",
                            name = "Remada Curvada com Halteres",
                            sets = 3,
                            reps = "10 a 12 reps",
                            restSeconds = 60,
                            notes = "Puxe direcionando seus cotovelos ao quadril para ativar as dorsais."
                        ),
                        WorkoutExercise(
                            id = "desenvolvimento_sentado",
                            name = "Desenvolvimento de Ombros",
                            sets = 3,
                            reps = "10 reps",
                            restSeconds = 60,
                            notes = "Mantenha o abdômen contraído; desça de forma lenta e segura."
                        ),
                        WorkoutExercise(
                            id = "elevacao_lateral",
                            name = "Elevação Lateral com Halteres",
                            sets = 3,
                            reps = "12 reps",
                            restSeconds = 60,
                            notes = "Eleve os braços levemente para frente (plano escapular) para poupar o manguito."
                        ),
                        WorkoutExercise(
                            id = "rosca_martelo",
                            name = "Rosca Martelo",
                            sets = 3,
                            reps = "12 reps",
                            restSeconds = 60,
                            notes = "Palmas voltadas para dentro. Pegada firme sem girar o punho."
                        ),
                        WorkoutExercise(
                            id = "prancha",
                            name = "Prancha Isométrica",
                            sets = 3,
                            reps = "30 a 45s segurando",
                            restSeconds = 60,
                            notes = "Corpo linear. Ative coxas e contraia glúteos e abdômen."
                        )
                    )
                ),
                WorkoutSplit(
                    name = "Treino B: Inferior e Quadril",
                    description = "Foco em perna completa, mobilidade de joelhos e glúteos.",
                    exercises = listOf(
                        WorkoutExercise(
                            id = "agachamento_goblet",
                            name = "Agachamento Goblet",
                            sets = 3,
                            reps = "10 a 12 reps",
                            restSeconds = 60,
                            notes = "Mantenha os calcanhares no chão e joelhos acompanhando a direção dos pés."
                        ),
                        WorkoutExercise(
                            id = "rdl",
                            name = "Levantamento Terra RDL (Stiff)",
                            sets = 3,
                            reps = "10 reps",
                            restSeconds = 75,
                            notes = "Empurre bem o bumbum para trás até sentir alongar a coxa posterior."
                        ),
                        WorkoutExercise(
                            id = "elevacao_pelvica",
                            name = "Elevação Pélvica (Hip Thrust)",
                            sets = 3,
                            reps = "12 reps",
                            restSeconds = 60,
                            notes = "Trave por 1 segundo no topo, espremendo bem os glúteos."
                        ),
                        WorkoutExercise(
                            id = "extensora",
                            name = "Cadeira Extensora",
                            sets = 3,
                            reps = "12 a 15 reps",
                            restSeconds = 60,
                            notes = "Estenda lentamente. Controle total na descida sem despencar o peso."
                        ),
                        WorkoutExercise(
                            id = "calf_raises",
                            name = "Gêmeos em Pé (Panturrilhas)",
                            sets = 3,
                            reps = "15 reps",
                            restSeconds = 65,
                            notes = "Suba até contração máxima e alongue bem na descida de forma controlada."
                        ),
                        WorkoutExercise(
                            id = "bird_dog",
                            name = "Perdigueiro (Bird Dog)",
                            sets = 3,
                            reps = "10 reps alternadas",
                            restSeconds = 60,
                            notes = "Mantenha o quadril e tronco estáveis, estendendo braço e perna opostos."
                        )
                    )
                )
            ),
            createdDate = "26 Mai 2026",
            isCustomAiGenerated = false,
            coachNotes = "Este é um treino de base desenhado de forma biomecanicamente equilibrada com 6 exercícios de alta qualidade por split. Ele trabalha a força de empurrar, puxar e pernas de forma simétrica e segura para iniciar sua jornada ativa confiando em cada movimento!"
        )
    }
}
