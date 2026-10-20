package com.example.mantenimientoelectronicoapp

/** Modelo local de una solicitud de mantenimiento. */
data class SolicitudMantenimiento(
    val codigo: String,
    val equipo: String,
    val tipo: String,
    val problema: String,
    val estado: String,
    val prioridad: String
)

/** Datos simulados reutilizados de la primera actividad. */
fun solicitudesIniciales(): List<SolicitudMantenimiento> = listOf(
    SolicitudMantenimiento("MT-001", "Dell Inspiron 15", "Computador portátil", "El equipo no enciende.", "Pendiente", "Alta"),
    SolicitudMantenimiento("MT-002", "Epson EcoTank L3150", "Impresora", "Presenta atascos frecuentes de papel.", "En diagnóstico", "Media"),
    SolicitudMantenimiento("MT-003", "Samsung 24 pulgadas", "Monitor", "La pantalla no muestra imagen.", "En reparación", "Alta"),
    SolicitudMantenimiento("MT-004", "Lenovo ThinkCentre", "Computador de escritorio", "Se reinicia después de varios minutos.", "Pendiente", "Media"),
    SolicitudMantenimiento("MT-005", "HP Pavilion", "Computador portátil", "La batería no recibe carga.", "Finalizado", "Baja"),
    SolicitudMantenimiento("MT-006", "TP-Link Archer C6", "Router", "Pierde la conexión de red constantemente.", "Pendiente", "Media")
)

/** Flujo sencillo y coherente de avance de un mantenimiento. */
fun siguienteEstado(estado: String): String? = when (estado) {
    "Pendiente" -> "En diagnóstico"
    "En diagnóstico" -> "En reparación"
    "En reparación" -> "Finalizado"
    else -> null
}

fun textoAccion(estado: String): String = when (estado) {
    "Pendiente" -> "Iniciar diagnóstico"
    "En diagnóstico" -> "Iniciar reparación"
    "En reparación" -> "Marcar como finalizado"
    else -> "Completado"
}
