package com.example.mantenimientoelectronicoapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresMantenimiento = lightColorScheme(
    primary = AzulAccion,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3ECFF),
    onPrimaryContainer = AzulOscuro,
    secondary = VerdeTecnico,
    onSecondary = Color.White,
    background = FondoPantalla,
    onBackground = AzulOscuro,
    surface = Color.White,
    onSurface = AzulOscuro
)

@Composable
fun MantenimientoElectronicoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresMantenimiento,
        typography = MantenimientoTypography,
        content = content
    )
}
