package com.example.mantenimientoelectronicoapp.data

import com.example.mantenimientoelectronicoapp.data.local.EquipoDao
import com.example.mantenimientoelectronicoapp.data.local.EquipoEntity
import com.example.mantenimientoelectronicoapp.data.local.MantenimientoDao
import com.example.mantenimientoelectronicoapp.data.local.MantenimientoEntity
import kotlinx.coroutines.flow.Flow

class MantenimientoRepository(
    private val equipoDao: EquipoDao,
    private val mantenimientoDao: MantenimientoDao
) {

    // EQUIPOS

    val equipos: Flow<List<EquipoEntity>> =
        equipoDao.observarEquipos()

    suspend fun obtenerEquipo(id: Long): EquipoEntity? =
        equipoDao.obtenerPorId(id)

    suspend fun registrarEquipo(equipo: EquipoEntity): Long =
        equipoDao.insertar(equipo)

    suspend fun actualizarEquipo(equipo: EquipoEntity): Int =
        equipoDao.actualizar(equipo)

    suspend fun eliminarEquipo(equipo: EquipoEntity): Int =
        equipoDao.eliminar(equipo)


    // MANTENIMIENTOS

    val mantenimientos: Flow<List<MantenimientoEntity>> =
        mantenimientoDao.observarMantenimientos()

    fun observarHistorial(
        equipoId: Long
    ): Flow<List<MantenimientoEntity>> =
        mantenimientoDao.observarPorEquipo(equipoId)

    suspend fun obtenerMantenimiento(
        id: Long
    ): MantenimientoEntity? =
        mantenimientoDao.obtenerPorId(id)

    suspend fun registrarMantenimiento(
        mantenimiento: MantenimientoEntity
    ): Long =
        mantenimientoDao.insertar(mantenimiento)

    suspend fun actualizarMantenimiento(
        mantenimiento: MantenimientoEntity
    ): Int =
        mantenimientoDao.actualizar(mantenimiento)

    suspend fun eliminarMantenimiento(
        mantenimiento: MantenimientoEntity
    ): Int =
        mantenimientoDao.eliminar(mantenimiento)
}
