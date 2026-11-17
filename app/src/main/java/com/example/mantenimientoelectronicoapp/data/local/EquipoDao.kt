package com.example.mantenimientoelectronicoapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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
}
