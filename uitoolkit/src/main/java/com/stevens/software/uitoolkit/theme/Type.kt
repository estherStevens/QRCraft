package com.stevens.software.uitoolkit.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.stevens.software.uitoolkit.R

val suseFontFamily = FontFamily(
    Font(R.font.suse_regular, FontWeight.Normal),
    Font(R.font.suse_medium, FontWeight.Medium),
    Font(R.font.suse_semibold, FontWeight.SemiBold)
)

val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = suseFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = suseFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 24.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = suseFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = suseFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )
)

