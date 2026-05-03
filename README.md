# Filmoteca - Prácticas SMP

## Práctica 1
- Listado de películas
- Detalle
- Edición básica
- XML + RecyclerView

-------------------------------

## Práctica 2 – Geocercas (SceneKit/Ubicación)

### Funcionalidad implementada:
- Añadir geocercas a lugares de rodaje
- Eliminación de geocercas
- Uso de Geofencing API
- Notificaciones al entrar en zona

### Archivos clave:
- GeofenceHelper.kt
- GeofenceBroadcastReceiver.kt
- FilmEditActivity.kt

### Limitaciones:
- En emulador no se disparan eventos reales
- Probado mediante simulación de ubicación

---

##  Flujo de uso
1. Abrir edición de película
2. Guardar película
3. Pulsar "Añadir geocercado"
4. Conceder permisos de ubicación
5. Simular ubicación en emulator
6. Recibir notificación
