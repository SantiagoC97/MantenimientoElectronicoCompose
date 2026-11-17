package com.example.mantenimientoelectronicoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        EquipoEntity::class,
        MantenimientoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MantenimientoDatabase : RoomDatabase() {

    abstract fun equipoDao(): EquipoDao

    abstract fun mantenimientoDao(): MantenimientoDao

    companion object {

        @Volatile
        private var INSTANCIA: MantenimientoDatabase? = null

        fun obtenerInstancia(context: Context): MantenimientoDatabase {
            return INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: Room.databaseBuilder(
                    context.applicationContext,
                    MantenimientoDatabase::class.java,
                    "mantenimiento_electronico.db"
                ).build().also {
                    INSTANCIA = it
                }
            }
        }
    }
}
