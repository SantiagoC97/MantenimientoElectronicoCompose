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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mantenimientoelectronicoapp.screens.PantallaInicio
import com.example.mantenimientoelectronicoapp.screens.PantallaRegistrar
import com.example.mantenimientoelectronicoapp.screens.PantallaSolicitudes
import kotlinx.coroutines.launch

private data class Destino(val ruta: String, val nombre: String, val simbolo: String)
private val destinos = listOf(
    Destino("inicio", "Inicio", "⌂"),
    Destino("solicitudes", "Solicitudes", "☷"),
    Destino("registrar", "Registrar", "+")
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Esta lista es la única fuente de datos para las tres pantallas.
    // Las sustituciones por copy() activan la recomposición de Compose.
    val solicitudes = remember {
        mutableStateListOf<SolicitudMantenimiento>().apply {
            addAll(solicitudesIniciales())
        }
    }
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    fun navegar(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                destinos.forEach { destino ->
                    NavigationBarItem(
                        selected = rutaActual == destino.ruta,
                        onClick = { navegar(destino.ruta) },
                        icon = { Text(destino.simbolo) },
                        label = { Text(destino.nombre) },
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
            composable("inicio") {
                PantallaInicio(
                    solicitudes = solicitudes,
                    onVerSolicitudes = { navegar("solicitudes") },
                    onRegistrar = { navegar("registrar") }
                )
            }
            composable("solicitudes") {
                PantallaSolicitudes(
                    solicitudes = solicitudes,
                    onAvanzarEstado = { codigo ->
                        val indice = solicitudes.indexOfFirst { it.codigo == codigo }
                        if (indice >= 0) {
                            val solicitud = solicitudes[indice]
                            val proximo = siguienteEstado(solicitud.estado)
                            if (proximo != null) {
                                solicitudes[indice] = solicitud.copy(estado = proximo)
                                scope.launch { snackbarHostState.showSnackbar("$codigo: $proximo") }
                            }
                        }
                    }
                )
            }
            composable("registrar") {
                PantallaRegistrar(
                    onGuardar = { equipo, tipo, problema, prioridad ->
                        val ultimoNumero = solicitudes.mapNotNull {
                            it.codigo.substringAfter("MT-", "").toIntOrNull()
                        }.maxOrNull() ?: 0
                        val codigo = "MT-%03d".format(ultimoNumero + 1)
                        solicitudes.add(
                            SolicitudMantenimiento(
                                codigo = codigo,
                                equipo = equipo,
                                tipo = tipo,
                                problema = problema,
                                estado = "Pendiente",
                                prioridad = prioridad
                            )
                        )
                        navegar("solicitudes")
                        scope.launch {
                            snackbarHostState.showSnackbar("Solicitud $codigo registrada correctamente")
                        }
                    }
                )
            }
        }
    }
}
