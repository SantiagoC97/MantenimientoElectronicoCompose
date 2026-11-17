package com.example.mantenimientoelectronicoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mantenimientoelectronicoapp.data.local.EquipoEntity
import com.example.mantenimientoelectronicoapp.ui.theme.FondoPantalla

@Composable
fun PantallaRegistrarMantenimiento(
    equipos: List<EquipoEntity>,
    onGuardar: (Long, String, String, String) -> Unit
) {
    var equipoId by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    var problema by rememberSaveable {
        mutableStateOf("")
    }

    var tipoMantenimiento by rememberSaveable {
        mutableStateOf("Correctivo")
    }

    var prioridad by rememberSaveable {
        mutableStateOf("Media")
    }

    var mostrarErrores by rememberSaveable {
        mutableStateOf(false)
    }

    var menuAbierto by remember {
        mutableStateOf(false)
    }

    val equipoSeleccionado = equipos.firstOrNull {
        it.id == equipoId
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "Registrar mantenimiento",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Selecciona un equipo y registra " +
                    "la intervención que necesita."
        )

        // Seleccionar un equipo almacenado en Room
        Text(
            text = "Equipo",
            style = MaterialTheme.typography.titleMedium
        )

        Box {
            OutlinedButton(
                onClick = { menuAbierto = true },
                enabled = equipos.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    equipoSeleccionado?.nombre
                        ?: "Seleccionar equipo *"
                )
            }

            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = {
                    menuAbierto = false
                }
            ) {
                equipos.forEach { equipo ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                "${equipo.nombre} - ${equipo.tipo}"
                            )
                        },
                        onClick = {
                            equipoId = equipo.id
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        if (equipos.isEmpty()) {
            Text(
                text = "Primero debes registrar " +
                        "un equipo en la sección Equipos.",
                color = MaterialTheme.colorScheme.error
            )
        }

        if (mostrarErrores && equipoSeleccionado == null) {
            Text(
                text = "Selecciona un equipo registrado.",
                color = MaterialTheme.colorScheme.error
            )
        }

        // Tipo de mantenimiento
        Text(
            text = "Tipo de mantenimiento",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Correctivo", "Preventivo").forEach { opcion ->
                FilterChip(
                    selected = tipoMantenimiento == opcion,
                    onClick = {
                        tipoMantenimiento = opcion
                    },
                    label = { Text(opcion) }
                )
            }
        }

        // Descripción de la intervención
        OutlinedTextField(
            value = problema,
            onValueChange = {
                problema = it
            },
            label = {
                Text("Descripción del mantenimiento *")
            },
            placeholder = {
                Text("Describe la falla o intervención")
            },
            minLines = 3,
            maxLines = 5,
            isError = mostrarErrores && problema.isBlank(),
            supportingText = {
                if (mostrarErrores && problema.isBlank()) {
                    Text("Ingresa una descripción")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Prioridad
        Text(
            text = "Prioridad",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Baja", "Media", "Alta").forEach { opcion ->
                FilterChip(
                    selected = prioridad == opcion,
                    onClick = {
                        prioridad = opcion
                    },
                    label = { Text(opcion) }
                )
            }
        }

        // Guardar mantenimiento
        Button(
            onClick = {
                mostrarErrores = true

                val idSeleccionado = equipoId

                if (
                    idSeleccionado != null &&
                    equipoSeleccionado != null &&
                    problema.isNotBlank()
                ) {
                    onGuardar(
                        idSeleccionado,
                        problema.trim(),
                        tipoMantenimiento,
                        prioridad
                    )

                    equipoId = null
                    problema = ""
                    tipoMantenimiento = "Correctivo"
                    prioridad = "Media"
                    mostrarErrores = false
                }
            },
            enabled = equipos.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar mantenimiento")
        }

        Text(
            text = "Los mantenimientos se asociarán " +
                    "a equipos registrados en el sistema.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
