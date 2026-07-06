package com.axel_stein.pizzatestappcompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.axel_stein.pizzatestappcompose.R

val FigtreeFontFamily = FontFamily(
    // Light (300)
    Font(resId = R.font.figtree_light, weight = FontWeight.W300, style = FontStyle.Normal),
    Font(resId = R.font.figtree_light_italic, weight = FontWeight.W300, style = FontStyle.Italic),

    // Regular (400)
    Font(resId = R.font.figtree_regular, weight = FontWeight.W400, style = FontStyle.Normal),
    Font(resId = R.font.figtree_italic, weight = FontWeight.W400, style = FontStyle.Italic),

    // Medium (500)
    Font(resId = R.font.figtree_medium, weight = FontWeight.W500, style = FontStyle.Normal),
    Font(resId = R.font.figtree_medium_italic, weight = FontWeight.W500, style = FontStyle.Italic),

    // SemiBold (600)
    Font(resId = R.font.figtree_semi_bold, weight = FontWeight.W600, style = FontStyle.Normal),
    Font(resId = R.font.figtree_semi_bold_italic, weight = FontWeight.W600, style = FontStyle.Italic),

    // Bold (700)
    Font(resId = R.font.figtree_bold, weight = FontWeight.W700, style = FontStyle.Normal),
    Font(resId = R.font.figtree_bold_italic, weight = FontWeight.W700, style = FontStyle.Italic),

    // ExtraBold (800)
    Font(resId = R.font.figtree_extra_bold, weight = FontWeight.W800, style = FontStyle.Normal),
    Font(resId = R.font.figtree_extra_bold_italic, weight = FontWeight.W800, style = FontStyle.Italic),

    // Black (900)
    Font(resId = R.font.figtree_black, weight = FontWeight.W900, style = FontStyle.Normal),
    Font(resId = R.font.figtree_black_italic, weight = FontWeight.W900, style = FontStyle.Italic)
)

fun createTypography(fontFamily: FontFamily): Typography {
    val defaultTypography = Typography()
    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),

        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),

        titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),

        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily),

        labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily)
    )
}

val Typography = createTypography(FigtreeFontFamily)