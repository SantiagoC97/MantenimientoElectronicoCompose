package com.example.mantenimientoelectronicoapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction

@Dao
interface EquipoDao {

    @Query("SELECT * FROM equipos ORDER BY nombre ASC")
    fun observarEquipos(): Flow<List<EquipoEntity>>

    @Query("SELECT * FROM equipos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): EquipoEntity?

    @Insert
    suspend fun insertar(equipo: EquipoEntity): Long

    @Update
    suspend fun actualizar(equipo: EquipoEntity): Int

    @Delete
    suspend fun eliminar(equipo: EquipoEntity): Int


    @Query("DELETE FROM mantenimientos WHERE equipoId = :id")
    suspend fun eliminarHistorialDeEquipo(id: Long): Int

    @Query("DELETE FROM equipos WHERE id = :id")
    suspend fun eliminarEquipoPorId(id: Long): Int

    @Transaction
    suspend fun eliminarEquipoCompleto(id: Long) {
        eliminarHistorialDeEquipo(id)
        eliminarEquipoPorId(id)
    }

}
