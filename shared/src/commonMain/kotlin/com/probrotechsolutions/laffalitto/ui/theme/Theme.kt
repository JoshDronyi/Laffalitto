package com.probrotechsolutions.laffalitto.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.stateholder.LocalSavedStateHolder
import moe.tlaster.precompose.stateholder.LocalStateHolder
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.stateholder.StateHolder

// ---------------------------------------------------------------------------
// Palette — warm amber primary, golden secondary, fresh-mint tertiary
// Feels sunny, playful and light-hearted without being garish.
// ---------------------------------------------------------------------------

private val LightColorScheme = lightColorScheme(
    primary              = Color(0xFFBE6B00),   // Warm amber — buttons, FABs, active indicators
    onPrimary            = Color(0xFFFFFFFF),
    primaryContainer     = Color(0xFFFFDDB5),   // Soft mango — chip/badge backgrounds
    onPrimaryContainer   = Color(0xFF3C1A00),

    secondary            = Color(0xFF7B5800),   // Deep gold — secondary actions
    onSecondary          = Color(0xFFFFFFFF),
    secondaryContainer   = Color(0xFFFFDEA8),   // Pale gold — nav-bar indicator, toggles
    onSecondaryContainer = Color(0xFF271900),

    tertiary             = Color(0xFF52693A),   // Fresh green — positive feedback accents
    onTertiary           = Color(0xFFFFFFFF),
    tertiaryContainer    = Color(0xFFD4EFBC),   // Mint — tag backgrounds
    onTertiaryContainer  = Color(0xFF122000),

    error                = Color(0xFFBA1A1A),
    onError              = Color(0xFFFFFFFF),
    errorContainer       = Color(0xFFFFDAD6),
    onErrorContainer     = Color(0xFF410002),

    background           = Color(0xFFFFFBFF),   // Warm pure white
    onBackground         = Color(0xFF1E1B18),

    surface              = Color(0xFFFFFBFF),
    onSurface            = Color(0xFF1E1B18),
    surfaceVariant       = Color(0xFFF2E0CE),   // Sandy cream — card surfaces
    onSurfaceVariant     = Color(0xFF50453A),

    outline              = Color(0xFF827368),
    outlineVariant       = Color(0xFFD5C3B5),

    inverseSurface       = Color(0xFF342F2B),
    inverseOnSurface     = Color(0xFFF9EEE7),
    inversePrimary       = Color(0xFFFFB867),

    surfaceTint          = Color(0xFFBE6B00),
)

private val DarkColorScheme = darkColorScheme(
    primary              = Color(0xFFFFB867),   // Bright amber — readable on dark chocolate
    onPrimary            = Color(0xFF642F00),
    primaryContainer     = Color(0xFF8F4D00),
    onPrimaryContainer   = Color(0xFFFFDDB5),

    secondary            = Color(0xFFE9C16C),   // Warm gold
    onSecondary          = Color(0xFF3F2D00),
    secondaryContainer   = Color(0xFF5B4200),
    onSecondaryContainer = Color(0xFFFFDEA8),

    tertiary             = Color(0xFFB8D39F),   // Soft mint
    onTertiary           = Color(0xFF263614),
    tertiaryContainer    = Color(0xFF3C5125),
    onTertiaryContainer  = Color(0xFFD4EFBC),

    error                = Color(0xFFFFB4AB),
    onError              = Color(0xFF690005),
    errorContainer       = Color(0xFF93000A),
    onErrorContainer     = Color(0xFFFFDAD6),

    background           = Color(0xFF161310),   // Warm near-black (chocolate)
    onBackground         = Color(0xFFEBE0D9),

    surface              = Color(0xFF161310),
    onSurface            = Color(0xFFEBE0D9),
    surfaceVariant       = Color(0xFF50453A),
    onSurfaceVariant     = Color(0xFFD5C3B5),

    outline              = Color(0xFF9D8E82),
    outlineVariant       = Color(0xFF50453A),

    inverseSurface       = Color(0xFFEBE0D9),
    inverseOnSurface     = Color(0xFF342F2B),
    inversePrimary       = Color(0xFFBE6B00),

    surfaceTint          = Color(0xFFFFB867),
)

// Slightly rounder corners give the UI a friendlier, more approachable feel.
private val LaffaLittoShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun LaffaLittoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // PreCompose state holders must wrap the MaterialTheme so the nav host
    // and view-model scopes have access to composition locals.
    CompositionLocalProvider(LocalStateHolder provides StateHolder()) {
        val savedStateHolder = SavedStateHolder(
            "LaffaLittoRoot",
            SaveableStateRegistry(restoredValues = mapOf(), canBeSaved = { true })
        )
        CompositionLocalProvider(LocalSavedStateHolder provides savedStateHolder) {
            MaterialTheme(
                colorScheme = colorScheme,
                shapes      = LaffaLittoShapes,
                content     = content
            )
        }
    }
}
