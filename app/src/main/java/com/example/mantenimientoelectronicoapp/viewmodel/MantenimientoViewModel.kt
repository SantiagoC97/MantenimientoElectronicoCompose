package com.example.mantenimientoelectronicoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mantenimientoelectronicoapp.data.MantenimientoRepository
import com.example.mantenimientoelectronicoapp.data.local.EquipoEntity
import com.example.mantenimientoelectronicoapp.data.local.MantenimientoEntity
import com.example.mantenimientoelectronicoapp.siguienteEstado
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mantenimientoelectronicoapp.data.local.MantenimientoDatabase
import kotlinx.coroutines.flow.first


class MantenimientoViewModel(
    private val repositorio: MantenimientoRepository
) : ViewModel() {

    // Equipos observados desde la base de datos Room
    val equipos: StateFlow<List<EquipoEntity>> =
        repositorio.equipos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Mantenimientos observados desde Room
    val mantenimientos: StateFlow<List<MantenimientoEntity>> =
        repositorio.mantenimientos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Registrar un equipo
    fun registrarEquipo(equipo: EquipoEntity) {
        viewModelScope.launch {
            repositorio.registrarEquipo(equipo)
        }
    }

    // Registrar un mantenimiento
    fun registrarMantenimiento(
        mantenimiento: MantenimientoEntity
    ) {
        viewModelScope.launch {
            repositorio.registrarMantenimiento(mantenimiento)
        }
    }

    // Editar un mantenimiento existente
    fun editarMantenimiento(
        mantenimiento: MantenimientoEntity
    ) {
        viewModelScope.launch {
            repositorio.actualizarMantenimiento(mantenimiento)
        }
    }

    // Eliminar un mantenimiento
    fun eliminarMantenimiento(
        mantenimiento: MantenimientoEntity
    ) {
        viewModelScope.launch {
            repositorio.eliminarMantenimiento(mantenimiento)
        }
    }

    // Avanzar el estado de un mantenimiento

    fun avanzarEstado(
        mantenimiento: MantenimientoEntity
    ) {
        val nuevoEstado = siguienteEstado(mantenimiento.estado)
            ?: return

        viewModelScope.launch {
            repositorio.cambiarEstado(
                id = mantenimiento.id,
                estadoActual = mantenimiento.estado,
                nuevoEstado = nuevoEstado
            )
        }
    }

    private var generandoDatosPrueba = false

    fun generarDatosPrueba() {

        if (generandoDatosPrueba) return
        generandoDatosPrueba = true

        viewModelScope.launch {
            try {
                val nombresExistentes = repositorio.equipos
                    .first()
                    .map { it.nombre }
                    .toSet()

                val ejemplos = listOf(
                    Triple("[PRUEBA] Dell Latitude 5420",
                        "Computador portátil", "Laboratorio"),
                    Triple("[PRUEBA] Epson EcoTank L3150",
                        "Impresora", "Oficina"),
                    Triple("[PRUEBA] Samsung 24 pulgadas",
                        "Monitor", "Sala de sistemas"),
                    Triple("[PRUEBA] Lenovo ThinkCentre",
                        "Computador de escritorio", "Laboratorio"),
                    Triple("[PRUEBA] TP-Link Archer C6",
                        "Router", "Sala de redes"),
                    Triple("[PRUEBA] HP Pavilion",
                        "Computador portátil", "Oficina"),
                    Triple("[PRUEBA] Fuente DC",
                        "Fuente de alimentación", "Laboratorio"),
                    Triple("[PRUEBA] Rigol DS1054Z",
                        "Osciloscopio", "Laboratorio")
                )

                val problemas = listOf(
                    listOf(
                        "El teclado presenta fallas.",
                        "La batería no recibe carga."
                    ),
                    listOf(
                        "Presenta atascos de papel.",
                        "La impresión presenta líneas."
                    ),
                    listOf(
                        "La pantalla parpadea.",
                        "No muestra imagen."
                    ),
                    listOf(
                        "Se reinicia inesperadamente.",
                        "Requiere limpieza interna."
                    ),
                    listOf(
                        "Pierde la conexión de red.",
                        "Presenta fallas en los puertos."
                    ),
                    listOf(
                        "Presenta sobrecalentamiento.",
                        "El ventilador produce ruido."
                    ),
                    listOf(
                        "La salida de voltaje es inestable.",
                        "Requiere revisión de terminales."
                    ),
                    listOf(
                        "No reconoce una de las sondas.",
                        "Requiere verificación de calibración."
                    )
                )

                val estados = listOf(
                    "Pendiente",
                    "En diagnóstico",
                    "En reparación",
                    "Finalizado"
                )

                // Garantiza cuatro registros de cada estado
                val estadosAleatorios =
                    (0 until 4).flatMap { estados }.shuffled()

                val prioridades = listOf(
                    "Baja", "Media", "Alta"
                )

                val tipos = listOf(
                    "Correctivo", "Preventivo"
                )

                var indiceEstado = 0

                val lote = System.currentTimeMillis()



                ejemplos.forEachIndexed { indice, ejemplo ->

                    val nombreDePrueba = "${ejemplo.first} - lote $lote"

                    if (nombreDePrueba !in nombresExistentes) {


                        val equipoId = repositorio.registrarEquipo(

                            EquipoEntity(
                                nombre = nombreDePrueba,
                                tipo = ejemplo.second,
                                ubicacion = ejemplo.third
                            )

                        )

                        problemas[indice].shuffled().forEach { problema ->

                            repositorio.registrarMantenimiento(
                                MantenimientoEntity(
                                    equipoId = equipoId,
                                    problema = problema,
                                    tipoMantenimiento = tipos.random(),
                                    estado = estadosAleatorios[indiceEstado++],
                                    prioridad = prioridades.random()
                                )
                            )
                        }
                    }
                }
            } finally {
                generandoDatosPrueba = false
            }
        }
    }


    fun eliminarEquipoConHistorial(id: Long) {
        viewModelScope.launch {
            repositorio.eliminarEquipoConHistorial(id)
        }
    }


    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val aplicacion = this[APPLICATION_KEY]
                    ?: error("No se pudo obtener el contexto de la aplicación")

                val baseDatos =
                    MantenimientoDatabase.obtenerInstancia(aplicacion)

                val repositorio = MantenimientoRepository(
                    baseDatos.equipoDao(),
                    baseDatos.mantenimientoDao()
                )

                MantenimientoViewModel(repositorio)
            }
        }
    }

}
