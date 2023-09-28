package no.hiof.friluftslivcompanionapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import no.hiof.friluftslivcompanionapp.R


val CustomTypography =  Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_title)),
        fontSize = 42.sp,
        lineHeight = 46.sp,
        letterSpacing = 0.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_header)),
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_thin)),
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )
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
