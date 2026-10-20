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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mantenimientoelectronicoapp.SolicitudMantenimiento
import com.example.mantenimientoelectronicoapp.components.DatoResumen
import com.example.mantenimientoelectronicoapp.components.SolicitudCard
import com.example.mantenimientoelectronicoapp.solicitudesIniciales
import com.example.mantenimientoelectronicoapp.ui.theme.AzulOscuro
import com.example.mantenimientoelectronicoapp.ui.theme.FondoPantalla
import com.example.mantenimientoelectronicoapp.ui.theme.MantenimientoElectronicoTheme
import com.example.mantenimientoelectronicoapp.ui.theme.TextoSecundario

@Composable
fun PantallaInicio(
    solicitudes: List<SolicitudMantenimiento>,
    onVerSolicitudes: () -> Unit,
    onRegistrar: () -> Unit
) {
    val pendientes = solicitudes.count { it.estado == "Pendiente" }
    val enProceso = solicitudes.count { it.estado == "En diagnóstico" || it.estado == "En reparación" }
    val finalizadas = solicitudes.count { it.estado == "Finalizado" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("GESTIÓN TÉCNICA", style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        Text("Panel de mantenimiento", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold, color = AzulOscuro)
        Spacer(Modifier.height(4.dp))
        Text("Sistema de Mantenimiento Electrónico",
            style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
        Spacer(Modifier.height(22.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = AzulOscuro)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Resumen de solicitudes", style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFFD7E6F5))
                Spacer(Modifier.height(14.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DatoResumen(solicitudes.size, "Total", Modifier.weight(1f))
                    DatoResumen(pendientes, "Pendientes", Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DatoResumen(enProceso, "En proceso", Modifier.weight(1f))
                    DatoResumen(finalizadas, "Finalizadas", Modifier.weight(1f))
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Button(onClick = onRegistrar, modifier = Modifier.fillMaxWidth()) { Text("+ Registrar mantenimiento") }
        OutlinedButton(onClick = onVerSolicitudes, modifier = Modifier.fillMaxWidth()) {
            Text("Ver todas las solicitudes")
        }
        Spacer(Modifier.height(19.dp))
        Text("Solicitudes recientes", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, color = AzulOscuro)
        Spacer(Modifier.height(5.dp))
        Text("Información actualizada durante esta sesión",
            style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
        Spacer(Modifier.height(12.dp))
        solicitudes.takeLast(3).reversed().forEach { solicitud ->
            SolicitudCard(solicitud)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VistaPreviaInicio() {
    MantenimientoElectronicoTheme {
        PantallaInicio(solicitudesIniciales(), {}, {})
    }
}
