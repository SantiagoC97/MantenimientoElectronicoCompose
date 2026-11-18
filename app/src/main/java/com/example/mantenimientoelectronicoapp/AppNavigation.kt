
package com.example.mantenimientoelectronicoapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mantenimientoelectronicoapp.data.local.EquipoEntity
import com.example.mantenimientoelectronicoapp.data.local.MantenimientoEntity
import com.example.mantenimientoelectronicoapp.screens.PantallaEquipos
import com.example.mantenimientoelectronicoapp.screens.PantallaInicio
import com.example.mantenimientoelectronicoapp.screens.PantallaRegistrarMantenimiento
import com.example.mantenimientoelectronicoapp.screens.PantallaSolicitudes
import com.example.mantenimientoelectronicoapp.viewmodel.MantenimientoViewModel
import kotlinx.coroutines.launch

private data class Destino(
    val ruta: String,
    val nombre: String,
    val simbolo: String
)

private val destinos = listOf(
    Destino("inicio", "Inicio", "⌂"),
    Destino("solicitudes", "Solicitudes", "☷"),
    Destino("equipos", "Equipos", "▣"),
    Destino("registrar", "Registrar", "+")
)

@Composable
fun AppNavigation() {

    // ViewModel conectado al Repository y a Room
    val mantenimientoViewModel: MantenimientoViewModel = viewModel(
        factory = MantenimientoViewModel.Factory
    )

    // Datos observados directamente desde Room
    val equipos by mantenimientoViewModel.equipos.collectAsStateWithLifecycle()

    val mantenimientos by mantenimientoViewModel.mantenimientos
        .collectAsStateWithLifecycle()

    // Adaptamos los registros de Room al modelo de las tarjetas
    // que ya utilizabamos en el primer corte.
    val solicitudes = mantenimientos.map { mantenimiento ->

        val equipo = equipos.firstOrNull {
            it.id == mantenimiento.equipoId
        }


        SolicitudMantenimiento(
            codigo = "MT-%03d".format(mantenimiento.id),
            equipo = equipo?.nombre ?: "Equipo no encontrado",
            tipo = equipo?.tipo ?: "Sin especificar",
            problema = mantenimiento.problema,
            estado = mantenimiento.estado,
            prioridad = mantenimiento.prioridad,
            tipoMantenimiento = mantenimiento.tipoMantenimiento
        )

    }

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    fun navegar(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                destinos.forEach { destino ->

                    NavigationBarItem(
                        selected = rutaActual == destino.ruta,
                        onClick = {
                            navegar(destino.ruta)
                        },
                        icon = {
                            Text(destino.simbolo)
                        },
                        label = {
                            Text(destino.nombre)
                        },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { paddingInterno ->

        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(paddingInterno)
        ) {

            // INICIO: indicadores calculados con datos de Room
            composable("inicio") {

                PantallaInicio(
                    solicitudes = solicitudes,
                    onVerSolicitudes = {
                        navegar("solicitudes")
                    },
                    onRegistrar = {
                        navegar("registrar")
                    }
                )
            }

            // SOLICITUDES: consulta y actualizacion en Room

            composable("solicitudes") {

                PantallaSolicitudes(
                    solicitudes = solicitudes,

                    onAvanzarEstado = { codigo ->

                        val id = codigo
                            .removePrefix("MT-")
                            .toLongOrNull()

                        val mantenimiento = mantenimientos.firstOrNull {
                            it.id == id
                        }


                        if (mantenimiento != null) {
                            mantenimientoViewModel.avanzarEstado(
                                mantenimiento
                            )
                        }
                    },


                    onEliminar = { codigo ->

                        val id = codigo
                            .removePrefix("MT-")
                            .toLongOrNull()

                        val mantenimiento = mantenimientos.firstOrNull {
                            it.id == id
                        }

                        if (mantenimiento != null) {
                            mantenimientoViewModel.eliminarMantenimiento(
                                mantenimiento
                            )
                        }
                    },


                    onEditar = { codigo, problema, tipoMantenimiento, prioridad ->

                        val id = codigo
                            .removePrefix("MT-")
                            .toLongOrNull()

                        val mantenimiento = mantenimientos.firstOrNull {
                            it.id == id
                        }

                        if (mantenimiento != null) {
                            mantenimientoViewModel.editarMantenimiento(
                                mantenimiento.copy(
                                    problema = problema,
                                    tipoMantenimiento = tipoMantenimiento,
                                    prioridad = prioridad
                                )
                            )
                        }
                    }
                )
            }



            // EQUIPOS: registro persistente

            composable("equipos") {

                PantallaEquipos(
                    equipos = equipos,

                    onRegistrarEquipo = { nombre, tipo, ubicacion ->

                        mantenimientoViewModel.registrarEquipo(
                            EquipoEntity(
                                nombre = nombre,
                                tipo = tipo,
                                ubicacion = ubicacion
                            )
                        )
                    },

                    onGenerarDatosPrueba = {
                        mantenimientoViewModel.generarDatosPrueba()
                    },
                    onEliminarEquipo = { equipoId ->
                        mantenimientoViewModel.eliminarEquipoConHistorial(equipoId)
                    }
                )
            }


            // REGISTRAR: mantenimiento asociado a un equipo
            composable("registrar") {

                PantallaRegistrarMantenimiento(
                    equipos = equipos,
                    onGuardar = {
                            equipoId,
                            problema,
                            tipoMantenimiento,
                            prioridad ->

                        mantenimientoViewModel.registrarMantenimiento(
                            MantenimientoEntity(
                                equipoId = equipoId,
                                problema = problema,
                                tipoMantenimiento = tipoMantenimiento,
                                prioridad = prioridad,
                                estado = "Pendiente"
                            )
                        )

                        navegar("solicitudes")

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Mantenimiento enviado para guardar"
                            )
                        }
                    }
                )
            }
        }
    }
}
