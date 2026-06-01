package com.example.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.UserProfile
import com.example.data.WorkoutPlan
import com.example.data.WorkoutSplit
import com.example.data.WorkoutExercise
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val workoutPlanAdapter = moshi.adapter(WorkoutPlan::class.java)

    /**
     * Sends a direct REST request to Gemini to generate a personalized structured workout.
     */
    suspend fun generateWorkout(profile: UserProfile): WorkoutPlan? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e(TAG, "Gemini API key is not configured in AI Studio Secrets panel.")
            return@withContext null
        }

        val prompt = buildWorkoutGenerationPrompt(profile)

        val requestBodyJson = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
            put("systemInstruction", JSONObject().apply {
                put("parts", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", "Você é o Coach Forte, um personal trainer e biomecânico de elite focado em prescrever treinos inteligentes, seguros e sob medida. Sua missão é gerar treinos adaptados à rotina e limitações do aluno. Responda exclusivamente com o objeto JSON solicitado, sem formatações Markdown extras reguladas.")
                    })
                })
            })
        }

        val request = Request.Builder()
            .url("$BASE_URL?key=$apiKey")
            .post(requestBodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                Log.e(TAG, "API call failed with code ${response.code}: $responseBody")
                return@withContext null
            }

            val parsedText = parseGeminiResponseText(responseBody) ?: return@withContext null
            
            // Clean markdown syntax blocks if returned by mistake
            val cleanedJson = cleanJsonString(parsedText)
            
            Log.d(TAG, "Successfully text parsed JSON from Gemini: $cleanedJson")

            // Deserialize with Moshi
            val rawPlan = workoutPlanAdapter.fromJson(cleanedJson)
            if (rawPlan != null) {
                // Complete fields that might be missing
                return@withContext WorkoutPlan(
                    title = rawPlan.title,
                    objective = profile.objective,
                    level = profile.level,
                    equipment = profile.equipments.joinToString(", "),
                    frequencyDays = profile.frequency,
                    limitations = profile.limitations,
                    splits = rawPlan.splits,
                    createdDate = "Hoje",
                    isCustomAiGenerated = true,
                    coachNotes = rawPlan.coachNotes
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini workout generation: ${e.message}", e)
        }
        return@withContext null
    }

    /**
     * Quick workout helper/Q&A chat assistant.
     */
    suspend fun askCoach(question: String, profile: UserProfile, history: List<String> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Opa! Para falar com o Coach Forte via Inteligência Artificial, você precisa configurar a chave `GEMINI_API_KEY` no painel de Secrets do AI Studio. Por enquanto, posso tirar suas dúvidas se houver chave configurada!"
        }

        val systemPrompt = """
            Você é o Coach Forte, um personal trainer de elite, atencioso, motivador e especialista em biomecânica de treino.
            Responda em português de forma clara, direta, motivadora e prática, sem terminologias complicadas desnecessárias.
            Prefira listar dicas em formato de tópicos rápidos e fáceis de ler na academia.
            
            Contexto do usuário:
            - Objetivo: ${profile.objective}
            - Nível: ${profile.level}
            - Equipamentos: ${profile.equipments.joinToString(", ")} (Local de Treino: ${profile.location})
            - Limitações físicas: ${profile.limitations}
            - Idade: ${profile.age} anos, Peso: ${profile.weight} kg, Altura: ${profile.height} cm
            - Tempo de Treino Disponível: ${profile.durationMinutes} minutos por sessão
            
            DIRETRIZES DE SEGURANÇA E QUALIDADE CRÍTICAS (DEVE SEGUIR À RISCA):
            1. Use a biblioteca de exercícios curados do aplicativo e fontes científicas altamente confiáveis de educação física.
            2. NUNCA invente ou forneça diagnósticos ou orientações médicas definitivas. Você é um treinador, não um médico.
            3. Em caso de relato de dor forte, desconforto agudo, lesão suspeita ou sintomas médicos preocupantes (como falta de ar extrema ou tontura), oriente IMEDIATAMENTE o usuário a PARAR o exercício físico, descansar e PROCURAR auxílio de um profissional de saúde qualificado.
            4. Evite recomendar treinos de alto volume ou intensidade excessivos que fujam do nível do usuário (especialmente se for Iniciante). Evite prescrever movimentos excessivamente técnicos sem a devida orientação de segurança.
            5. Nunca faça promessas exageradas de resultados milagrosos de perda de peso ou ganho muscular. Mantenha as expectativas realistas e pautadas no esforço consistente e seguro.
            
            Se o usuário trouxer perguntas de dores ou limitações (ex: dor no joelho ou lombar), responda de forma ultra protetora, explicando as adaptações inteligentes e sugerindo alternativas seguras como substituir agachamento livre pesado por agachamento na parede com bola ou elevação pélvica de solo.
        """.trimIndent()

        // Combine history into a small unified prompt
        val promptBuilder = StringBuilder()
        history.forEach { promptBuilder.append(it).append("\n") }
        promptBuilder.append("Usuário pergunta: $question")

        val requestBodyJson = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", promptBuilder.toString())
                        })
                    })
                })
            })
            put("systemInstruction", JSONObject().apply {
                put("parts", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", systemPrompt)
                    })
                })
            })
        }

        val request = Request.Builder()
            .url("$BASE_URL?key=$apiKey")
            .post(requestBodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                return@withContext "Erro ao contatar o Coach: Código ${response.code}"
            }
            return@withContext parseGeminiResponseText(responseBody) ?: "Nenhuma resposta gerada."
        } catch (e: Exception) {
            return@withContext "Não consegui conexão no momento. Verifique sua chave ou internet. Detalhes: ${e.localizedMessage}"
        }
    }

    private fun parseGeminiResponseText(jsonResponse: String): String? {
        try {
            val obj = JSONObject(jsonResponse)
            val candidates = obj.getJSONArray("candidates")
            if (candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")
                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse text from raw JSON response", e)
        }
        return null
    }

    private fun cleanJsonString(raw: String): String {
        var str = raw.trim()
        if (str.startsWith("```json")) {
            str = str.substring("```json".length)
        } else if (str.startsWith("```")) {
            str = str.substring("```".length)
        }
        if (str.endsWith("```")) {
            str = str.substring(0, str.length - "```".length)
        }
        return str.trim()
    }

    private fun buildWorkoutGenerationPrompt(profile: UserProfile): String {
        val mappingObj = when (profile.objective) {
            "Hipertrofia" -> "Hipertrofia - Ganho de Volume e Massa muscular"
            "Emagrecimento" -> "Emagrecimento - Déficit calórico, circuito ou alta intensidade com intervalos otimizados"
            "Força" -> "Ganho de Força e potência muscular"
            "Resistência" -> "Resistência muscular e condicionamento aeróbio aprimorado"
            "Mobilidade" -> "Mobilidade articular profunda, flexibilidade e alongamento seguro"
            else -> "Saúde Geral, fortalecimento articular geral e bem-estar físico"
        }

        val mappingEq = "Aparelhos e acessórios disponíveis: " + profile.equipments.joinToString(", ") + ". Treina em: " + profile.location

        val mappingLim = when (profile.limitations) {
            "Nenhuma" -> "Zero limitações corporais relatadas. Projete de forma equilibrada."
            "Lombar" -> "Possui desconfortos ou dor na lombar. Proteger 100% a lombar! EVITAR Levantamento Terra pesado ou Agachamentos pesados com barra de coluna. Substituir por Elevação Pélvica, Extensora, Prancha ou Agachamento Goblet focando em tronco vertical nas notas."
            "Joelho" -> "Possui desconfortos ou dor nos joelhos. EVITAR agachamentos profundos sob alta tensão ou impacto. Ajustar para amplitude reduzida segura, usar notas biomecânicas de segurança excelentes."
            "Ombro" -> "Possui dor ou limitação no Ombro. EVITAR desenvolvimento de ombros excessivamente aberto ou supino com cotovelos muito afastados que geram impacto subacromial. Substituir por flexões com apoios fechados ou desenvolvimento fechado (pegada neutra) mais seguro nas notas."
            else -> "O aluno relatou restrição específica: ${profile.limitations}. Cuidado extremo ao selecionar exercícios para esta região."
        }

        return """
            Gere uma rotina de treinos completa, de acordo com o seguinte perfil de aluno:
            - Aluno: ${profile.name}, Idade: ${profile.age} anos, Peso: ${profile.weight} kg, Altura: ${profile.height} cm.
            - Tempo médio do treino por sessão: ${profile.durationMinutes} minutos.
            - Objetivo de treino: $mappingObj
            - Nível de Experiência: ${profile.level}
            - Equipamentos e local disponíveis: $mappingEq
            - Frequência semanal recomendada: ${profile.frequency} treinos por semana (por favor, gere exatamente ${profile.frequency} divisões de treinos!).
            - Limitações físicas importantes: $mappingLim
            - Preferências adicionais do aluno: ${profile.preferences}

            DIRETRIZES TÉCNICAS:
            1. Cada treino (split) deve ter obrigatoriamente de 5 a 8 exercícios selecionados (recomende em média 6 ou 7 exercícios por split de forma a preencher inteligentemente o tempo de treino disponível de ${profile.durationMinutes} minutos), abrangendo de forma completa o grupo muscular planejado.
            2. Cada exercício deve especificar o número de séries (sets, ex: 3 ou 4), a faixa de repetições (reps: ex: '10 a 12' ou '8 a 10'), o tempo de descanso (restSeconds: ex: 60 ou 90) e o campo 'notes' com indicações precisas em português de execução de acordo com o nível e limitações.
            3. Use o campo 'coachNotes' geral para justificar por que essa rotina foi montada, elogiando as escolhas do aluno e detalhando cientificamente as substituições para que ele evite lesões!

            A sua resposta deve ser EXCLUSIVAMENTE um objeto JSON válido estruturado exatamente assim (não inclua palavras extras, introduções ou conclusões fora do JSON):
            {
              "title": "Ajuste Biomecânico Forte: [Insira um subtítulo curto e empoderador em português]",
              "splits": [
                {
                  "name": "Treino A: [Nome do split, ex: Superior Foco Puxar]",
                  "description": "Foco muscular e postura do treino.",
                  "exercises": [
                    {
                      "id": "supino_halter",
                      "name": "Supino com Halteres",
                      "sets": 3,
                      "reps": "8 a 12 reps",
                      "restSeconds": 60,
                      "notes": "Mantenha as escápulas aduzidas. Ajuste anatômico: evite estender totalmente o ombro se doer."
                    }
                  ]
                }
              ],
              "coachNotes": "Exponha aqui um texto motivador e acolhedor (1 a 2 parágrafos) justificando as adaptações de segurança feitas para este aluno específico."
            }
        """.trimIndent()
    }

    /**
     * Queries Gemini to estimate portions, macronutrients, and calories for a specific food description.
     */
    suspend fun queryFoodCalories(foodQuery: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Chave de API do Gemini não configurada corretamente nos Secrets."
        }
        val systemPrompt = """
            Você é um Nutricionista e Contador de Calorias Inteligente.
            Seu objetivo é analisar as refeições ou alimentos inseridos pelo usuário.
            Estime com base científica de tabelas nutricionais (TBCA/TACO) de forma aproximada:
            1. O peso provável ou padrão da porção (em gramas).
            2. A quantidade de Macronutrientes (Proteínas, Carboidratos, Gorduras em gramas).
            3. A quantidade total de Calorias (kcal).
            
            Sua resposta de análise deve ser amigável e explicativa em tópicos claros, e DEVE terminar com uma linha especial contendo os valores separados por ponto e vírgula contendo a soma consolidada de calorias, proteínas, carboidratos e gorduras para podermos registrar no diário. A última linha deve começar exatamente com '[RESULT]' e ter os números puros:
            Exemplo da linha final de resultado consolidado:
            [RESULT];350;25;40;8
            Onde: 350 é calorias (kcal), 25g de proteína, 40g de carboidratos, 8g de gorduras.
            
            Mantenha um tom profissional, acolhedor e focado na saúde.
        """.trimIndent()

        val requestBodyJson = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "Analise e estime as calorias para a seguinte refeição ou comida: $foodQuery")
                        })
                    })
                })
            })
            put("systemInstruction", JSONObject().apply {
                put("parts", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", systemPrompt)
                    })
                })
            })
        }

        val request = Request.Builder()
            .url("$BASE_URL?key=$apiKey")
            .post(requestBodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                return@withContext "Erro ao consultar informações nutricionais: ${response.code}"
            }
            return@withContext parseGeminiResponseText(responseBody) ?: "Nenhuma estimativa nutricional gerada."
        } catch (e: Exception) {
            return@withContext "Falha ao consultar o banco nutricional do Gemini: ${e.localizedMessage}"
        }
    }
}

