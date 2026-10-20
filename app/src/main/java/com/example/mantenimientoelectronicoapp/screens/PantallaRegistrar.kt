package com.example.mantenimientoelectronicoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mantenimientoelectronicoapp.ui.theme.AzulOscuro
import com.example.mantenimientoelectronicoapp.ui.theme.FondoPantalla
import com.example.mantenimientoelectronicoapp.ui.theme.TextoSecundario

@Composable
fun PantallaRegistrar(onGuardar: (equipo: String, tipo: String, problema: String, prioridad: String) -> Unit) {
    var equipo by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf("") }
    var problema by rememberSaveable { mutableStateOf("") }
    var prioridad by rememberSaveable { mutableStateOf("Media") }
    var mostrarErrores by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Registrar mantenimiento", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold, color = AzulOscuro)
        Spacer(Modifier.height(5.dp))
        Text("Ingresa los datos del equipo y la falla reportada.",
            style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = equipo,
            onValueChange = { equipo = it },
            label = { Text("Nombre del equipo *") },
            placeholder = { Text("Ej.: Lenovo ThinkPad") },
            isError = mostrarErrores && equipo.isBlank(),
            supportingText = {
                if (mostrarErrores && equipo.isBlank()) Text("Escribe el nombre del equipo")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = tipo,
            onValueChange = { tipo = it },
            label = { Text("Tipo de equipo *") },
            placeholder = { Text("Ej.: Computador portátil") },
            isError = mostrarErrores && tipo.isBlank(),
            supportingText = {
                if (mostrarErrores && tipo.isBlank()) Text("Indica el tipo de equipo")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = problema,
            onValueChange = { problema = it },
            label = { Text("Descripción de la falla *") },
            placeholder = { Text("Describe brevemente el problema") },
            isError = mostrarErrores && problema.isBlank(),
            supportingText = {
                if (mostrarErrores && problema.isBlank()) Text("Describe la falla del equipo")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )
        Spacer(Modifier.height(14.dp))
        Text("Prioridad", style = MaterialTheme.typography.titleMedium, color = AzulOscuro)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Baja", "Media", "Alta").forEach { opcion ->
                FilterChip(
                    selected = prioridad == opcion,
                    onClick = { prioridad = opcion },
                    label = { Text(opcion) }
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                mostrarErrores = true
                if (equipo.isNotBlank() && tipo.isNotBlank() && problema.isNotBlank()) {
                    onGuardar(equipo.trim(), tipo.trim(), problema.trim(), prioridad)
                    equipo = ""
                    tipo = ""
                    problema = ""
                    prioridad = "Media"
                    mostrarErrores = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar solicitud")
        }
        Spacer(Modifier.height(12.dp))
        Text("Los datos se conservan durante esta sesión de la aplicación.",
            style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
    }
}
