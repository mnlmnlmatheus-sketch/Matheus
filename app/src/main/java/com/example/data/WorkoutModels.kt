package com.example.data

data class WorkoutExercise(
    val id: String, // Can be local id (e.g., "agachamento") or generated id
    val name: String,
    val sets: Int,
    val reps: String, // e.g., "10-12" or "8"
    val restSeconds: Int = 60,
    val notes: String = "" // Adaptations like "Foco em descida lenta para segurança"
)

data class WorkoutSplit(
    val name: String, // e.g. "Treino A: Superior", "Treino B: Inferior", "Full Body"
    val description: String = "",
    val exercises: List<WorkoutExercise>
)

data class WorkoutPlan(
    val title: String, // e.g. "Treino Inteligente - Foco Pernas"
    val objective: String,
    val level: String,
    val equipment: String,
    val frequencyDays: Int,
    val limitations: String,
    val splits: List<WorkoutSplit>,
    val createdDate: String,
    val isCustomAiGenerated: Boolean = false,
    val coachNotes: String = "" // AI Coach explanations of why this routine was built
)

data class CompletedSession(
    val id: String,
    val dateString: String, // e.g., "26 Mai, 11:40"
    val routineTitle: String,
    val splitName: String,
    val durationSeconds: Long,
    val exercisesCompleted: Int,
    val totalExercises: Int
)

data class UserProfile(
    val name: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val objective: String, // "Hipertrofia", "Emagrecimento", "Força", "Resistência", "Mobilidade", "Saúde Geral"
    val level: String, // "Iniciante", "Intermediário", "Avançado"
    val location: String, // "Casa", "Academia", "Ambos"
    val equipments: List<String>, // list of available equipment
    val frequency: Int, // days per week: 1 to 7
    val durationMinutes: Int, // average workout time in minutes
    val limitations: String, // "Nenhuma", "Lombar", "Joelho", "Ombro", etc.
    val preferences: String // extra preferences prompt
)

data class FoodDiaryItem(
    val id: String,
    val name: String,
    val dateString: String, // e.g. "Hoje, 12:40"
    val calories: Int,
    val proteins: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0
)

