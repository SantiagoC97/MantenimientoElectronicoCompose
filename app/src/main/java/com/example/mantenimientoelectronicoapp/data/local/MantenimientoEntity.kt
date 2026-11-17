package com.example.mantenimientoelectronicoapp.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mantenimientos",
    foreignKeys = [
        ForeignKey(
            entity = EquipoEntity::class,
            parentColumns = ["id"],
            childColumns = ["equipoId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["equipoId"])]
)
data class MantenimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipoId: Long,
    val problema: String,
    val tipoMantenimiento: String = "Correctivo",
    val estado: String = "Pendiente",
    val prioridad: String = "Media",
    val fechaRegistro: Long = System.currentTimeMillis()
)
