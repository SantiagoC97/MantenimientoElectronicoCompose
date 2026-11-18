# Segundo corte — Sistema de Mantenimiento Electrónico

**Asignatura:** Aplicativos Móviles  
**Tecnologías:** Kotlin, Jetpack Compose, Material 3, Navigation Compose, Room y MVVM

## Descripción

Esta versión amplía el Sistema de Mantenimiento Electrónico desarrollado en el primer corte. Permite administrar equipos y sus intervenciones de mantenimiento, con almacenamiento local persistente mediante Room. La aplicación puede ejecutarse desde el APK instalado en un dispositivo Android, sin abrir Android Studio ni conectar el teléfono al computador.

## Arquitectura

- **Datos:** `EquipoEntity` y `MantenimientoEntity`, relacionadas mediante `equipoId`.
- **Persistencia:** `MantenimientoDatabase`, `EquipoDao` y `MantenimientoDao`.
- **Repositorio:** `MantenimientoRepository`, intermediario entre Room y el ViewModel.
- **Estado de la interfaz:** `MantenimientoViewModel` con `Flow`/`StateFlow`.
- **Presentación:** pantallas en Jetpack Compose y navegación entre Inicio, Solicitudes, Equipos y Registrar.

Los cambios se almacenan en Room y se reflejan en las pantallas que observan los registros.

## Funcionalidades

1. **Gestión de equipos:** registrar equipos con nombre, tipo y ubicación; consultar los equipos almacenados.
2. **Registro de mantenimientos:** asociar un mantenimiento a un equipo existente, indicando descripción, tipo y prioridad.
3. **Consulta y búsqueda:** mostrar solicitudes almacenadas y buscar por código, equipo, tipo o descripción.
4. **Edición:** modificar la descripción, el tipo de mantenimiento y la prioridad de un registro existente.
5. **Estados:** avanzar de `Pendiente` a `En diagnóstico`, luego a `En reparación` y finalmente a `Finalizado`.
6. **Eliminación con confirmación:** eliminar un mantenimiento sin borrar su equipo; al eliminar un equipo, eliminar también sus mantenimientos asociados después de confirmar.
7. **Historial por equipo:** consultar en Equipos las intervenciones asociadas a cada dispositivo.
8. **Generación de datos de prueba:** agregar nuevos lotes de equipos y mantenimientos de ejemplo para comprobar el funcionamiento. Son registros simulados, pero se guardan realmente en Room.
9. **Resumen en Inicio:** mostrar contadores de solicitudes totales, pendientes, en proceso y finalizadas, actualizados según los datos almacenados.

## Instrucciones para probar

1. Instalar el APK y abrir **Sistema de Mantenimiento Electrónico**.
2. Entrar a **Equipos** y registrar un equipo. Alternativamente, seleccionar **Generar datos de prueba**.
3. Entrar a **Registrar**, seleccionar un equipo y crear un mantenimiento con descripción, tipo y prioridad.
4. Abrir **Solicitudes** y comprobar que el mantenimiento aparece.
5. Buscar el mantenimiento por nombre del equipo o por una palabra de su descripción.
6. Pulsar **Editar**, modificar sus datos y guardar los cambios.
7. Pulsar **Iniciar diagnóstico**, después **Iniciar reparación** y continuar hasta **Finalizado**. El registro debe conservarse y cambiar únicamente de estado.
8. Entrar a **Equipos → Ver historial** y comprobar que el mantenimiento aparece asociado al equipo correcto.
9. Eliminar un mantenimiento de prueba y comprobar que desaparece de Solicitudes, sin eliminar el equipo.
10. Eliminar un equipo de prueba, confirmar la acción y comprobar que también desaparecen sus mantenimientos asociados y que el resumen de Inicio se actualiza.
11. Cerrar completamente la aplicación y volver a abrirla para verificar que los registros restantes y sus estados se conservan.

## Pruebas realizadas

Se comprobó el funcionamiento en un teléfono Android Huawei: registro de equipos y mantenimientos, consulta, búsqueda, edición, avance de estados, eliminación con confirmación, historial, generación de datos de prueba y persistencia tras reiniciar la aplicación. También se verificó que la aplicación instalada funciona sin conexión USB y con Android Studio cerrado.

## Instalación

El entregable incluye un archivo `.apk` instalable. Para probar la aplicación no es necesario compilar el proyecto: basta con instalar el APK en un dispositivo Android compatible y abrirla desde su ícono.

## Repositorio

**Repositorio:** https://github.com/SantiagoC97/MantenimientoElectronicoCompose  
**Rama del segundo corte:** [`segundo-corte-room`](https://github.com/SantiagoC97/MantenimientoElectronicoCompose/tree/segundo-corte-room)

> Este documento describe las funcionalidades y pruebas del segundo corte. La versión del primer corte se conserva en la rama `main`.
