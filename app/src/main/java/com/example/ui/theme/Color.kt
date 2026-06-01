package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

object ThemeConfig {
    var primaryColor by mutableStateOf(Color(0xFF84CC16))
    var secondaryColor by mutableStateOf(Color(0xFFA3E635))
    
    val palettes = listOf(
        ThemePalette("LIME_CYBER", "Verde Ciber", Color(0xFF84CC16), Color(0xFFA3E635)),
        ThemePalette("OCEAN_BLUE", "Azul Oceano", Color(0xFF0EA5E9), Color(0xFF38BDF8)),
        ThemePalette("EMERALD_MINT", "Verde Esmeralda", Color(0xFF10B981), Color(0xFF34D399)),
        ThemePalette("VOLCANO_ORANGE", "Laranja Vulcão", Color(0xFFF97316), Color(0xFFFB923C)),
        ThemePalette("ROYAL_AMETHYST", "Ametista Real", Color(0xFF8B5CF6), Color(0xFFA78BFA)),
        ThemePalette("CRIMSON_PULSE", "Carmesim Nobre", Color(0xFFEF4444), Color(0xFFF87171)),
        ThemePalette("MONOCHROME", "Preto & Branco", Color(0xFFFFFFFF), Color(0xFF94A3B8))
    )
}

data class ThemePalette(val code: String, val name: String, val primary: Color, val secondary: Color)

val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate600 = Color(0xFF475569)
val Slate400 = Color(0xFF94A3B8)
val Slate50 = Color(0xFFF8FAFC)

val Lime500: Color get() = ThemeConfig.primaryColor
val Lime600 = Color(0xFF65A30D)
val Lime400: Color get() = ThemeConfig.secondaryColor

val Amber400 = Color(0xFFFBBF24)
val Crimson400 = Color(0xFFF87171)

val Slate850 = Color(0xFF172033)
val Slate300 = Color(0xFFCBD5E1)
val Slate200 = Color(0xFFE2E8F0)
val Slate100 = Color(0xFFF1F5F9)
val Slate500 = Color(0xFF64748B)

val Emerald500 = Color(0xFF10B981)
val Amber500 = Color(0xFFF59E0B)
