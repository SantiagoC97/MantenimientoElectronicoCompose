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

        editarMantenimiento(
            mantenimiento.copy(estado = nuevoEstado)
        )
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
