package com.jordan.labweek9.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun LabWeek9Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

//UI Element for displaying a title
@Composable
fun OnBackgroundTitleText(text: String) {
    TitleText(text = text, color =
        MaterialTheme.colorScheme.onBackground)
}
//Here, we use the titleLarge style from the typography
@Composable
fun TitleText(text: String, color: Color) {
    Text(text = text, style =
        MaterialTheme.typography.titleLarge, color = color)
}
//UI Element for displaying an item list
@Composable
fun OnBackgroundItemText(text: String) {
    ItemText(text = text, color =
        MaterialTheme.colorScheme.onBackground)
}
//Here, we use the bodySmall style from the typography
@Composable
fun ItemText(text: String, color: Color) {
    Text(text = text, style =
        MaterialTheme.typography.bodySmall, color = color)
}

//UI Element for displaying a button
@Composable
fun PrimaryTextButton(text: String, onClick: () -> Unit) {
    TextButton(text = text,
        textColor = Color.White,
        onClick = onClick
    )
}
//Here, we use the labelMedium style from the typography
@Composable
fun TextButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Color.DarkGray,
                contentColor = textColor
            )
    ) {
        Text(text = text, style =
            MaterialTheme.typography.labelMedium)
    }
}
