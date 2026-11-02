package com.example.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkAccent,
    onPrimary = DarkOnAccent,
    primaryContainer = DarkLine,
    onPrimaryContainer = DarkInk,
    secondary = DarkAccent,
    onSecondary = DarkOnAccent,
    background = DarkBg,
    onBackground = DarkInk,
    surface = DarkCard,
    onSurface = DarkInk,
    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkMuted,
    outline = DarkLine,
    error = DarkBad,
    onError = Color.White,
    errorContainer = DarkBadBg,
    onErrorContainer = DarkBad
)

private val LightColorScheme = lightColorScheme(
    primary = LightAccent,
    onPrimary = LightOnAccent,
    primaryContainer = LightOkBg,
    onPrimaryContainer = LightInk,
    secondary = LightAccent,
    onSecondary = LightOnAccent,
    background = LightBg,
    onBackground = LightInk,
    surface = LightCard,
    onSurface = LightInk,
    surfaceVariant = LightCard,
    onSurfaceVariant = LightMuted,
    outline = LightLine,
    error = LightBad,
    onError = Color.White,
    errorContainer = LightBadBg,
    onErrorContainer = LightBad
)

@Immutable
data class ExtendedColors(
    val ok: Color,
    val okBg: Color,
    val bad: Color,
    val badBg: Color
)

val LightExtendedColors = ExtendedColors(
    ok = LightOk,
    okBg = LightOkBg,
    bad = LightBad,
    badBg = LightBadBg
)

val DarkExtendedColors = ExtendedColors(
    ok = DarkOk,
    okBg = DarkOkBg,
    bad = DarkBad,
    badBg = DarkBadBg
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

object AppTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}

private val ThemeAnimationSpec: AnimationSpec<Color> = tween(
    durationMillis = 400,
    easing = FastOutSlowInEasing
)

@Composable
private fun animateColorScheme(
    targetColorScheme: ColorScheme,
    animationSpec: AnimationSpec<Color> = ThemeAnimationSpec
): ColorScheme {
    val primary by animateColorAsState(targetColorScheme.primary, animationSpec, label = "color_primary")
    val onPrimary by animateColorAsState(targetColorScheme.onPrimary, animationSpec, label = "color_onPrimary")
    val primaryContainer by animateColorAsState(targetColorScheme.primaryContainer, animationSpec, label = "color_primaryContainer")
    val onPrimaryContainer by animateColorAsState(targetColorScheme.onPrimaryContainer, animationSpec, label = "color_onPrimaryContainer")
    val inversePrimary by animateColorAsState(targetColorScheme.inversePrimary, animationSpec, label = "color_inversePrimary")
    val secondary by animateColorAsState(targetColorScheme.secondary, animationSpec, label = "color_secondary")
    val onSecondary by animateColorAsState(targetColorScheme.onSecondary, animationSpec, label = "color_onSecondary")
    val secondaryContainer by animateColorAsState(targetColorScheme.secondaryContainer, animationSpec, label = "color_secondaryContainer")
    val onSecondaryContainer by animateColorAsState(targetColorScheme.onSecondaryContainer, animationSpec, label = "color_onSecondaryContainer")
    val tertiary by animateColorAsState(targetColorScheme.tertiary, animationSpec, label = "color_tertiary")
    val onTertiary by animateColorAsState(targetColorScheme.onTertiary, animationSpec, label = "color_onTertiary")
    val tertiaryContainer by animateColorAsState(targetColorScheme.tertiaryContainer, animationSpec, label = "color_tertiaryContainer")
    val onTertiaryContainer by animateColorAsState(targetColorScheme.onTertiaryContainer, animationSpec, label = "color_onTertiaryContainer")
    val background by animateColorAsState(targetColorScheme.background, animationSpec, label = "color_background")
    val onBackground by animateColorAsState(targetColorScheme.onBackground, animationSpec, label = "color_onBackground")
    val surface by animateColorAsState(targetColorScheme.surface, animationSpec, label = "color_surface")
    val onSurface by animateColorAsState(targetColorScheme.onSurface, animationSpec, label = "color_onSurface")
    val surfaceVariant by animateColorAsState(targetColorScheme.surfaceVariant, animationSpec, label = "color_surfaceVariant")
    val onSurfaceVariant by animateColorAsState(targetColorScheme.onSurfaceVariant, animationSpec, label = "color_onSurfaceVariant")
    val surfaceTint by animateColorAsState(targetColorScheme.surfaceTint, animationSpec, label = "color_surfaceTint")
    val inverseSurface by animateColorAsState(targetColorScheme.inverseSurface, animationSpec, label = "color_inverseSurface")
    val inverseOnSurface by animateColorAsState(targetColorScheme.inverseOnSurface, animationSpec, label = "color_inverseOnSurface")
    val error by animateColorAsState(targetColorScheme.error, animationSpec, label = "color_error")
    val onError by animateColorAsState(targetColorScheme.onError, animationSpec, label = "color_onError")
    val errorContainer by animateColorAsState(targetColorScheme.errorContainer, animationSpec, label = "color_errorContainer")
    val onErrorContainer by animateColorAsState(targetColorScheme.onErrorContainer, animationSpec, label = "color_onErrorContainer")
    val outline by animateColorAsState(targetColorScheme.outline, animationSpec, label = "color_outline")
    val outlineVariant by animateColorAsState(targetColorScheme.outlineVariant, animationSpec, label = "color_outlineVariant")
    val scrim by animateColorAsState(targetColorScheme.scrim, animationSpec, label = "color_scrim")
    val surfaceBright by animateColorAsState(targetColorScheme.surfaceBright, animationSpec, label = "color_surfaceBright")
    val surfaceDim by animateColorAsState(targetColorScheme.surfaceDim, animationSpec, label = "color_surfaceDim")
    val surfaceContainer by animateColorAsState(targetColorScheme.surfaceContainer, animationSpec, label = "color_surfaceContainer")
    val surfaceContainerHigh by animateColorAsState(targetColorScheme.surfaceContainerHigh, animationSpec, label = "color_surfaceContainerHigh")
    val surfaceContainerHighest by animateColorAsState(targetColorScheme.surfaceContainerHighest, animationSpec, label = "color_surfaceContainerHighest")
    val surfaceContainerLow by animateColorAsState(targetColorScheme.surfaceContainerLow, animationSpec, label = "color_surfaceContainerLow")
    val surfaceContainerLowest by animateColorAsState(targetColorScheme.surfaceContainerLowest, animationSpec, label = "color_surfaceContainerLowest")

    return targetColorScheme.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = surfaceBright,
        surfaceDim = surfaceDim,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainerLowest = surfaceContainerLowest
    )
}

@Composable
private fun animateExtendedColors(
    targetExtended: ExtendedColors,
    animationSpec: AnimationSpec<Color> = ThemeAnimationSpec
): ExtendedColors {
    val ok by animateColorAsState(targetExtended.ok, animationSpec, label = "extended_ok")
    val okBg by animateColorAsState(targetExtended.okBg, animationSpec, label = "extended_okBg")
    val bad by animateColorAsState(targetExtended.bad, animationSpec, label = "extended_bad")
    val badBg by animateColorAsState(targetExtended.badBg, animationSpec, label = "extended_badBg")
    return ExtendedColors(ok = ok, okBg = okBg, bad = bad, badBg = badBg)
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep intentional brand colors true to the HTML design
    content: @Composable () -> Unit,
) {
    val targetColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val targetExtended = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val animatedColorScheme = animateColorScheme(targetColorScheme)
    val animatedExtended = animateExtendedColors(targetExtended)

    CompositionLocalProvider(LocalExtendedColors provides animatedExtended) {
        MaterialTheme(
            colorScheme = animatedColorScheme,
            typography = Typography,
            content = content
        )
    }
}
