package com.example.mantenimientoelectronicoapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MantenimientoDao {

    @Query(
        "SELECT * FROM mantenimientos " +
                "ORDER BY fechaRegistro DESC, id DESC"
    )
    fun observarMantenimientos(): Flow<List<MantenimientoEntity>>

    @Query(
        "SELECT * FROM mantenimientos " +
                "WHERE equipoId = :equipoId " +
                "ORDER BY fechaRegistro DESC, id DESC"
    )
    fun observarPorEquipo(
        equipoId: Long
    ): Flow<List<MantenimientoEntity>>

    @Query("SELECT * FROM mantenimientos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): MantenimientoEntity?

    @Insert
    suspend fun insertar(
        mantenimiento: MantenimientoEntity
    ): Long

    @Update
    suspend fun actualizar(
        mantenimiento: MantenimientoEntity
    ): Int

    @Delete
    suspend fun eliminar(
        mantenimiento: MantenimientoEntity
    ): Int
}
