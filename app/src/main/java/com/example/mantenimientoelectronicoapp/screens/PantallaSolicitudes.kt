package com.example.mantenimientoelectronicoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.mantenimientoelectronicoapp.SolicitudMantenimiento
import com.example.mantenimientoelectronicoapp.components.SolicitudCard
import com.example.mantenimientoelectronicoapp.ui.theme.AzulOscuro
import com.example.mantenimientoelectronicoapp.ui.theme.FondoPantalla
import com.example.mantenimientoelectronicoapp.ui.theme.TextoSecundario

@Composable
fun PantallaSolicitudes(
    solicitudes: List<SolicitudMantenimiento>,
    onAvanzarEstado: (String) -> Unit
) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    val filtro = busqueda.trim()
    val filtradas = solicitudes.filter { solicitud ->
        filtro.isEmpty() ||
            solicitud.codigo.contains(filtro, ignoreCase = true) ||
            solicitud.equipo.contains(filtro, ignoreCase = true) ||
            solicitud.tipo.contains(filtro, ignoreCase = true) ||
            solicitud.problema.contains(filtro, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(FondoPantalla),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Solicitudes", style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold, color = AzulOscuro)
                Spacer(Modifier.height(5.dp))
                Text("Consulta y actualiza el mantenimiento de los equipos.",
                    style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    label = { Text("Buscar solicitud o equipo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(7.dp))
                Text("${filtradas.size} resultado(s)",
                    style = MaterialTheme.typography.labelMedium, color = TextoSecundario)
            }
        }
        if (filtradas.isEmpty()) {
            item {
                Text("No se encontraron solicitudes con esa búsqueda.",
                    color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            items(filtradas, key = { it.codigo }) { solicitud ->
                SolicitudCard(
                    solicitud = solicitud,
                    onAvanzarEstado = { onAvanzarEstado(solicitud.codigo) }
                )
            }
        }
    }
}
