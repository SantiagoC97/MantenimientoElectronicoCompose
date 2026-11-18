
package com.example.mantenimientoelectronicoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    onAvanzarEstado: (String) -> Unit,
    onEliminar: (String) -> Unit,
    onEditar: (String, String, String, String) -> Unit
) {
    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var codigoAEliminar by remember {
        mutableStateOf<String?>(null)
    }

    var solicitudAEditar by remember {
        mutableStateOf<SolicitudMantenimiento?>(null)
    }

    val filtro = busqueda.trim()

    val filtradas = solicitudes.filter { solicitud ->
        filtro.isEmpty() ||
                solicitud.codigo.contains(filtro, ignoreCase = true) ||
                solicitud.equipo.contains(filtro, ignoreCase = true) ||
                solicitud.tipo.contains(filtro, ignoreCase = true) ||
                solicitud.problema.contains(filtro, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    "Solicitudes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    "Consulta y actualiza el mantenimiento de los equipos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(14.dp))

                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    label = { Text("Buscar solicitud o equipo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(7.dp))

                Text(
                    "${filtradas.size} resultado(s)",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario
                )
            }
        }

        if (filtradas.isEmpty()) {
            item {
                Text(
                    "No se encontraron solicitudes con esa búsqueda.",
                    color = TextoSecundario
                )
            }
        } else {
            items(
                items = filtradas,
                key = { it.codigo }
            ) { solicitud ->

                Column {
                    SolicitudCard(
                        solicitud = solicitud,
                        onAvanzarEstado = {
                            onAvanzarEstado(solicitud.codigo)
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                solicitudAEditar = solicitud
                            }
                        ) {
                            Text("Editar")
                        }

                        TextButton(
                            onClick = {
                                codigoAEliminar = solicitud.codigo
                            }
                        ) {
                            Text(
                                "Eliminar",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }

    // Confirmacion para eliminar
    val codigoSeleccionado = codigoAEliminar

    if (codigoSeleccionado != null) {
        AlertDialog(
            onDismissRequest = {
                codigoAEliminar = null
            },
            title = {
                Text("Eliminar mantenimiento")
            },
            text = {
                Text(
                    "¿Deseas eliminar el mantenimiento " +
                            "$codigoSeleccionado? " +
                            "Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminar(codigoSeleccionado)
                        codigoAEliminar = null
                    }
                ) {
                    Text(
                        "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        codigoAEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Ventana para editar un mantenimiento
    val solicitudSeleccionada = solicitudAEditar

    if (solicitudSeleccionada != null) {

        var descripcionEditada by remember(
            solicitudSeleccionada.codigo
        ) {
            mutableStateOf(solicitudSeleccionada.problema)
        }

        var tipoEditado by remember(
            solicitudSeleccionada.codigo
        ) {
            mutableStateOf(
                solicitudSeleccionada.tipoMantenimiento
            )
        }

        var prioridadEditada by remember(
            solicitudSeleccionada.codigo
        ) {
            mutableStateOf(solicitudSeleccionada.prioridad)
        }

        var mostrarError by remember(
            solicitudSeleccionada.codigo
        ) {
            mutableStateOf(false)
        }

        AlertDialog(
            onDismissRequest = {
                solicitudAEditar = null
            },

            title = {
                Text("Editar mantenimiento")
            },

            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        "Solicitud ${solicitudSeleccionada.codigo}",
                        style = MaterialTheme.typography.labelMedium
                    )

                    OutlinedTextField(
                        value = descripcionEditada,
                        onValueChange = {
                            descripcionEditada = it
                        },
                        label = {
                            Text("Descripción *")
                        },
                        isError = mostrarError &&
                                descripcionEditada.isBlank(),
                        supportingText = {
                            if (
                                mostrarError &&
                                descripcionEditada.isBlank()
                            ) {
                                Text("La descripción es obligatoria")
                            }
                        },
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        "Tipo de mantenimiento",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "Correctivo",
                            "Preventivo"
                        ).forEach { opcion ->

                            FilterChip(
                                selected = tipoEditado == opcion,
                                onClick = {
                                    tipoEditado = opcion
                                },
                                label = {
                                    Text(opcion)
                                }
                            )
                        }
                    }

                    Text(
                        "Prioridad",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "Baja",
                            "Media",
                            "Alta"
                        ).forEach { opcion ->

                            FilterChip(
                                selected = prioridadEditada == opcion,
                                onClick = {
                                    prioridadEditada = opcion
                                },
                                label = {
                                    Text(opcion)
                                }
                            )
                        }
                    }
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        val descripcion =
                            descripcionEditada.trim()

                        if (descripcion.isBlank()) {
                            mostrarError = true
                        } else {
                            onEditar(
                                solicitudSeleccionada.codigo,
                                descripcion,
                                tipoEditado,
                                prioridadEditada
                            )

                            solicitudAEditar = null
                        }
                    }
                ) {
                    Text("Guardar cambios")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        solicitudAEditar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
