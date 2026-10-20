package com.example.mantenimientoelectronicoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mantenimientoelectronicoapp.ui.theme.MantenimientoElectronicoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MantenimientoElectronicoTheme {
                AppNavigation()
            }
        }
    }
}
