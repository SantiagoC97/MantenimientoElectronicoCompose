package com.example.mantenimientoelectronicoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mantenimientoelectronicoapp.data.local.EquipoEntity
import com.example.mantenimientoelectronicoapp.ui.theme.AzulOscuro
import com.example.mantenimientoelectronicoapp.ui.theme.FondoPantalla
import com.example.mantenimientoelectronicoapp.ui.theme.MantenimientoElectronicoTheme

@Composable
fun PantallaEquipos(
    equipos: List<EquipoEntity>,
    onRegistrarEquipo: (String, String, String) -> Unit,
    onGenerarDatosPrueba: () -> Unit = {},
    onEliminarEquipo: (Long) -> Unit = {}
) {

    var nombre by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf("") }
    var ubicacion by rememberSaveable { mutableStateOf("") }
    var mostrarErrores by rememberSaveable { mutableStateOf(false) }

    var equipoAEliminar by remember {
        mutableStateOf<EquipoEntity?>(null)
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Equipos electrónicos",
                style = MaterialTheme.typography.headlineSmall,
                color = AzulOscuro
            )

            Text(
                "Registra y consulta los equipos del sistema.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del equipo *") },
                isError = mostrarErrores && nombre.isBlank(),
                supportingText = {
                    if (mostrarErrores && nombre.isBlank()) {
                        Text("Ingresa el nombre del equipo")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo de equipo *") },
                isError = mostrarErrores && tipo.isBlank(),
                supportingText = {
                    if (mostrarErrores && tipo.isBlank()) {
                        Text("Ingresa el tipo de equipo")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                onClick = {
                    mostrarErrores = true

                    if (nombre.isNotBlank() && tipo.isNotBlank()) {
                        onRegistrarEquipo(
                            nombre.trim(),
                            tipo.trim(),
                            ubicacion.trim().ifBlank {
                                "Sin especificar"
                            }
                        )

                        nombre = ""
                        tipo = ""
                        ubicacion = ""
                        mostrarErrores = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar equipo")
            }
        }


        item {
            OutlinedButton(
                onClick = onGenerarDatosPrueba,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generar datos de prueba")
            }
        }



        item {
            Text(
                "Equipos registrados (${equipos.size})",
                style = MaterialTheme.typography.titleLarge,
                color = AzulOscuro
            )
        }

        if (equipos.isEmpty()) {
            item {
                Text("Todavía no hay equipos registrados.")
            }
        }

        items(equipos, key = { it.id }) { equipo ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        equipo.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text("Tipo: ${equipo.tipo}")
                    Text("Ubicación: ${equipo.ubicacion}")
                    Text("ID: ${equipo.id}")

                    TextButton(
                        onClick = {
                            equipoAEliminar = equipo
                        }
                    ) {
                        Text(
                            text = "Eliminar equipo",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                }
            }
        }
    }

    val equipoSeleccionado = equipoAEliminar

    if (equipoSeleccionado != null) {

        AlertDialog(
            onDismissRequest = {
                equipoAEliminar = null
            },

            title = {
                Text("Eliminar equipo")
            },

            text = {
                Text(
                    "¿Deseas eliminar ${equipoSeleccionado.nombre}? " +
                            "También se eliminarán todos los mantenimientos " +
                            "asociados a este equipo. " +
                            "Esta acción no se puede deshacer."
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminarEquipo(equipoSeleccionado.id)
                        equipoAEliminar = null
                    }
                ) {
                    Text(
                        "Eliminar todo",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        equipoAEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun VistaPreviaEquipos() {
    MantenimientoElectronicoTheme {
        PantallaEquipos(
            equipos = listOf(
                EquipoEntity(
                    id = 1,
                    nombre = "Dell Inspiron 15",
                    tipo = "Computador portátil",
                    ubicacion = "Laboratorio"
                )
            ),
            onRegistrarEquipo = { _, _, _ -> }
        )
    }
}
