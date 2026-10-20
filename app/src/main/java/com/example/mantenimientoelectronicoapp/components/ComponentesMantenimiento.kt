package com.example.mantenimientoelectronicoapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mantenimientoelectronicoapp.SolicitudMantenimiento
import com.example.mantenimientoelectronicoapp.textoAccion
import com.example.mantenimientoelectronicoapp.ui.theme.AzulOscuro
import com.example.mantenimientoelectronicoapp.ui.theme.TextoSecundario

@Composable
fun EstadoBadge(estado: String) {
    val fondo: Color
    val texto: Color
    when (estado) {
        "Pendiente" -> { fondo = Color(0xFFFFF0D7); texto = Color(0xFF986000) }
        "En diagnóstico" -> { fondo = Color(0xFFE4F0FF); texto = Color(0xFF1E5BA8) }
        "En reparación" -> { fondo = Color(0xFFE1F7EE); texto = Color(0xFF137858) }
        "Finalizado" -> { fondo = Color(0xFFE9F7E8); texto = Color(0xFF2A7327) }
        else -> { fondo = Color(0xFFEEF0F3); texto = Color.DarkGray }
    }
    Box(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(50.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = estado, color = texto, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun DatoResumen(numero: Int, etiqueta: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            numero.toString(),
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(etiqueta, color = Color(0xFFD7E6F5), style = MaterialTheme.typography.labelMedium)
    }
}

/** Tarjeta compartida entre Inicio y Solicitudes. */
@Composable
fun SolicitudCard(
    solicitud: SolicitudMantenimiento,
    modifier: Modifier = Modifier,
    onAvanzarEstado: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${solicitud.codigo} · ${solicitud.tipo}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                EstadoBadge(solicitud.estado)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = solicitud.equipo,
                style = MaterialTheme.typography.titleMedium,
                color = AzulOscuro,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(5.dp))
            Text(solicitud.problema, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            Spacer(Modifier.height(12.dp))
            Text(
                "Prioridad: ${solicitud.prioridad}",
                style = MaterialTheme.typography.labelMedium,
                color = AzulOscuro
            )
            if (onAvanzarEstado != null && solicitud.estado != "Finalizado") {
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onAvanzarEstado,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(textoAccion(solicitud.estado))
                }
            }
        }
    }
}
