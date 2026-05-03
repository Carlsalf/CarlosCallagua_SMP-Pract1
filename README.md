# Filmoteca - Prácticas SMP
## Práctica 1
- Listado de películas
- Detalle
- Edición básica
- XML + RecyclerView

-------------------------------

## Práctica 2 – Geocercas (Ubicación)

### Funcionalidad implementada

- Añadir geocercas a lugares de rodaje
- Eliminación de geocercas
- Uso de Geofencing API
- Preparado para notificaciones al entrar en zona

### Archivos clave

- GeofenceHelper.kt
- GeofenceBroadcastReceiver.kt
- FilmEditActivity.kt

### Limitaciones

- En el emulador no siempre se disparan eventos reales de geocercas
- La funcionalidad está correctamente implementada y lista para dispositivo físico

### Flujo de uso

1. Abrir edición de película
2. Guardar película
3. Pulsar "Añadir geocercado"
4. Conceder permisos de ubicación
5. Simular ubicación en emulador
6. Recepción de evento en dispositivo real o entorno compatible
