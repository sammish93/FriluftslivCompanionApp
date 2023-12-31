package no.hiof.friluftslivcompanionapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import no.hiof.friluftslivcompanionapp.R


val CustomTypography =  Typography(

    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_black)),
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.sp
    ),

    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_black)),
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),

    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_black)),
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_regular)),
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_regular)),
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_regular)),
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_light)),
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_light)),
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_light)),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.enriqueta_medium)),
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.enriqueta_medium)),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.enriqueta_medium)),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),

    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_light)),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_light)),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),

    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.londrinasolid_light)),
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
)



/*
Alternative 2:
object CustomTypography {
    val Title = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_title)),
        fontSize = 42.sp,
        lineHeight = 46.sp,
        letterSpacing = 0.sp
    )

    val Header = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_header)),
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )

    val Paragraph_normal = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_thin)),
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )
    val Paragraph_italic = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_thin_italic)),
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )
}*/
