package com.rsicarelli.zeroglu.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


internal val LightThemeColors by lazy {
    lightColorScheme(
        primary = LightColors.Primary,
        onPrimary = LightColors.OnPrimary,
        primaryContainer = LightColors.PrimaryContainer,
        onPrimaryContainer = LightColors.OnPrimaryContainer,
        secondary = LightColors.Secondary,
        onSecondary = LightColors.OnSecondary,
        secondaryContainer = LightColors.SecondaryContainer,
        onSecondaryContainer = LightColors.OnSecondaryContainer,
        tertiary = LightColors.Tertiary,
        onTertiary = LightColors.OnTertiary,
        tertiaryContainer = LightColors.TertiaryContainer,
        onTertiaryContainer = LightColors.OnTertiaryContainer,
        error = LightColors.Error,
        errorContainer = LightColors.ErrorContainer,
        onError = LightColors.OnError,
        onErrorContainer = LightColors.OnErrorContainer,
        background = LightColors.Background,
        onBackground = LightColors.OnBackground,
        surface = LightColors.Surface,
        onSurface = LightColors.OnSurface,
        surfaceVariant = LightColors.SurfaceVariant,
        onSurfaceVariant = LightColors.OnSurfaceVariant,
        outline = LightColors.Outline,
        inverseOnSurface = LightColors.InverseOnSurface,
        inverseSurface = LightColors.InverseSurface,
        inversePrimary = LightColors.InversePrimary,
    )
}
internal val DarkThemeColors by lazy {
    darkColorScheme(
        primary = DarkColors.Primary,
        onPrimary = DarkColors.OnPrimary,
        primaryContainer = DarkColors.PrimaryContainer,
        onPrimaryContainer = DarkColors.OnPrimaryContainer,
        secondary = DarkColors.Secondary,
        onSecondary = DarkColors.OnSecondary,
        secondaryContainer = DarkColors.SecondaryContainer,
        onSecondaryContainer = DarkColors.OnSecondaryContainer,
        tertiary = DarkColors.Tertiary,
        onTertiary = DarkColors.OnTertiary,
        tertiaryContainer = DarkColors.TertiaryContainer,
        onTertiaryContainer = DarkColors.OnTertiaryContainer,
        error = DarkColors.Error,
        errorContainer = DarkColors.ErrorContainer,
        onError = DarkColors.OnError,
        onErrorContainer = DarkColors.OnErrorContainer,
        background = DarkColors.Background,
        onBackground = DarkColors.OnBackground,
        surface = DarkColors.Surface,
        onSurface = DarkColors.OnSurface,
        surfaceVariant = DarkColors.SurfaceVariant,
        onSurfaceVariant = DarkColors.OnSurfaceVariant,
        outline = DarkColors.Outline,
        inverseOnSurface = DarkColors.InverseOnSurface,
        inverseSurface = DarkColors.InverseSurface,
        inversePrimary = DarkColors.InversePrimary,
    )
}

private object LightColors {

    val Primary = Color(0xFF006d44)
    val OnPrimary = Color(0xFFffffff)
    val PrimaryContainer = Color(0xFF90f7bf)
    val OnPrimaryContainer = Color(0xFF002111)
    val Secondary = Color(0xFF4e6356)
    val OnSecondary = Color(0xFFffffff)
    val SecondaryContainer = Color(0xFFd0e9d7)
    val OnSecondaryContainer = Color(0xFF0b1f15)
    val Tertiary = Color(0xFF3c6472)
    val OnTertiary = Color(0xFFffffff)
    val TertiaryContainer = Color(0xFFc0e9f9)
    val OnTertiaryContainer = Color(0xFF001f28)
    val Error = Color(0xFFba1b1b)
    val ErrorContainer = Color(0xFFffdad4)
    val OnError = Color(0xFFffffff)
    val OnErrorContainer = Color(0xFF410001)
    val Background = Color(0xFFfbfdf8)
    val OnBackground = Color(0xFF191c1a)
    val Surface = Color(0xFFfbfdf8)
    val OnSurface = Color(0xFF191c1a)
    val SurfaceVariant = Color(0xFFdce5dc)
    val OnSurfaceVariant = Color(0xFF404943)
    val Outline = Color(0xFF707972)
    val InverseOnSurface = Color(0xFFf0f1ed)
    val InverseSurface = Color(0xFF2d312e)
    val InversePrimary = Color(0xFF73daa4)
}

private object DarkColors {

    val Primary = Color(0xFF73daa4)
    val OnPrimary = Color(0xFF003921)
    val PrimaryContainer = Color(0xFF005232)
    val OnPrimaryContainer = Color(0xFF90f7bf)
    val Secondary = Color(0xFFb4ccbb)
    val OnSecondary = Color(0xFF203529)
    val SecondaryContainer = Color(0xFF364b3e)
    val OnSecondaryContainer = Color(0xFFd0e9d7)
    val Tertiary = Color(0xFFa4cddd)
    val OnTertiary = Color(0xFF053542)
    val TertiaryContainer = Color(0xFF234c59)
    val OnTertiaryContainer = Color(0xFFc0e9f9)
    val Error = Color(0xFFffb4a9)
    val ErrorContainer = Color(0xFF930006)
    val OnError = Color(0xFF680003)
    val OnErrorContainer = Color(0xFFffdad4)
    val Background = Color(0xFF191c1a)
    val OnBackground = Color(0xFFe2e3df)
    val Surface = Color(0xFF191c1a)
    val OnSurface = Color(0xFFe2e3df)
    val SurfaceVariant = Color(0xFF404943)
    val OnSurfaceVariant = Color(0xFFc0c9c1)
    val Outline = Color(0xFF8a938b)
    val InverseOnSurface = Color(0xFF191c1a)
    val InverseSurface = Color(0xFFe2e3df)
    val InversePrimary = Color(0xFF006d44)

}

